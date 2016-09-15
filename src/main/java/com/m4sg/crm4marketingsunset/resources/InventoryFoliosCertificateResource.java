package com.m4sg.crm4marketingsunset.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Optional;
import com.hubspot.jackson.jaxrs.PropertyFiltering;
import com.m4c.model.entity.BatchCertificates;
import com.m4c.model.entity.Campaign;
import com.m4c.model.entity.online.Certificate;
import com.m4sg.crm4marketingsunset.core.dto.PaginationDTO;
import com.m4sg.crm4marketingsunset.services.CertificateService;
import com.m4sg.crm4marketingsunset.services.InventoryService;
import io.dropwizard.hibernate.UnitOfWork;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.beans.IntrospectionException;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhon on 12/06/15.
 */
@Path("/v1/inventory")
@Produces(MediaType.APPLICATION_JSON)
public class InventoryFoliosCertificateResource extends GenericResource<BatchCertificates> {
    private InventoryService service=new InventoryService();
    private CertificateService certificateService=new CertificateService();

    public InventoryFoliosCertificateResource(Validator validator) {
        super(validator);
    }

    @GET
    @UnitOfWork
    @PropertyFiltering
    public Response list(@DefaultValue("") @QueryParam("q") String search,
                         @DefaultValue("10") @QueryParam("pageLength") String pageLength,
                         @DefaultValue("") @QueryParam("orderBy") String orderBy,
                         @DefaultValue("ASC") @QueryParam("order") String order,
                         @DefaultValue("1")@QueryParam("page") String  page)
            throws JsonProcessingException, URISyntaxException, IntrospectionException {

        PaginationDTO<BatchCertificates> paginationDTO=service.list(orderBy, order, Integer.parseInt(pageLength), Integer.parseInt(page));
         return Response.ok(paginationDTO).build();
    }

    @GET
    @UnitOfWork
    @PropertyFiltering
    @Path("/{id}")
    public Response get(@PathParam("id") long id)
            throws JsonProcessingException, URISyntaxException, IntrospectionException {

        Optional<BatchCertificates> batchCertificatesOptional=service.findById(id);
        BatchCertificates batchCertificate = findSafely(batchCertificatesOptional, Campaign.TAG);

        return Response.ok(batchCertificate).build();
    }
    @GET
    @Path("/{id}/export")
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    @UnitOfWork
    public Response exportExcel(@PathParam("id") String id)throws Exception{
        XSSFWorkbook wb = new XSSFWorkbook();
        CreationHelper createHelper = wb.getCreationHelper();
        XSSFSheet sheet = wb.createSheet("certificates");


        List<Certificate> certificateList=new ArrayList<Certificate>();
        certificateList=certificateService.findByLote(Long.parseLong(id));


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


        for (int i = 0; i < certificateList.size(); i++) {
            Certificate certificate =  certificateList.get(i);
            Row row = sheet.createRow(i);
//          Create a cell and put a value in it.
//          Integer tam=certificate.getCampaign().toString().length();
            Cell cell = row.createCell(0);
//            String[] certSplit=certificate.getNumber().split("-");
//            Cell cell1 = row.createCell(1);
            cell.setCellValue(certificate.getNumber());
//            cell1.setCellValue(certificate.getNumber());
        }
// Write the output to a file
        File file;
        file = new File("workbook.xlsx");
        FileOutputStream fileOut = new FileOutputStream(file);
        wb.write(fileOut);
        fileOut.close();

        return Response.ok(file).header("content-disposition","attachment; filename = certificates.xlsx").build();
    }

}
