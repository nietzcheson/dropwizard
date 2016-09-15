package com.m4sg.crm4marketingsunset.util;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Years;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Fernando on 06/04/2015.
 */
public class DateUtils {
    public static Date stringToDateYYYYMMDD(String dateString){

        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        try {
            return simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Integer getAge(Integer day, Integer month, Integer year) {
        Integer age = null;
        if(year!=null){
            if(month==null || month==0) month=1;
            if(day==null || day==0) day=1;
            DateMidnight birthDate = new DateMidnight(year, month, day);
            DateTime now = new DateTime();
            Years yage = Years.yearsBetween(birthDate, now);
            age = yage.getYears();
        }

        return age;
    }
}
