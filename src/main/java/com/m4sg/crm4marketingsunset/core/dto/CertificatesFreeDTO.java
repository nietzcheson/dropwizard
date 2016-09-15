package com.m4sg.crm4marketingsunset.core.dto;

/**
 * Created by desarrollo1 on 30/03/2016.
 */
public class CertificatesFreeDTO {
    private Long CAMPANIA_ID;
    private String CAMPANIA_NAME;
    private Integer COUNTER;

    public Long getCAMPANIA_ID() {
        return CAMPANIA_ID;
    }

    public void setCAMPANIA_ID(Long CAMPANIA_ID) {
        this.CAMPANIA_ID = CAMPANIA_ID;
    }

    public String getCAMPANIA_NAME() {
        return CAMPANIA_NAME;
    }

    public void setCAMPANIA_NAME(String CAMPANIA_NAME) {
        this.CAMPANIA_NAME = CAMPANIA_NAME;
    }

    public Integer getCOUNTER() {
        return COUNTER;
    }

    public void setCOUNTER(Integer COUNTER) {
        this.COUNTER = COUNTER;
    }
}
