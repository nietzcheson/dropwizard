package com.m4sg.crm4marketingsunset.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Usuario on 17/04/2015.
 */
public class PreviewBrokerDTO {
    private String NOMBRE;
    private String IDUSER;
    private  Long IDCAMPANIA;
    private String CAMPANIA;

    @JsonProperty("name")
    public String getNOMBRE() {
        return NOMBRE;
    }

    public void setNOMBRE(String NOMBRE) {
        this.NOMBRE = NOMBRE;
    }

    @JsonProperty("user")
    public String getIDUSER() {
        return IDUSER;
    }

    public void setIDUSER(String IDUSER) {
        this.IDUSER = IDUSER;
    }



    @JsonProperty("campanaId")
    public Long getIDCAMPANIA() {
        return IDCAMPANIA;
    }

    public void setIDCAMPANIA(Long IDCAMPANIA) {
        this.IDCAMPANIA = IDCAMPANIA;
    }
    @JsonProperty("campanaName")
    public String getCAMPANIA() {
        return CAMPANIA;
    }

    public void setCAMPANIA(String CAMPANIA) {
        this.CAMPANIA = CAMPANIA;
    }
}
