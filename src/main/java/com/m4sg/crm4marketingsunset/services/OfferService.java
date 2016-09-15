package com.m4sg.crm4marketingsunset.services;

import com.google.common.base.Optional;
import com.m4c.model.entity.*;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.dto.OfferDTO;
import com.m4sg.crm4marketingsunset.core.dto.OfferLanguageDTO;
import com.m4sg.crm4marketingsunset.core.dto.PaginationDTO;
import com.m4sg.crm4marketingsunset.dao.*;
import com.m4sg.crm4marketingsunset.util.FindSafely;
import org.hamcrest.Matchers;
import org.hibernate.QueryException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.*;

import static ch.lambdaj.Lambda.*;

/**
 * Created by Juan on 1/12/2015.
 */
public class OfferService extends GenericDAOService {


    public OfferService() {
        super(OfferDAO.class, MealPlanDAO.class, CountryDAO.class,
                DestinationDAO.class, HookDAO.class, TransportationDAO.class,
                LanguageDAO.class, OfferLanguageDAO.class);
    }

    public Offer createOffer(OfferDTO offerDTO) {
        Optional<Offer> offerOptional = offerDAO.find(offerDTO.getName());
        if(offerOptional.isPresent()) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("name", "Offer already exist.")).build());
        }
        Offer offer = createOfferObject(offerDTO);

        if(offerDTO.getDestinationIds().length > 0) {
            createOfferDestinations(offer, offerDTO.getDestinationIds());
        }
        offer = offerDAO.saveOrUpdate(offer);
        if(offerDTO.getTranslations().size() > 0) {
            Set<OfferLanguage> translations = createTranslations(offer, offerDTO.getTranslations());
            offer.setTranslations(translations);
            return offerDAO.saveOrUpdate(offer);
        }
        return offer;
    }

    private Offer createOfferObject(OfferDTO offerDTO) {
        // Check if already exists
        MealPlan mealPlan = mealPlanDAO.find(offerDTO.getMealPlanId()).get();
        Transportation transportation = transportationDAO.find(offerDTO.getTransportationId()).get();
        Hook hook = hookDAO.find(offerDTO.getHookId()).get();

        Date dateCreated = new Date();

        return new Offer(offerDTO.getName(), mealPlan, offerDTO.getPrice(),
                offerDTO.getActivationFee(), offerDTO.getTaxes(),
                offerDTO.getStatus(), transportation,
                offerDTO.getDescription(), hook, offerDTO.getNights(), dateCreated, dateCreated);
    }

    private void createOfferDestinations(Offer offer, Integer[] destinationIds) {
        Set<Destination> destinations = new HashSet<Destination>();
        OfferDestinationService offerDestinationService=new OfferDestinationService();
        for (int i = 0; i < destinationIds.length; i++) {
            Destination destination = destinationDAO.find(destinationIds[i]).get();
            destinations.add(destination);
        }

        if(destinationIds.length == 0 && offer.getDestinations().size() > 0) {
            // We first need to get them and then add them to the remove
            offer.getDestinations().clear();
            return;
        }
        for (Destination destination: destinations) {
            int count = select(offer.getDestinations(), having(on(OfferDestination.class).getDestination(), Matchers.equalTo(destination))).size();
            if(count == 0) {
                offer.getDestinations().add(new OfferDestination(offer, destination));
            }
        }

        List<OfferDestination> offerDestinationListAux=new ArrayList<OfferDestination>(offer.getDestinations());
        for (OfferDestination offerDestination: offerDestinationListAux) {
            int count = select(destinations, having(on(Destination.class), Matchers.equalTo(offerDestination.getDestination()))).size();
            if(count == 0) {
                offer.getDestinations().remove(offerDestination);
                offerDestinationService.delete(offer,offerDestination.getDestination());
            }
        }
    }


    private void updateOfferLanguages(Offer offer, Set<OfferLanguageDTO> offerLanguageDTOs) {
        Set<OfferLanguage> translations = createTranslations(offer, offerLanguageDTOs);
        if(offer.getTranslations().equals(translations)) {
            return;
        }
        offerLanguageDAO.deleteOfferLanguages(offer);
        offer.getTranslations().addAll(translations);
    }

    private Set<OfferLanguage> createTranslations(Offer offer, Set<OfferLanguageDTO> offerLanguageDTOs) {
        Set<OfferLanguage> languages = new HashSet<OfferLanguage>();
        for(OfferLanguageDTO offerLanguageDTO: offerLanguageDTOs) {
            Optional<Language> languageOptional = languageDAO.find(offerLanguageDTO.getLanguageCode());
            Language language = languageOptional.get();
            OfferLanguage translation =
                    new OfferLanguage(offer, language,
                            offerLanguageDTO.getWebsiteDescription(), offerLanguageDTO.getWebsiteDetails(), offerLanguageDTO.getWebsiteTerms());
            languages.add(translation);
        }
        return languages;
    }


    public Offer updateOffer(long id, OfferDTO offerDTO) {
        Optional<Offer> offerOptional = offerDAO.find(id);
        Offer offer =  (Offer) FindSafely.findSafely(offerOptional, Offer.TAG);
        List<Offer> all = offerDAO.findAll(offerDTO.getName());

        if((all.indexOf(offer) < 0 && all.size() > 0) ||
                all.size() > 1) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("name", "Offer name cannot be duplicated.")).build());
        }
        setParams(offerDTO, offer);
        return offerDAO.saveOrUpdate(offer);
    }

    private void setParams(OfferDTO offerDTO, Offer offer) {
        Date dateUpdated = new Date();

        if(null != offerDTO.getName()) {
            offer.setName(offerDTO.getName());
        }
        if(null != offerDTO.getMealPlanId()) {
            // Gets the mealPlan Object
            MealPlan mealPlan = mealPlanDAO.find(offerDTO.getMealPlanId()).get();
            offer.setMealPlan(mealPlan);
        }

        if(null != offerDTO.getPrice()) {
            offer.setPrice(offerDTO.getPrice());
        }
        if(null != offerDTO.getActivationFee()) {
            offer.setActivationFee(offerDTO.getActivationFee());
        }
        if(null != offerDTO.getTaxes()) {
            offer.setTaxes(offerDTO.getTaxes());
        }
        if(null != offerDTO.getDestinationIds()) {
            createOfferDestinations(offer, offerDTO.getDestinationIds());

        }
        if(null != offerDTO.getStatus()) {
            offer.setStatus(offerDTO.getStatus());
        }
        if(null != offerDTO.getTransportationId()) {
            Transportation transportation = transportationDAO.find(offerDTO.getTransportationId()).get();
            offer.setTransportation(transportation);
        }
        if(null != offerDTO.getDescription()) {
            offer.setDescription(offerDTO.getDescription());
        }
        if(null != offerDTO.getHookId()) {
            // Check if it's a valid hook
            Hook hook = hookDAO.find(offerDTO.getHookId()).get();
            offer.setHook(hook);
        }
        if(null != offerDTO.getNights()) {
            offer.setNights(offerDTO.getNights());
        }
        if(null != offerDTO.getTranslations()) {
            updateOfferLanguages(offer, offerDTO.getTranslations());
        }

        offer.setDateUpdated(dateUpdated);
    }

    public Optional<Offer> getOffer(long offerId) {
        return offerDAO.find(offerId);
    }

    public PaginationDTO<Offer> listOffers(int pageLength, int page, String order, String orderBy) {
        try {
            return offerDAO.findAll(pageLength, page, order, orderBy);
        } catch (QueryException e) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("orderBy", e.getMessage())).build());
        }
    }

    public PaginationDTO<Offer> listOffers(int pageLength, int page, String order, String orderBy, String query) {
        // Add query
        try {
            return offerDAO.findAll(pageLength, page, order, orderBy, query);
        } catch (QueryException e) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("orderBy", e.getMessage())).build());
        }
    }

}
