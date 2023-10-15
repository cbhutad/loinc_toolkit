package in.cdac.medinfo.loinc.response.serv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/*
 * 
 * This class consists of all resposne parameters for all supporting apis.
 * 
 */


@JsonPropertyOrder({"LOINC_PART_NUMBER", "LOINC_PART_NAME", "LOINC_PART_DESCRIPTION", "STATUS"})
public class PartModel {

    private String LOINC_PART_NUMBER;
    private String LOINC_PART_NAME;
    private String LOINC_PART_DESCRIPTION;
    private String STATUS;

    @JsonProperty("LOINC_PART_NUMBER")
    public String getLOINC_PART_NUMBER() {
        return this.LOINC_PART_NUMBER;
    }

    @JsonProperty("LOINC_PART_NUMBER")
    public void setLOINC_PART_NUMBER(String loinc_PART_NUMBER) {
        this.LOINC_PART_NUMBER = loinc_PART_NUMBER;
    }

    @JsonProperty("LOINC_PART_NAME")
    public String getLOINC_PART_NAME() {
        return this.LOINC_PART_NAME;
    }

    @JsonProperty("LOINC_PART_NAME")
    public void setLOINC_PART_NAME(String LOINC_PART_NAME) {
        this.LOINC_PART_NAME = LOINC_PART_NAME;
    }

    @JsonProperty("LOINC_PART_DESCRIPTION")
    public String getLOINC_PART_DESCRIPTION() {
        return this.LOINC_PART_DESCRIPTION;
    }

    @JsonProperty("LOINC_PART_DESCRIPTION")
    public void setLOINC_PART_DESCRIPTION(String LOINC_PART_DESCRIPTION) {
        this.LOINC_PART_DESCRIPTION = LOINC_PART_DESCRIPTION;
    }

    @JsonProperty("STATUS")
    public String getSTATUS() {
        return this.STATUS;
    }

    @JsonProperty("STATUS")
    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

}
