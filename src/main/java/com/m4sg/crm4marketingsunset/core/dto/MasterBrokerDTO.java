package com.m4sg.crm4marketingsunset.core.dto;

/**
 * Created by Fernando on 08/07/2015.
 */
public class MasterBrokerDTO {

    private long ID;
    private String COMPANY;
    private String EMAIL;
    private String USERNAME;
    private String RESPONSABLENAME;
    private String CAMPAIGN;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getCOMPANY() {
        return COMPANY;
    }

    public void setCOMPANY(String COMPANY) {
        this.COMPANY = COMPANY;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getRESPONSABLENAME() {
        return RESPONSABLENAME;
    }

    public void setRESPONSABLENAME(String RESPONSABLENAME) {
        this.RESPONSABLENAME = RESPONSABLENAME;
    }

    public String getCAMPAIGN() {
        if(CAMPAIGN!=null && !CAMPAIGN.isEmpty()){
            CAMPAIGN=CAMPAIGN.substring(0,CAMPAIGN.length()-1);
        }
        return CAMPAIGN;
    }

    public void setCAMPAIGN(String CAMPAIGN) {
        this.CAMPAIGN = CAMPAIGN;
    }
}
