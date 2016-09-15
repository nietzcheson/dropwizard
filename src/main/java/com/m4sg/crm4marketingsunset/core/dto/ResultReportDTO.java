package com.m4sg.crm4marketingsunset.core.dto;

/**
 * Created by desarrollo1 on 04/12/2015.
 */
public class ResultReportDTO {
    private  Long total;
    private  Long done;
    private  Long nodone;

    public ResultReportDTO(){
        this.total=null;
        this.done=null;
        this.nodone=null;
    }

    public Long getTotal() {
        return total;
    }

    public Long getDone() {
        return done;
    }

    public Long getNodone() {
        return nodone;
    }

    public void setNodone(Long nodone) {
        this.nodone = nodone;
    }

    public void setDone(Long done) {
        this.done = done;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
