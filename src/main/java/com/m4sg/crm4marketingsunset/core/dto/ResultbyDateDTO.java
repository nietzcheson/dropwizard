package com.m4sg.crm4marketingsunset.core.dto;

/**
 * Created by desarrollo1 on 07/12/2015.
 */
public class ResultbyDateDTO {
    private String DATEGROUP;
    private Integer TOTAL;

    public String getDateGroup() {
        return DATEGROUP;
    }

    public Integer getTotal() {
        return TOTAL;
    }

    public void setTotal(Integer TOTAL) {
        this.TOTAL = TOTAL;
    }

    public void setDateGroup(String DATEGROUP) {
        this.DATEGROUP = DATEGROUP;
    }
}
