package com.m4sg.crm4marketingsunset.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by desarrollo1 on 06/04/2016.
 */
public class CampaignPackageDTO {
    private Long ID;
    private String NOMBRE;
    private String CAMPANIA;
    private Long IDCAMPANIA;
    private Double IMPORTE;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getNOMBRE() {
        return NOMBRE;
    }

    public void setNOMBRE(String NOMBRE) {
        this.NOMBRE = NOMBRE;
    }

    public String getCAMPANIA() {
        return CAMPANIA;
    }

    public void setCAMPANIA(String CAMPANIA) {
        this.CAMPANIA = CAMPANIA;
    }

    public Long getIDCAMPANIA() {
        return IDCAMPANIA;
    }

    public void setIDCAMPANIA(Long IDCAMPANIA) {
        this.IDCAMPANIA = IDCAMPANIA;
    }

    public Double getIMPORTE() {
        return IMPORTE;
    }

    public void setIMPORTE(Double IMPORTE) {
        this.IMPORTE = IMPORTE;
    }
}
