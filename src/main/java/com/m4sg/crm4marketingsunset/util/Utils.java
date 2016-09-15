package com.m4sg.crm4marketingsunset.util;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;

import javax.annotation.Nullable;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by Fernando on 18/03/2015.
 */
public class Utils {

    public static void isValid(Optional optional,String property){

        if(!optional.isPresent()){
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation(property, "Does not exists.")).build());
        }

    }
    public String getPropValue(String property) throws IOException{
        Properties prop = new Properties();
        String propFileName = "config.properties";

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

        if (inputStream != null) {
            prop.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
        }
    return prop.getProperty(property);
    }

public static boolean isNumber(String number){
    Pattern patternInteger = Pattern.compile("^[0-9]+$");
    return patternInteger.matcher(number).matches();

}
    public static boolean isMail(String email){
        Pattern patternInteger = Pattern.compile(".+@.+\\..+");
        return patternInteger.matcher(email).matches();

    }

    public static List<Long> transformStringToLong(List<String> ids){

        return new ArrayList<Long>(Lists.transform(ids, new Function<String, Long>() {
            @Nullable
            @Override
            public Long apply(String s) {
                Long idCallcenter;
                try {
                    idCallcenter = Long.parseLong(s);
                } catch (NumberFormatException ex) {
                    idCallcenter = 0l;
                }
                return idCallcenter;

            }
        }));

    }
    public static boolean isNumeric(String inputData) {
        Scanner sc = new Scanner(inputData);
        return sc.hasNextInt();
    }
}
