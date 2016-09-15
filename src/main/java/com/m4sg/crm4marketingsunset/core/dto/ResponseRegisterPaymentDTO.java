package com.m4sg.crm4marketingsunset.core.dto;

/**
 * Created by Usuario on 22/07/2015.
 */
public class ResponseRegisterPaymentDTO {
    private String status;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
