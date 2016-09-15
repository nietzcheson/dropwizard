package com.m4sg.crm4marketingsunset.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by Fernando on 09/04/2015.
 */
public class CustomerDataLongDTO {

    private String RN;
    private Long MV_IDBOOKING;
    private String M_NOMBRE;
    private String M_APELLIDO;
    private Long M_IDCLIENTE;
    private String NUMAUT;
    private String USERINIT;
    private Long RES;
    private String M_IDUSER;
    private String M_FECHA;
    private Long IDCAMPANIA;
    private String IDSTATUS;
    private String FECHA_WELCOME;


    //private String M_EMAIL;
   // private String M_ESTADO;

    //private String M_IDCALLCENTER;



   // private String CIUDAD;

  //  private String MV_CONSULTOR;
    //private Long MV_IDSTATUS;

    //private String STATUSVENTA;


    //private String ECERT;
   // private String VENTA_STATUS;




    public String getRN() {
        return RN;
    }

    public void setRN(String RN) {
        this.RN = RN;
    }

    public String getM_APELLIDO() {
        return M_APELLIDO;
    }

    public void setM_APELLIDO(String m_APELLIDO) {
        M_APELLIDO = m_APELLIDO;
    }

    /*public String getM_EMAIL() {
        return M_EMAIL;
    }

    public void setM_EMAIL(String m_EMAIL) {
        M_EMAIL = m_EMAIL;
    }
*/
   /* public String getM_ESTADO() {
        return M_ESTADO;
    }

    public void setM_ESTADO(String m_ESTADO) {
        M_ESTADO = m_ESTADO;
    }*/

    public String getM_FECHA() {
        return M_FECHA;
    }

    public void setM_FECHA(String m_FECHA) {
        M_FECHA = m_FECHA;
    }

   /* public String getM_IDCALLCENTER() {
        return M_IDCALLCENTER;
    }

    public void setM_IDCALLCENTER(String m_IDCALLCENTER) {
        M_IDCALLCENTER = m_IDCALLCENTER;
    }*/

    public Long getM_IDCLIENTE() {
        return M_IDCLIENTE;
    }

    public void setM_IDCLIENTE(Long m_IDCLIENTE) {
        M_IDCLIENTE = m_IDCLIENTE;
    }

    public String getM_IDUSER() {
        return M_IDUSER;
    }

    public void setM_IDUSER(String m_IDUSER) {
        M_IDUSER = m_IDUSER;
    }

    public String getM_NOMBRE() {
        return M_NOMBRE;
    }

    public void setM_NOMBRE(String m_NOMBRE) {
        M_NOMBRE = m_NOMBRE;
    }

   /* public String getCIUDAD() {
        return CIUDAD;
    }

    public void setCIUDAD(String CIUDAD) {
        this.CIUDAD = CIUDAD;
    }*/

    public Long getMV_IDBOOKING() {
        return MV_IDBOOKING;
    }

    public void setMV_IDBOOKING(Long MV_IDBOOKING) {
        this.MV_IDBOOKING = MV_IDBOOKING;
    }

    /*public String getMV_CONSULTOR() {
        return MV_CONSULTOR;
    }

    public void setMV_CONSULTOR(String MV_CONSULTOR) {
        this.MV_CONSULTOR = MV_CONSULTOR;
    }

    public Long getMV_IDSTATUS() {
        return MV_IDSTATUS;
    }

    public void setMV_IDSTATUS(Long MV_IDSTATUS) {
        this.MV_IDSTATUS = MV_IDSTATUS;
    }
*/
    public String getNUMAUT() {
        return NUMAUT;
    }

    public void setNUMAUT(String NUMAUT) {
        this.NUMAUT = NUMAUT;
    }



    public Long getRES() {
        return RES;
    }

    public void setRES(Long RES) {
        this.RES = RES;
    }

/*

    public String getECERT() {
        return ECERT;
    }

    public void setECERT(String ECERT) {
        this.ECERT = ECERT;
    }
*/



    public Long getIDCAMPANIA() {
        return IDCAMPANIA;
    }

    public void setIDCAMPANIA(Long IDCAMPANIA) {
        this.IDCAMPANIA = IDCAMPANIA;
    }



    public String getUSERINIT() {
        return USERINIT;
    }

    public void setUSERINIT(String USERINIT) {
        this.USERINIT = USERINIT;
    }
/*
    public String getVENTA_STATUS() {
        return VENTA_STATUS;
    }

    public void setVENTA_STATUS(String VENTA_STATUS) {
        this.VENTA_STATUS = VENTA_STATUS;
    }*/

    public String getIDSTATUS() {
        return IDSTATUS;
    }

    public void setIDSTATUS(String IDSTATUS) {
        this.IDSTATUS = IDSTATUS;
    }

    public String getFECHA_WELCOME() {
        return FECHA_WELCOME;
    }

    public void setFECHA_WELCOME(String FECHA_WELCOME) {
        this.FECHA_WELCOME = FECHA_WELCOME;
    }
/*
    public String getSTATUSVENTA() {
        return STATUSVENTA;
    }

    public void setSTATUSVENTA(String STATUSVENTA) {
        this.STATUSVENTA = STATUSVENTA;
    }*/


}
