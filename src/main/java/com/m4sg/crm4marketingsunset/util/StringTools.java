package com.m4sg.crm4marketingsunset.util;

import com.javeros.anonimos.code.Rfc;
import com.javeros.anonimos.code.dto.PersonaRfcDto;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sistemas on 09/01/2015.
 */
public class StringTools {


    public static String generaToken(int length) {
        String token = "";
        String letras = "A1B2C3D4E5F6G7H8J9K1L2M3N4O5P6Q7R8S9T0U5V4W3X4Y3Z";
        Long i = 0l;
        int size = letras.length();
        int rangoi = 0;
        int rangof = letras.length();

        while (token.length() < length) {
            i = Math.round((Math.random() * (rangof - rangoi)));
            if ((i >= 0) && (i < size)) {
                token += letras.charAt(Integer.valueOf(String.valueOf(i)));
            }
        }

        return token;
    }

    public static String slugify(String string, Boolean lowercase) {
        string = noAccents(string);
        // Apostrophes.
        string = string.replaceAll("([a-z])'s([^a-z])", "$1s$2");
        string = string.replaceAll("[^\\w]", "-").replaceAll("-{2,}", "-");
        // Get rid of any - at the start and end.
        string = string.replaceAll("-+$", "").replaceAll("^-+", "");

        return (lowercase ? string.toLowerCase() : string);
    }
    public static String replaceEspecialCharacterByHyphen(String string){
        string = noAccents(string);
        // Apostrophes.
        string = string.replaceAll("([a-z])'s([^a-z])", "$1s$2");
        string = string.replaceAll("[^\\s^\\w]","").replaceAll("-{2,}", "-");
        // Get rid of any - at the start and end.
        string = string.replaceAll("-+$", "").replaceAll("^-+", "");
        return string;
    }
    public static String noAccents(String string) {
        return Normalizer.normalize(string, Normalizer.Form.NFKC).replaceAll("[àáâãäåāąă]", "a").replaceAll("[çćčĉċ]", "c").replaceAll("[ďđð]", "d").replaceAll("[èéêëēęěĕė]", "e").replaceAll("[ƒſ]", "f").replaceAll("[ĝğġģ]", "g").replaceAll("[ĥħ]", "h").replaceAll("[ìíîïīĩĭįı]", "i").replaceAll("[ĳĵ]", "j").replaceAll("[ķĸ]", "k").replaceAll("[łľĺļŀ]", "l").replaceAll("[ñńňņŉŋ]", "n").replaceAll("[òóôõöøōőŏœ]", "o").replaceAll("[Þþ]", "p").replaceAll("[ŕřŗ]", "r").replaceAll("[śšşŝș]", "s").replaceAll("[ťţŧț]", "t").replaceAll("[ùúûüūůűŭũų]", "u").replaceAll("[ŵ]", "w").replaceAll("[ýÿŷ]", "y").replaceAll("[žżź]", "z").replaceAll("[æ]", "ae").replaceAll("[ÀÁÂÃÄÅĀĄĂ]", "A").replaceAll("[ÇĆČĈĊ]", "C").replaceAll("[ĎĐÐ]", "D").replaceAll("[ÈÉÊËĒĘĚĔĖ]", "E").replaceAll("[ĜĞĠĢ]", "G").replaceAll("[ĤĦ]", "H").replaceAll("[ÌÍÎÏĪĨĬĮİ]", "I").replaceAll("[Ĵ]", "J").replaceAll("[Ķ]", "K").replaceAll("[ŁĽĹĻĿ]", "L").replaceAll("[ÑŃŇŅŊ]", "N").replaceAll("[ÒÓÔÕÖØŌŐŎ]", "O").replaceAll("[ŔŘŖ]", "R").replaceAll("[ŚŠŞŜȘ]", "S").replaceAll("[ÙÚÛÜŪŮŰŬŨŲ]", "U").replaceAll("[Ŵ]", "W").replaceAll("[ÝŶŸ]", "Y").replaceAll("[ŹŽŻ]", "Z").replaceAll("[ß]", "ss");
    }

    public static String generateRFC(String  name,String aPaterno,String aMaterno,Date birthDate) {

        Rfc rfc = new Rfc();
        PersonaRfcDto personaRfcDto = new PersonaRfcDto();

        personaRfcDto.setNombre(name);
        if(aMaterno!=null && !aMaterno.isEmpty() && aMaterno.length()>2){
            personaRfcDto.setApMaterno(aMaterno);
        }
        else if(aPaterno!=null && !aPaterno.isEmpty() && aPaterno.length()>2){
            personaRfcDto.setApMaterno(aPaterno);
        }else{
            personaRfcDto.setApMaterno(name);
        }
        if(aPaterno!=null && !aPaterno.isEmpty() && aPaterno.length()>2){
            personaRfcDto.setApPaterno(aPaterno);
        }else{
            personaRfcDto.setApPaterno(name);
        }

        if(birthDate!=null) {

            personaRfcDto.setFecha(new SimpleDateFormat("yyyyMMdd").format(birthDate));
        }else{
            personaRfcDto.setFecha("2000/01/01");
        }
        return rfc.generarRfc(personaRfcDto);
    }

    public static String extractFirstEmail(String input){

        Pattern p = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(input);
        String email="";
        while(matcher.find()) {
            email=matcher.group(0);
            break;
        }

        return email;

    }

}
