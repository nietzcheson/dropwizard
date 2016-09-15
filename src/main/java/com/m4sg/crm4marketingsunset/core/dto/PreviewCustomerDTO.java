package com.m4sg.crm4marketingsunset.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by Fernando on 09/04/2015.
 */
public class PreviewCustomerDTO {

    private String FIRSTNAME;
    private String CAMPAIGN;
    private Long IDBOOKING;
    private String NUMCERT;
    private String RESERVATIONGROUP;
    private Date FECHAVENTA;
    private Date DATEUPDATED;
    private Long IDCLIENTE;
    private Long IDCAMPANIA;
    private Long OFFERID;
    private Long TIPOCERT;
    private String MV_STATUS;
    private Long M_RESERVACION;
    private Long IDSTATUS;
    private Long IDSTATUSRESERVA;
    private String STATUS;
    private String LINECOLOR;
    private String ARCHIVO;


    private String IDUSER;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone="UTC" )
    public Date getFECHAVENTA() {
        return FECHAVENTA;
    }

    public void setFECHAVENTA(Date FECHAVENTA) {
        this.FECHAVENTA = FECHAVENTA;
    }
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone="UTC" )
    public Date getDATEUPDATED() {
        return DATEUPDATED;
    }

    public void setDATEUPDATED(Date DATEUPDATED) {
        this.DATEUPDATED = DATEUPDATED;
    }

    @JsonProperty("name")
    public String getFIRSTNAME() {
        return FIRSTNAME;
    }

    public void setFIRSTNAME(String FIRSTNAME) {
        this.FIRSTNAME = FIRSTNAME;
    }

    public String getCAMPAIGN() {
        return CAMPAIGN;
    }

    public void setCAMPAIGN(String CAMPAIGN) {
        this.CAMPAIGN = CAMPAIGN;
    }

    public Long getIDBOOKING() {
        return IDBOOKING;
    }

    public void setIDBOOKING(Long IDBOOKING) {
        this.IDBOOKING = IDBOOKING;
    }

    public String getNUMCERT() {
        return NUMCERT;
    }

    public void setNUMCERT(String NUMCERT) {
        this.NUMCERT = NUMCERT;
    }

    public Long getIDCLIENTE() {
        return IDCLIENTE;
    }

    public void setIDCLIENTE(Long IDCLIENTE) {
        this.IDCLIENTE = IDCLIENTE;
    }

    public String getIDUSER() {
        return IDUSER;
    }

    public void setIDUSER(String IDUSER) {
        this.IDUSER = IDUSER;
    }

    public Long getIDCAMPANIA() {
        return IDCAMPANIA;
    }

    public void setIDCAMPANIA(Long IDCAMPANIA) {
        this.IDCAMPANIA = IDCAMPANIA;
    }

    public Long getOFFERID() {
        return OFFERID;
    }

    public void setOFFER_ID(Long OFFERID) {
        this.OFFERID = OFFERID;
    }

    public Long getTIPOCERT() {
        return TIPOCERT;
    }

    public void setTIPOCERT(Long TIPOCERT) {
        this.TIPOCERT = TIPOCERT;
    }

    public String getRESERVATIONGROUP() {
        return RESERVATIONGROUP;
    }

    public void setRESERVATIONGROUP(String RESERVATIONGROUP) {
        this.RESERVATIONGROUP = RESERVATIONGROUP;
    }

    public void setOFFERID(Long OFFERID) {
        this.OFFERID = OFFERID;
    }
    public String getMV_STATUS() {
        return MV_STATUS;
    }

    public void setMV_STATUS(String MV_STATUS) {
        this.MV_STATUS = MV_STATUS;
    }

    public Long getM_RESERVACION() {
        return M_RESERVACION;
    }

    public void setM_RESERVACION(Long m_RESERVACION) {
        M_RESERVACION = m_RESERVACION;
    }

    public Long getIDSTATUS() {
        return IDSTATUS;
    }

    public void setIDSTATUS(Long IDSTATUS) {
        this.IDSTATUS = IDSTATUS;
    }

    public String getLINECOLOR() {
        return LINECOLOR;
    }

    public void setLINECOLOR(String LINECOLOR) {
        this.LINECOLOR = LINECOLOR;
    }

    public Long getIDSTATUSRESERVA() {
        return IDSTATUSRESERVA;
    }

    public void setIDSTATUSRESERVA(Long IDSTATUSRESERVA) {
        this.IDSTATUSRESERVA = IDSTATUSRESERVA;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getARCHIVO() {
        return ARCHIVO;
    }

    public void setARCHIVO(String ARCHIVO) {
        this.ARCHIVO = ARCHIVO;
    }
}
