package com.m4sg.crm4marketingsunset.util;

/**
 * Created by Juan on 2/9/2015.
 */
public final class Constants {

    private Constants() {
        // Nothing to do
    }

    public static final String ORDER_ASC = "asc";
    public static final String ORDER_DESC = "desc";

    public static final String PATH_HOTEL_IMAGES = "/opt/data/hotels/";
    //public static final String PATH_ECERT_IMAGES = "C:\\temp";
    public static final String PATH_ECERT_IMAGES = "/opt/data/ecert/";
    public static final String PATH_BROKER_IMAGES = "/opt/data/site/";

    public static final String PATH_CERT_TYPE_IMAGES = "/opt/data/cert_type/";
    //public static final String PATH_CERT_TYPE_IMAGES = "/Users/jhon/Documents/Basura/cert_type/";

    public static final String PUBLIC_SERVER = "10.194.18.210"; //10.194.18.210
    public static final Integer PUBLIC_SERVER_PORT_SSH = 9200;
    public static final String PRIVATE_SERVER = "10.194.17.173";//10.194.17.173
    public static final Integer PRIVATE_SERVER_PORT_SSH = 22;

    public static final String PUBLIC_SERVER_FTP_USERNAME = "crmm4sg";
    public static final String PUBLIC_SERVER_FTP_PASSWORD = "CRM$0108";
    public static final String PRIVATE_SERVER_FTP_USERNAME = "crmm4sg";//desarrollo
    public static final String PRIVATE_SERVER_FTP_PASSWORD = "CRM$0108";//DES2012#$

    public static final String PATH_PUBLIC_SERVER_ECERT_IMAGES = "/usr/java/apache-tomcat-5.5.25/webapps/files/certificates/images/ecert/"; // /usr/java/apache-tomcat-5.5.25/webapps/files/certificates/images/ecert/
    public static final String PATH_PUBLIC_SERVER_BROKER_IMAGES = "/opt/data/remote1/site/";// /usr/java/apache-tomcat-5.5.25/webapps/files/certificates/images/site/
    public static final String PATH_PUBLIC_SERVER_CERT_TYPE_IMAGES = "/opt/data/remote1/cert_type/";


    /*public static final String PATH_PRIVATE_SERVER_ECERT_IMAGES = "/Users/jhon/Documents/Basura/ecert/";// /usr/java/hydra/services/M4CService/
    public static final String PATH_PRIVATE_SERVER_BROKER_IMAGES = "/Users/jhon/Documents/Basura/site/";// /usr/java/apache-tomcat-6.0.29/webapps/ImagenesApps/Certificados/site/
    public static final String PATH_PRIVATE_SERVER_CERT_TYPE_IMAGES = "/opt/data/remote/cert_type/";*/

    public static final String PATH_PRIVATE_SERVER_ECERT_IMAGES = "/usr/java/hydra/services/M4CService/";// /usr/java/hydra/services/M4CService/
    public static final String PATH_PRIVATE_SERVER_BROKER_IMAGES = "/opt/data/remote/site/";// /usr/java/apache-tomcat-6.0.29/webapps/ImagenesApps/Certificados/site/
    public static final String PATH_PRIVATE_SERVER_CERT_TYPE_IMAGES = "/opt/data/remote/cert_type/";
    public static final String KEY_ENCRYPT_DECRYPT = "SYS73XRV";
    public static final String PERMISSION_DECRYPT_CREDIT_CARD = "VDesencriptar";
    public static final String PERMISSION_UPDATE_DELETE_PAYMENT = "ModificacionDatosPago";


    public static final String IP_SECURITY_SUNSET = "http://acuarius.it.sunset.com.mx:8080/";
    public static final String IP_PUBLIC_SUNSET = "http://bpo.m4sunset.com:8080";
    public static final String PATH_SECURITY_SUNSET_LOGIN = "GroupSunsetSecurityProxyServices/security/dameusuario";
    public static final String PATH_SECURITY_SUNSET_ROLES = "GroupSunsetSecurityProxyServices/security/getareasii";
    public static final Long[] callCentersIds = { 201l, 1541l,5962l,361l };


}
