package com.m4sg.crm4marketingsunset.resources;

import com.google.common.base.Optional;
import com.m4c.model.entity.Offer;
import com.m4c.model.entity.online.Country;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.services.CountryService;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Juan on 1/14/2015.
 */
@Path("/v1/countries")
@Produces(MediaType.APPLICATION_JSON)
public class CountryResource extends GenericResource<Country> {

    CountryService countryService = new CountryService();
    public CountryResource(Validator validator) {
        super(validator);
    }

    @GET
    @UnitOfWork
    public Response getCountries(@Auth SimplePrincipal isAuthenticated) {
        List<Country> countries = countryService.listCountries();
        return Response.ok(countries).build();
    }

    @GET
    @UnitOfWork
    @Path("/{code}")
    public Response getCountry(@PathParam("code") String code,
                               @Auth SimplePrincipal isAuthenticated) {
        final Optional<Country> countryOptional = countryService.getCountry(code);
        Country country = (Country) findSafely(countryOptional, Offer.TAG);
        return Response.ok(country).build();
    }
}
