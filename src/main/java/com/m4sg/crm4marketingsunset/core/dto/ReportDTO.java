package com.m4sg.crm4marketingsunset.core.dto;

import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.ValidateConstraint;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by desarrollo1 on 03/12/2015.
 */
public class ReportDTO extends ValidateConstraint {
    private String from;
    private String to;
    @NotNull
    @Size(min = 8)
    private String by;

    public ReportDTO() {
        this.from = null;
        this.to = null;
        this.by=null;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getBy() {
        return by;
    }

    public void setTo(String to){
        this.to=to;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public List<ErrorRepresentation> validate() {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        if(getTo()!=null || getFrom() !=null){
            Date today= new Date();
            if(getTo()==null)
                setTo(dt.format(today));
            if(getFrom()==null)
                setFrom(dt.format(today));
            //Sí la fecha de from es después que la de to.
            try {
                if (dt.parse(getFrom()).compareTo(dt.parse(getTo())) > 0)
                    validationMessages.add(new ErrorRepresentation("date", "No valid date"));
            }catch (Exception e){
                validationMessages.add(new ErrorRepresentation("date", "No valid date"));
            }

            if(getTo()!=null && getFrom() !=null){
                try {
                    if (isThisDateValid(getTo(), "yyyy-MM-dd"))
                        setTo(dt.format(dt.parse(getTo())));

                    if (isThisDateValid(getFrom(), "yyyy-MM-dd"))
                        setFrom(dt.format(dt.parse(getFrom())));
                }catch (Exception e){
                    validationMessages.add(new ErrorRepresentation("date", "No valid date"));
                }
            }
        }

        return validationMessages;
    }
}
