package com.m4sg.crm4marketingsunset.services;

import com.google.common.base.Optional;
import com.m4c.model.entity.Destination;
import com.m4c.model.entity.online.Country;
import com.m4c.model.entity.online.State;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.dto.DestinationDTO;
import com.m4sg.crm4marketingsunset.dao.DestinationDAO;
import com.m4sg.crm4marketingsunset.util.FindSafely;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Juan on 1/14/2015.
 */
public class DestinationService extends GenericDAOService {


    public DestinationService() {
        super(DestinationDAO.class);
    }

    public List<Destination> listDestinations(){
        return destinationDAO.findAll();
    }

    public Optional<Destination> getDestination(long destinationId) {
        return destinationDAO.find(destinationId);
    }

    public Destination createDestination(DestinationDTO destinationDTO) {
        Optional<Destination> destinationOptional = destinationDAO.find(destinationDTO.getName());
        StateService service=new StateService();
        Optional<State> stateOptional =null;
        if(destinationDTO.getStateId()!=null && destinationDTO.getStateId()>0l) {
             stateOptional = service.getState(destinationDTO.getStateId());
        }

        CountryService countryService=new CountryService();
        Optional<Country> countryOptional=countryService.getCountry(destinationDTO.getCountryId());
    if(stateOptional!=null){
        if(!stateOptional.isPresent()) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("state", "State does not exist.")).build());
        }
    }
        if(!countryOptional.isPresent()) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("country", "Country does not exist.")).build());
        }
        if(destinationOptional.isPresent()) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("name", "Destination already exist.")).build());
        }
        State state=null;
        if(stateOptional!=null){
            state=  stateOptional.get();
        }
        Destination destination = new Destination(destinationDTO.getName(), destinationDTO.getDescription(), destinationDTO.getActive(),state,countryOptional.get());
        return destinationDAO.saveOrUpdate(destination);
    }

    public Destination updateDestination(long id, DestinationDTO destinationDTO) {
        //Checks if the destination exists
        Optional<Destination> destinationOptional = destinationDAO.find(id);
        Destination destination = (Destination) FindSafely.findSafely(destinationOptional, Destination.TAG);
        List<Destination> all = destinationDAO.findAll(destinationDTO.getName());
        // Check if destination name is not duplicate
        if((all.indexOf(destination) < 0 && all.size() > 0) ||
                all.size() > 1) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("name", "Destination name cannot be duplicated.")).build());
        }

        StateService service=new StateService();
        Optional<State> stateOptional=service.getState(destinationDTO.getStateId());

        CountryService countryService=new CountryService();
        Optional<Country> countryOptional=countryService.getCountry(destinationDTO.getCountryId());



        if(null != destinationDTO.getName()) {
            destination.setName(destinationDTO.getName());
        }

        if(null != destinationDTO.getDescription()) {
            destination.setDescription(destinationDTO.getDescription());
        }

        if(null != destinationDTO.getActive()) {
            destination.setActive(destinationDTO.getActive());
        }
        destination.setState(stateOptional.get());
        destination.setCountry(countryOptional.get());

        return destinationDAO.saveOrUpdate(destination);
    }
}
