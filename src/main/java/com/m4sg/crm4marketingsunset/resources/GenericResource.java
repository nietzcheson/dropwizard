package com.m4sg.crm4marketingsunset.resources;

import com.google.common.base.Optional;
import com.sun.jersey.api.NotFoundException;

import javax.validation.Validator;

/**
 * Created by sistemas on 07/01/2015.
 */
public abstract class GenericResource<E> {

    protected final Validator validator;

    public GenericResource(Validator validator) {
        this.validator=validator;
    }

    protected E findSafely(Optional<E> optional, String tag) {
        if (!optional.isPresent()) {
            throw new NotFoundException("No such " + tag + ".");
        }

        return optional.get();
    }

}
