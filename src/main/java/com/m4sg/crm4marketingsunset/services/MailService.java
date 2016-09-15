package com.m4sg.crm4marketingsunset.services;

import com.m4sg.crm4marketingsunset.util.Constants;
import com.m4sg.crm4marketingsunset.util.FileIO;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.sunset.services.MailReader;
import com.sunset.services.MailSender;
import com.sunset.services.SessionMail;
import org.apache.commons.io.FileUtils;
import org.apache.commons.mail.EmailException;

import javax.activation.MimetypesFileTypeMap;
import javax.mail.MessagingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Usuario on 16/12/2015.
 */
public class MailService {

    public static void sendEmail(String toMail, String ccMail, String bccMail, String subjectMail, String bodyMail, String nameReport, String idBooking, Boolean ecert, Boolean leadburst) throws IOException {
        String host= FileIO.readProperties().getProperty("hostMail");
        String user= FileIO.readProperties().getProperty("userMail");
        String password= FileIO.readProperties().getProperty("passwordMail");


        MailSender mailSender=new MailSender( host,25,user,password);
        try {

            String[] listParq=toMail.split(",");
            for (int i = 0; i < listParq.length; i++) {
                String string = listParq[i];
                mailSender.getToLst().add(string);
            }
            String[] listCC=ccMail.split(",");
            for (int i = 0; i < listCC.length; i++) {
                String string = listCC[i];
                mailSender.getCcLst().add(string);
            }
            String[] listBCC=bccMail.split(",");
            for (int i = 0; i < listBCC.length; i++) {
                String string = listBCC[i];
                mailSender.getBccLst().add(string);
            }
            /*  Adjuntar Certificado */
            if(ecert){
                //Url para obtener el certificado
                String report= Constants.IP_PUBLIC_SUNSET+"/M4CApp/reportes/requestReportes.jsp?REPORTE="+nameReport+"&BookingNumber="+idBooking;
                URL url1 = new URL(report);
//                String tDir= FileIO.readProperties().getProperty("java.io.tmpdir");
                //Nombrando archivo
                String path = "certificate" + ".pdf";
                //Creación de archivo
                File file = new File(path);
                file.deleteOnExit();
                FileUtils.copyURLToFile(url1, file);
                InputStream targetStream = new FileInputStream(file);
                String content = new MimetypesFileTypeMap().getContentType(file);//match.getMimeType();//URLConnection.guessContentTypeFromStream(targetStream);//Files.probeContentType(path);
                //Adjuntar archivo
                mailSender.attachment(targetStream, file.getName(), content, file.getName());
            }

            String url="";
            if(leadburst){


                mailSender=addAttachImage(mailSender,"http://www.cancuncards.com/public/images/leadburst/certificate-thumb.jpg","certificate-thumb.jpg");

                mailSender=addAttachImage(mailSender,"http://www.cancuncards.com/public/images/leadburst/autoresponder/header-logo.jpg","header-logo.jpg");
                mailSender=addAttachImage(mailSender,"http://www.cancuncards.com/public/images/leadburst/autoresponder/header-bottom.jpg","header-bottom.jpg");
                mailSender=addAttachImage(mailSender,"http://www.cancuncards.com/public/images/leadburst/certificate-thumb.jpg","certificate-thumb.jpg");





                url="<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                        "<head>\n" +
                        "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                        "    <title></title>\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n" +
                        "\n" +
                        "    <style type=\"text/css\">\n" +
                        "    \t@import url(https://fonts.googleapis.com/css?family=Lato:400,300,300italic,400italic,700,700italic,900,900italic);\n" +
                        "    </style>\n" +
                        "    <link href='https://fonts.googleapis.com/css?family=Lato:400,300,300italic,400italic,700,700italic,900,900italic' rel='stylesheet' type='text/css'>\n" +
                        "\n" +
                        "    <style type=\"text/css\">\n" +
                        "    * { margin-top: 0px; margin-bottom: 0px; margin-left: 0px; margin-right: 0px; padding: 0px 0px; }\n" +
                        "    .ExternalClass * {line-height: 100%; margin-top: 0px; margin-bottom: 0px; margin-left: 0px; margin-right: 0px; padding: 0px 0px; }\n" +
                        "    #outlook a {padding:0;}\n" +
                        "    body{width:100% !important;} .ReadMsgBody{width:100%;} .ExternalClass{width:100%;}\n" +
                        "    body{-webkit-text-size-adjust:none; -ms-text-size-adjust:none;}\n" +
                        "    body, p, div, td {\n" +
                        "        font-family: sans-serif;\n" +
                        "        font-family: Tahoma, sans-serif;\n" +
                        "        font-family: 'Lato', sans-serif;\n" +
                        "    }\n" +
                        "    </style>\n" +
                        "\n" +
                        "    <!--[if !mso]><!-->\n" +
                        "    <style type=\"text/css\">\n" +
                        "    table, tr, td, p, font, .outlookFallback {font-family: Tahoma, sans-serif, 'Lato' !important;}\n" +
                        "    [style*=\"Lato\"] {\n" +
                        "        font-family: 'Lato', Tahoma, sans-serif !important;\n" +
                        "    }\n" +
                        "    </style>\n" +
                        "    <!--<![endif]-->\n" +
                        "\n" +
                        "</head>\n" +
                        "\n" +
                        "<body style=\"margin: 0; padding: 0; background-color: #e1e1e1;\">\n" +
                        "    <center>\n" +
                        "        <table width=\"700\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#e1e1e1\" style=\"margin: 0 auto 0 auto; width: 700px;\" align=\"center\">\n" +
                        "        <tr valign=\"top\">\n" +
                        "            <td width=\"700\" style=\"font-family: Tahoma, sans-serif, 'Lato'; font-size: 16px;\" align=\"center\" bgcolor=\"#e1e1e1\">\n" +
                        "                <table width=\"700\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                        "                <tr valign=\"top\">\n" +
                        "\t\t\t\t\t<td align=\"center\" height=\"131\" ><a href=\"http://cancunresortpromotion.com/\" target=\"_blank\">" +
                        "<img src=\"header-logo.jpg\" width=\"700\" height=\"131\" border=\"0\" style=\"border: 0px; display: block;\" /></a></td>\n" +
                        "                </tr>\n" +
                        "                <tr valign=\"top\">\n" +
                        "\t\t\t\t\t<td align=\"center\">\n" +
                        "                    \t<table width=\"700\" cellpadding=\"0\" cellspacing=\"20\" border=\"0\" bgcolor=\"019d9e\" style=\"background: #019d9e;\">\n" +
                        "                        <tr>\n" +
                        "                        \t<td align=\"center\"><p style=\"padding: 0px; Margin: 0px\" class=\"outlookFallback\"><font face=\"Tahoma, sans-serif, 'Lato'\" style=\"Margin: 0; padding: 0px; color: #ffffff; font-family: Tahoma, sans-serif, 'Lato'; font-size: 30px; font-weight: 700; line-height: 1.1em;\">"+subjectMail+"</font></p></td>\n" +
                        "                        </tr>\n" +
                        "                        </table>\n" +
                        "                    </td>\n" +
                        "                </tr>\n" +
                        "                <tr valign=\"top\">\n" +
                        "\t\t\t\t\t<td align=\"center\">\n" +
                        "                    \t<table width=\"700\" cellpadding=\"0\" cellspacing=\"20\" border=\"0\" bgcolor=\"dd1b0c\" style=\"background: #fff;\">\n" +
                        "                        <tr>\n" +
                        "                        \t<td align=\"center\">\n" +
                        "                            \t<p style=\"padding: 0px; Margin: 0px\" class=\"outlookFallback\"><font face=\"Tahoma, sans-serif, 'Lato'\" style=\"Margin: 0; padding: 0px; color: #555; font-family: Tahoma, sans-serif, 'Lato'; font-size: 20px; font-weight: 400; line-height: 1.5em;\"><b>Congratulations</b>, you have 12 months from today to book your Cancun vacation. To reserve your family vacation, just call the number on the certificate and start enjoying a relaxing week in paradise.</font></p>\n" +
                        "                                <br>\n" +
                        "                                <br>\n" +
                        "                                <hr>\n" +
                        "                                <br>\n" +
                        "                                <p style=\"padding: 0px; Margin: 0px\" class=\"outlookFallback\"><font face=\"Tahoma, sans-serif, 'Lato'\" style=\"Margin: 0; padding: 0px; color: #555; font-family: Tahoma, sans-serif, 'Lato'; font-size: 22px; font-weight: 700; line-height: 1.5em;\">Your certificate is attached.</font></p>\n" +
                        "                                <br>\n" +
                        "                                <div><img src=\"certificate-thumb.jpg\" alt=\"\" style=\"border: 0px;\" border=\"0\" width=\"420\" height=\"280\" ></div>\n" +
                        "                                <br><br>\n" +
                        "                                <p style=\"padding: 0px; Margin: 0px\" class=\"outlookFallback\"><font face=\"Tahoma, sans-serif, 'Lato'\" style=\"Margin: 0; padding: 0px; color: #555; font-family: Tahoma, sans-serif, 'Lato'; font-size: 14px; font-weight: 400; line-height: 1.5em;\">The attached certificate should look like the image above with your unique certificate ID number.</font></p>\n" +
                        "                                <br>\n" +
                        "                            </td>\n" +
                        "                        </tr>\n" +
                        "                        </table>\n" +
                        "                    </td>\n" +
                        "                </tr>\n" +
                        "                <tr valign=\"top\">\n" +
                        "\t\t\t\t\t<td align=\"center\" height=\"117\"><img src=\"header-bottom.jpg\" alt=\"\" width=\"700\" height=\"117\" border=\"0\" style=\"border: 0px; display: block;\" /></td>\n" +
                        "                </tr>\n" +
                        "                <tr valign=\"top\">\n" +
                        "\t\t\t\t\t<td align=\"center\">\n" +
                        "                    \t<table width=\"700\" cellpadding=\"0\" cellspacing=\"30\" border=\"0\" bgcolor=\"ffffff\" style=\"background: #ffffff;\">\n" +
                        "                        <tr>\n" +
                        "                        \t<td align=\"center\">\n" +
                        "                            \t<p style=\"padding: 0px; Margin: 0px\" class=\"outlookFallback\"><font face=\"Tahoma, sans-serif, 'Lato'\" style=\"Margin: 0; padding: 0px; color: #fda939; font-family: Tahoma, sans-serif, 'Lato'; font-size: 26px; font-weight: 900; line-height: 1.2em;\">Ready to book your vacation? <br><a href=\"tel:18008717915\" style=\"color: #fda939 !important;\">1-800-871-7915</a></font></p>\n" +
                        "                        \t</td>\n" +
                        "                        </tr>\n" +
                        "                        </table>\n" +
                        "                    </td>\n" +
                        "                </tr>\n" +
                        "                <tr valign=\"top\">\n" +
                        "\t\t\t\t\t<td align=\"center\">\n" +
                        "                    \t<table width=\"700\" cellpadding=\"0\" cellspacing=\"30\" border=\"0\" bgcolor=\"303030\" style=\"background: #303030;\">\n" +
                        "                        <tr>\n" +
                        "                        \t<td align=\"center\">\n" +
                        "                            \t<p style=\"padding: 0px; Margin: 0px\" class=\"outlookFallback\"><font face=\"Tahoma, sans-serif, 'Lato'\" style=\"Margin: 0; padding: 0px; color: #ffffff; font-family: Tahoma, sans-serif, 'Lato'; font-size: 14px; font-weight: 400; line-height: 1.2em;\">Cancun Discount Vacation | Copyright &copy; 2016 | All Rights Reserved.<br />Please do not respond to this email.</font></p>\n" +
                        "                        \t</td>\n" +
                        "                        </tr>\n" +
                        "                        </table>\n" +
                        "                    </td>\n" +
                        "                </tr>\n" +
                        "                </table>\n" +
                        "            </td>\n" +
                        "        </tr>\n" +
                        "        </table>\n" +
                        "    </center>\n" +
                        "</body>\n" +
                        "</html>";
            }else {
                url="<div style=\"background-color:#ececec;padding:0;margin:0 auto;font-weight:200;width:100%!important\">\n" +
                        "   <table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"table-layout:fixed;font-weight:200;font-family:Helvetica,Arial,sans-serif\" width=\"100%\">\n" +
                        "      <tbody>\n" +
                        "         <tr>\n" +
                        "            <td align=\"center\">\n" +
                        "               <center style=\"width:100%\">\n" +
                        "                  <table bgcolor=\"#FFFFFF\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"margin:0 auto;max-width:512px;font-weight:200;width:inherit;font-family:Helvetica,Arial,sans-serif\" width=\"512\">\n" +
                        "                     <tbody>\n" +
                        "                        <tr>\n" +
                        "                           <td bgcolor=\"#F3F3F3\" width=\"100%\" style=\"background-color:#f3f3f3;padding:12px;border-bottom:1px solid #ececec\">\n" +
                        "                              <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"font-weight:200;width:100%!important;font-family:Helvetica,Arial,sans-serif;min-width:100%!important\" width=\"100%\">\n" +
                        "                                 <tbody>\n" +
                        "                                    <tr>\n" +
                        //"                                       <td align=\"left\" valign=\"middle\"><a href=\"http://www.leadburst.com\" style=\"color:#4c4c4c;white-space:normal;display:inline-block;text-decoration:none\" target=\"_blank\"><img alt=\"LeadBurst\" border=\"0\" src=\"http://www.leadburst.com/img/LeadBurst_Email_Logo.png\" height=\"50\" width=\"150\" style=\"outline:none;color:#ffffff;display:block;text-decoration:none;font-size:12px;border-color:#ececec;border-width:1px;border-style:solid\" class=\"CToWUd\"></a></td>\n" +
                        "                                       <td valign=\"middle\" width=\"100%\" align=\"right\" style=\"padding:0 0 0 10px\"></td>\n" +
                        "                                       <td width=\"1\">&nbsp;</td>\n" +
                        "                                    </tr>\n" +
                        "                                 </tbody>\n" +
                        "                              </table>\n" +
                        "                           </td>\n" +
                        "                        </tr>\n" +
                        "                        <tr>\n" +
                        "                           <td align=\"left\">\n" +
                        "                              <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"font-weight:200;font-family:Helvetica,Arial,sans-serif\" width=\"100%\">\n" +
                        "                                 <tbody>\n" +
                        "                                    <tr>\n" +
                        "                                       <td width=\"100%\">\n" +
                        "                                          <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"font-weight:200;font-family:Helvetica,Arial,sans-serif\" width=\"100%\">\n" +
                        "                                             <tbody>\n" +
                        "                                                <tr>\n" +
                        "                                                   <td align=\"center\" bgcolor=\"#055395\" style=\"background-color:#055395;padding:20px 48px;color:#ffffff\">\n" +
                        "                                                      <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\">\n" +
                        "                                                         <tbody>\n" +
                        "                                                            <tr>\n" +
                        "                                                               <td align=\"center\" width=\"100%\">\n" +
                        "                                                                  <h1 style=\"padding:0;margin:0;color:#ffffff;font-weight:500;font-size:20px;line-height:24px\">"+subjectMail+"</h1>\n" +
                        "                                                               </td>\n" +
                        "                                                            </tr>\n" +
                        "                                                         </tbody>\n" +
                        "                                                      </table>\n" +
                        "                                                   </td>\n" +
                        "                                                </tr>\n" +
                        "                                                <tr>\n" +
                        "                                                   <td align=\"center\" style=\"padding:20px 0 32px 0; color:#999999;font-weight:200;font-family:Helvetica,Arial,sans-serif; font-size: 14px;\">\n" +
                        "                                                      "+bodyMail+" \n" +
                        "\n";
//                    if(ecert)
//                        url+=("<br/><br/>" +
//                                "<a style='padding: 10px; background: rgb(5, 83, 149); color: #fff;' href='http://bpo.m4sunset.com:8080/M4CApp/reportes/requestReportes.jsp?REPORTE="
//                                +nameReport+"&BookingNumber="+idBooking+"'>Click here to download your Cancun vacation certificate </a>");
                url+=("                                             </td>\n" +
                        "                                                </tr>\n" +
                        "                                             </tbody>\n" +
                        "                                          </table>\n" +
                        "                                       </td>\n" +
                        "                                    </tr>\n" +
                        "                                 </tbody>\n" +
                        "                              </table>\n" +
                        "                           </td>\n" +
                        "                        </tr>\n" +
                        "                     </tbody>\n" +
                        "                  </table>\n" +
                        "               </center>\n" +
                        "            </td>\n" +
                        "         </tr>\n" +
                        "      </tbody>\n" +
                        "   </table>\n" +
                        "   <img src=\"https://ci4.googleusercontent.com/proxy/REo3csxGL_UN3BKAz5S2W0djnkcxjBCd_sCAxiuoHEZd6HrPL9j6uF7nL6IcJ2KVsWKZv3JFCUEdArzy6b5eeEJ9gXdMoElpflU=s0-d-e1-ft#http://www.linkedin.com/emimp/6oz36c-ii93y572-7t.gif\" style=\"outline:none;color:#ffffff;display:block;text-decoration:none;width:1px;border-color:#ececec;border-width:1px;border-style:solid;min-height:1px\" class=\"CToWUd\">\n" +
                        "   <div class=\"yj6qo\"></div>\n" +
                        "   <div class=\"adL\"> </div>\n" +
                        "</div>");
            }


          bodyMail=url;
            System.out.println(bodyMail);
            try {
                mailSender.send(subjectMail,subjectMail,bodyMail);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }

    private static MailSender addAttachImage(MailSender mailSender,String url,String nameImage) throws IOException {

        String report= url;
        URL url1 = new URL(report);
//                String tDir= FileIO.readProperties().getProperty("java.io.tmpdir");
        //Nombrando archivo
        String path = nameImage;
        //Creación de archivo
        File file = new File(path);
        file.deleteOnExit();
        FileUtils.copyURLToFile(url1, file);
        InputStream targetStream = new FileInputStream(file);
        String content = new MimetypesFileTypeMap().getContentType(file);//match.getMimeType();//URLConnection.guessContentTypeFromStream(targetStream);//Files.probeContentType(path);
        //Adjuntar archivo
        mailSender.attachmentInline(targetStream, file.getName(), content, file.getName());
        return  mailSender;
    }

}
