package in.cdac.medinfo.loinc.response.serv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"LOINC_NUM", "LONG_COMMON_NAME"})
public class PanelModel {

    private String LOINC_NUM;
    private String LONG_COMMON_NAME;

    @JsonProperty("LOINC_NUM")
    public void setLOINC_NUM(String LOINC_NUM) {
        this.LOINC_NUM = LOINC_NUM;
    }

    @JsonProperty("LOINC_NUM")
    public String getLOINC_NUM() {
        return this.LOINC_NUM;
    }

    @JsonProperty("LONG_COMMON_NAME")
    public void setLONG_COMMON_NAME(String LONG_COMMON_NAME) {
        this.LONG_COMMON_NAME = LONG_COMMON_NAME;
    }

    @JsonProperty("LONG_COMMON_NAME")
    public String getLONG_COMMON_NAME() {
        return this.LONG_COMMON_NAME;
    }

}