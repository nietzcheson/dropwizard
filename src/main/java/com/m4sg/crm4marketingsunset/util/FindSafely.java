package com.m4sg.crm4marketingsunset.util;

import com.google.common.base.Optional;
import com.sun.jersey.api.NotFoundException;

/**
 * Created by Juan on 26/02/2015.
 */
public class FindSafely {

    private FindSafely() {}

    public static Object findSafely(Optional optional, String tag) {
        if (!optional.isPresent()) {
            throw new NotFoundException("No such " + tag + ".");
        }
        return optional.get();
    }

}
