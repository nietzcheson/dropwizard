package com.m4sg.crm4marketingsunset.services;

import com.google.common.base.Optional;
import com.m4c.model.entity.*;
import com.m4c.model.entity.online.Country;
import com.m4c.model.entity.online.State;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.dto.HotelDTO;
import com.m4sg.crm4marketingsunset.core.dto.HotelLanguageDTO;
import com.m4sg.crm4marketingsunset.dao.*;
import com.m4sg.crm4marketingsunset.util.Constants;
import com.m4sg.crm4marketingsunset.util.FindSafely;
import com.sun.jersey.api.NotFoundException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;

/**
 * Created by Juan on 1/26/2015.
 */
public class HotelService extends GenericDAOService {
    public HotelService() {
        super( HotelDAO.class, DestinationDAO.class, CountryDAO.class, StateDAO.class, LanguageDAO.class,
                HotelImageDAO.class, HotelLanguageDAO.class,OfferDAO.class);
    }

    public List<Hotel> listHotels(){
        return hotelDAO.findAll();
    }
    public List<Hotel>findByOffer(long offerId){

        Optional<Offer> offerOptional=offerDAO.find(offerId);

        if(!offerOptional.isPresent()) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("offer", "Offer Does not exist.")).build());
        }

        return hotelDAO.findByOffer(offerOptional.get());

    }

    public Optional<Hotel> getHotel(long hotelId){
        return hotelDAO.find(hotelId);
    }

    public Hotel createHotel(HotelDTO hotelDTO) {
        Optional<Hotel> hotelOptional = hotelDAO.find(hotelDTO.getName());
        if(hotelOptional.isPresent()) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("name", "Hotel already exist.")).build());
        }
        Hotel hotel = createHotelObject(hotelDTO);
        hotel = hotelDAO.saveOrUpdate(hotel);

        if(hotelDTO.getTranslations().size() > 0) {
            Optional<Hotel> newHotelOptional = hotelDAO.find(hotel.getId());
            hotel = newHotelOptional.get();
            Set<HotelLanguage> translations = createTranslations(hotel, hotelDTO.getTranslations());
            hotel.getTranslations().addAll(translations);
            return hotelDAO.saveOrUpdate(hotel);
        }
        return hotel;
    }

    private Hotel createHotelObject(HotelDTO hotelDTO) {
        int active = 1;
        int ageAdults = 13;
        Destination destination = destinationDAO.find(hotelDTO.getDestinationId()).get();
        Country country = countryDAO.find(hotelDTO.getCountryCode()).get();
        State state = stateDAO.find(hotelDTO.getStateId()).get();

        return new Hotel(hotelDTO.getName(), active, hotelDTO.getAddress(),
                hotelDTO.getWebsite(), null, null, null, ageAdults, null, destination, country, state, hotelDTO.getCity(), hotelDTO.getZipCode() );
    }

    public Hotel updateHotel(long id, HotelDTO hotelDTO) throws IOException {
        // Validate if exists before
        Optional<Hotel> hotelOptional = hotelDAO.find(id);
        Hotel hotel = (Hotel) FindSafely.findSafely(hotelOptional, Hotel.TAG);
        // Validate if it's not a duplicate name
        List<Hotel> all = hotelDAO.findAll(hotelDTO.getName());
        if((all.indexOf(hotel) < 0 && all.size() > 0) ||
                all.size() > 1) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("name", "Hotel name cannot be duplicated.")).build());
        }
        setParams(hotelDTO, hotel);
        return hotelDAO.saveOrUpdate(hotel);
    }

    private void setParams(HotelDTO hotelDTO, Hotel hotel) {
        if (null != hotelDTO.getName()) {
            hotel.setName(hotelDTO.getName());
        }
        if (null != hotelDTO.getDestinationId()) {
            Destination destination = destinationDAO.find(hotelDTO.getDestinationId()).get();
            hotel.setDestination(destination);
        }

        if (null != hotelDTO.getCity()) {
            hotel.setCity(hotelDTO.getCity());
        }
        if (null != hotelDTO.getZipCode()) {
            hotel.setZipCode(hotelDTO.getZipCode());
        }
        if (null != hotelDTO.getAddress()) {
            hotel.setAddress(hotelDTO.getAddress());
        }
        if (null != hotelDTO.getTranslations()) {
            updateHotelLanguages(hotel, hotelDTO.getTranslations());
        }
        if (null != hotelDTO.getWebsite()) {
            hotel.setUrl(hotelDTO.getWebsite());
        }
    }

    private void updateHotelLanguages(Hotel hotel, Set<HotelLanguageDTO> hotelLanguageDTOs) {
        Set<HotelLanguage> translations = createTranslations(hotel, hotelLanguageDTOs);
        if(hotel.getTranslations().equals(translations)) {
            return;
        }
        hotelLanguageDAO.deleteHotelLanguages(hotel);
        hotel.getTranslations().addAll(translations);
    }

    public Hotel createHotelImage(long id, String fileLocation, String token,String url) {
        Optional<Hotel> hotelOptional = hotelDAO.find(id);
        Hotel hotel = (Hotel) FindSafely.findSafely(hotelOptional, Hotel.TAG);
        hotel.getImages().add(new HotelImage(hotel, token, fileLocation, url));
        return hotelDAO.saveOrUpdate(hotel);
    }

    public Hotel deleteHotelImage(long hotelId, String tokenImage) {
        Optional<Hotel> hotelOptional = hotelDAO.find(hotelId);
        Hotel hotel = (Hotel) FindSafely.findSafely(hotelOptional, Hotel.TAG);
        // Get the hotelImage object
        Optional<HotelImage> imageOptional = hotelImageDAO.find(hotel,tokenImage);
        if(!imageOptional.isPresent()) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("image", "Image does not exist.")).build());
        }
        HotelImage hotelImage = imageOptional.get();

        hotel.getImages().remove(hotelImage);
        deleteFile(Constants.PATH_HOTEL_IMAGES,hotelImage.getPath());
        hotelImageDAO.deleteHotelImage(hotelImage);
        return hotelDAO.saveOrUpdate(hotel);
    }
    public File getImage(long hotelId,String tokenImage){

        Optional<Hotel> hotelOptional = hotelDAO.find(hotelId);
        Hotel hotel = (Hotel) FindSafely.findSafely(hotelOptional, Hotel.TAG);
        Optional<HotelImage> hotelImage=hotelImageDAO.find(hotel, tokenImage);

         return getFile(hotelImage.get().getPath());
    }

    public File getFile(String path){
        File file = new File(path);
        System.out.println("Verifica si la imagen existe..");
        if (!file.exists()) {
            throw new NotFoundException("No such image.");
        }
        System.out.println("La imagen existe..");
        return file;
    }

    public boolean deleteFile(String path,String name){
        File file = new File(path.concat(name));
        boolean isRemoved=true ;
        if (!file.exists()) {
            isRemoved=file.delete();
        }
        return  isRemoved;
    }


    private Set<HotelLanguage> createTranslations(Hotel hotel, Set<HotelLanguageDTO> hotelLanguageDTOs) {
        Set<HotelLanguage> languages = new HashSet<HotelLanguage>();
        for(HotelLanguageDTO hotelLanguageDTO: hotelLanguageDTOs) {
            Optional<Language> languageOptional = languageDAO.find(hotelLanguageDTO.getLanguageCode());
            Language language = languageOptional.get();
            HotelLanguage translation =
                    new HotelLanguage(hotel, language,
                            hotelLanguageDTO.getWebsiteDescription());
            languages.add(translation);
        }
        return languages;
    }

    public List<Hotel>getHotelsByOffer(Offer offer){

        List<Destination> destinationLst=extract(offer.getDestinations(),on(OfferDestination.class).getDestination());

        return hotelDAO.findByDestinationLst(destinationLst);

    }
}
