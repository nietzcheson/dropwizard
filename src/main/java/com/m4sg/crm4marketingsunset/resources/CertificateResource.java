package com.m4sg.crm4marketingsunset.resources;

import com.codahale.metrics.annotation.Timed;
import com.m4c.model.entity.online.Certificate;
import com.m4sg.crm4marketingsunset.SimplePrincipal;
import com.m4sg.crm4marketingsunset.core.dto.*;
import com.m4sg.crm4marketingsunset.services.CampaignService;
import com.m4sg.crm4marketingsunset.services.CertificateService;
import com.m4sg.crm4marketingsunset.services.SaleService;
import com.m4sg.crm4marketingsunset.util.Utils;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Usuario on 23/03/2015.
 */

@Path("/v1/certificates")
@Produces(MediaType.APPLICATION_JSON)
public class CertificateResource extends GenericResource<Certificate>{
    private CertificateService certificateService=new CertificateService();
    private SaleService saleService=new SaleService();
    private CampaignService campaignService=new CampaignService();
    public CertificateResource(Validator validator) {
        super(validator);
    }

    @POST
    @Path("/")
    @UnitOfWork
    public Response getCertificates(SearchCertificateDTO searchCertificateDTO, @Auth SimplePrincipal isAuthenticated){
        List<Certificate> saleCertificateDTO= certificateService.getCertificates(searchCertificateDTO);
        return Response.ok(saleCertificateDTO).build();
    }

    @GET
    @Path("/{id}/getFolios")
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    @UnitOfWork
    public Response exportExcel(@PathParam("id") String id)throws Exception{
        XSSFWorkbook wb = new XSSFWorkbook();
        CreationHelper createHelper = wb.getCreationHelper();
        XSSFSheet sheet = wb.createSheet("certificates");


        List<Certificate> certificateList=new ArrayList<Certificate>();
        certificateList=certificateService.getCertificates(Long.parseLong(id));


        //Styles for titles
        XSSFCellStyle style;
        XSSFFont titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short)15);
        titleFont.setColor(new XSSFColor(new java.awt.Color(39, 51, 89)));
        titleFont.setBold(true);
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(titleFont);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBottomBorderColor(new XSSFColor(new java.awt.Color(39, 51, 89)));
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setRightBorderColor(new XSSFColor(new java.awt.Color(39, 51, 89)));
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setLeftBorderColor(new XSSFColor(new java.awt.Color(39, 51, 89)));

        //

        /*//the header row: centered text in 48pt font
        XSSFRow headerRow = sheet.createRow(0);
        headerRow.setHeightInPoints(15);
        XSSFCell titleCell = headerRow.createCell(0);
        titleCell.setCellValue("CertificateSerial");
        titleCell.setCellStyle(style);
        sheet.setColumnWidth(0, 20 * 256);
        XSSFCell cell2 = headerRow.createCell(1);
        cell2.setCellValue("Number");
        cell2.setCellStyle(style);*/
        //sheet.setColumnWidth(0,20*256);

        for (int i = 0; i < certificateList.size(); i++) {
            Certificate certificate =  certificateList.get(i);
            //System.out.println("ROW -->"+i);
            Row row = sheet.createRow(i+1);
// Create a cell and put a value in it.
            Integer tam=certificate.getCampaign().toString().length();
            Cell cell = row.createCell(0);
//            String[] certSplit=certificate.getNumber().split("-");
            Cell cell1 = row.createCell(1);
            cell.setCellValue(certificate.getCampaign().toString());
            cell1.setCellValue(certificate.getNumber());
        }
// Write the output to a file
        File file;
        file = new File("workbook.xlsx");
        FileOutputStream fileOut = new FileOutputStream(file);
        wb.write(fileOut);
        fileOut.close();

        return Response.ok(file).header("content-disposition","attachment; filename = certificates.xlsx").build();
    }
    @GET
    @Path("/{certificate}")
    @UnitOfWork
    public Response getCertificate(@PathParam("certificate") String certificateNumber, @Auth SimplePrincipal isAuthenticated){

        CertificateCampaignDTO certificateCampaignDTO=campaignService.validateCertificate(certificateNumber);

        return Response.ok(certificateCampaignDTO).build();
    }
    @POST
    @Path("/{queryParam}/detail")
    @Timed
    @UnitOfWork
    public Response getSale(@PathParam("queryParam") String id,@Auth SimplePrincipal isAuthenticated){
            CertificateCustomerDTO saleCertificateDTO1= saleService.getCertificate(id);
            return Response.ok(saleCertificateDTO1).build();
    }

    @GET
    @Path("/getFoliosbyActiveCampaigns")
    @UnitOfWork
    public Response getFoliosbyActiveCampaigns()throws Exception{
        List<CertificatesFreeDTO> certificatesFreeDTOList= campaignService.getFoliosbyActiveCampaigns();
        return Response.ok(certificatesFreeDTOList).build();
    }

    @GET
    @Path("/countFolios/{campaignId}")
    @UnitOfWork
    public Response countFolios(@PathParam("campaignId") Long campaignId,@Auth SimplePrincipal isAuthenticated)throws Exception{
        List<CountFoliosDTO> countFoliosDTOList= campaignService.countFolios(campaignId);
        return Response.ok(countFoliosDTOList).build();
    }

}
