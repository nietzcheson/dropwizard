package com.m4sg.crm4marketingsunset.resources;

import com.google.common.base.Optional;
import com.m4c.model.entity.Language;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.services.LanguageService;
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
 * Created by Juan on 1/28/2015.
 */
@Path("/v1/languages")
@Produces(MediaType.APPLICATION_JSON)
public class LanguageResource extends GenericResource<Language> {

    LanguageService languageService = new LanguageService();

    public LanguageResource(Validator validator) {
        super(validator);
    }

    @GET
    @UnitOfWork
    public Response getLanguages(@Auth SimplePrincipal isAuthenticated) {
        List<Language> languages = languageService.listLanguages();
        return Response.ok(languages).build();
    }

    @GET
    @UnitOfWork
    @Path("/{code}")
    public Response getLanguage(@PathParam("code") String code, @Auth SimplePrincipal isAuthenticated) {
        final Optional<Language> languageOptional = languageService.getLanguage(code);
        Language language = findSafely(languageOptional, Language.TAG);
        return Response.ok(language).build();
    }
}
