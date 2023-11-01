package in.cdac.medinfo.loinc.response.serv.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "LOINC_NUMBER", "COMPONENT", "PROPERTY", "TIME_ASPCT", "SYSTEM", "SCALE_TYP", "METHOD_TYP",
"LONG_COMMON_NAME", "ShortName", "DisplayName", "STATUS", "CLASS", "CLASSTYPE", "ExampleUnits", "ORDER_OBS",
"VersionLastChanged", "VersionFirstReleased", "RelatedNames", "Children" })
public class Model {
    
    private String LOINC_NUMBER;
	private String COMPONENT;
	private String PROPERTY;
	private String TIME_ASPCT;
	private String SYSTEM;
	private String SCALE_TYP;
	private String METHOD_TYP;
	private String LONG_COMMON_NAME;
	private String ShortName;
	private String DisplayName;
	private String STATUS;
	private String CLASS;
	private String CLASSTYPE;
	private String ExampleUnits;
	private String ORDER_OBS;
	private String VersionLastChanged;
	private String VersionFirstReleased;
	private String RelatedNames;
	private List<ChildModel> children;

    @JsonProperty("LOINC_NUMBER")
	public String getLOINC() {
		return LOINC_NUMBER;
	}

	@JsonProperty("LOINC_NUMBER")
	public void setLOINC(String lOINC) {
		LOINC_NUMBER = lOINC;
	}

	@JsonProperty("LONG_COMMON_NAME")
	public String getLONG_COMMON_NAME() {
		return LONG_COMMON_NAME;
	}

	@JsonProperty("LONG_COMMON_NAME")
	public void setLONG_COMMON_NAME(String lONG_COMMON_NAME) {
		LONG_COMMON_NAME = lONG_COMMON_NAME;
	}

	@JsonProperty("ShortName")
	public String getShortName() {
		return ShortName;
	}

	@JsonProperty("ShortName")
	public void setShortName(String shortName) {
		ShortName = shortName;
	}

	@JsonProperty("COMPONENT")
	public String getCOMPONENT() {
		return COMPONENT;
	}

	@JsonProperty("COMPONENT")
	public void setCOMPONENT(String cOMPONENT) {
		COMPONENT = cOMPONENT;
	}

	@JsonProperty("METHOD_TYP")
	public String getMETHOD_TYP() {
		return METHOD_TYP;
	}

	@JsonProperty("METHOD_TYP")
	public void setMETHOD_TYP(String mETHOD_TYP) {
		METHOD_TYP = mETHOD_TYP;
	}

	@JsonProperty("SYSTEM")
	public String getSYSTEM() {
		return SYSTEM;
	}

	@JsonProperty("SYSTEM")
	public void setSYSTEM(String sYSTEM) {
		SYSTEM = sYSTEM;
	}

	@JsonProperty("PROPERTY")
	public String getPROPERTY() {
		return PROPERTY;
	}

	@JsonProperty("PROPERTY")
	public void setPROPERTY(String pROPERTY) {
		PROPERTY = pROPERTY;
	}

	@JsonProperty("TIME_ASPCT")
	public String getTIME_ASPCT() {
		return TIME_ASPCT;
	}

	@JsonProperty("TIME_ASPCT")
	public void setTIME_ASPCT(String tIME_ASPCT) {
		TIME_ASPCT = tIME_ASPCT;
	}

	@JsonProperty("SCALE_TYP")
	public String getSCALE_TYP() {
		return SCALE_TYP;
	}

	@JsonProperty("SCALE_TYP")
	public void setSCALE_TYP(String sCALE_TYP) {
		SCALE_TYP = sCALE_TYP;
	}

	@JsonProperty("ORDER_OBS")
	public String getORDER_OBS() {
		return ORDER_OBS;
	}

	@JsonProperty("ORDER_OBS")
	public void setORDER_OBS(String oRDER_OBS) {
		ORDER_OBS = oRDER_OBS;
	}

	@JsonProperty("DisplayName")
	public String getDisplayName() {
		return DisplayName;
	}

	@JsonProperty("DisplayName")
	public void setDisplayName(String displayName) {
		DisplayName = displayName;
	}

	@JsonProperty("STATUS")
	public String getSTATUS() {
		return STATUS;
	}

	@JsonProperty("STATUS")
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}

	@JsonProperty("CLASS")
	public String getCLASS() {
		return CLASS;
	}

	@JsonProperty("CLASS")
	public void setCLASS(String cLASS) {
		CLASS = cLASS;
	}

	@JsonProperty("CLASSTYPE")
	public String getCLASSTYPE() {
		return CLASSTYPE;
	}

	@JsonProperty("CLASSTYPE")
	public void setCLASSTYPE(String cLASSTYPE) {
		CLASSTYPE = cLASSTYPE;
	}

	@JsonProperty("VersionLastChanged")
	public String getVersionLastChanged() {
		return VersionLastChanged;
	}

	@JsonProperty("VersionLastChanged")
	public void setVersionLastChanged(String versionLastChanged) {
		VersionLastChanged = versionLastChanged;
	}

	@JsonProperty("VersionFirstReleased")
	public String getVersionFirstReleased() {
		return VersionFirstReleased;
	}

	@JsonProperty("VersionFirstReleased")
	public void setVersionFirstReleased(String versionFirstReleased) {
		VersionFirstReleased = versionFirstReleased;
	}
	
	@JsonProperty("ExampleUnits")
	public String getExampleUnits() {
		return ExampleUnits;
	}
    
	@JsonProperty("ExampleUnits")
	public void setExampleUnits(String exampleUnits) {
		this.ExampleUnits = exampleUnits;
	}

	@JsonProperty("Children")
	public List<ChildModel> getChildren() {
		return children;
	}

	@JsonProperty("Children")
	public void setChildren(List<ChildModel> children) {
		this.children = children;
	}

	@JsonProperty("RelatedNames")
	public String getRelatedNames() {
		return RelatedNames;
	}

	@JsonProperty("RelatedNames")
	public void setRelatedNames(String relatedName) {
		this.RelatedNames = relatedName;
	}

    /**
     * @param lOINC_NUMBER
	 * @param cOMPONENT
	 * @param pROPERTY
	 * @param tIME_ASPCT
	 * @param sYSTEM
	 * @param sCALE_TYP
	 * @param mETHOD_TYP
	 * @param lONG_COMMON_NAME
	 * @param shortName
	 * @param displayName
	 * @param sTATUS
	 * @param cLASS
	 * @param cLASSTYPE
	 * @param exampleUnits
	 * @param oRDER_OBS
	 * @param versionLastChanged
	 * @param versionFirstReleased
	 * @param relatedNames
     * @param children
     */
    public Model(String lOINC_NUMBER, String lONG_COMMON_NAME, String shortName,String cOMPONENT, String mETHOD_TYP,
    String sYSTEM, String pROPERTY, String tIME_ASPCT, String sCALE_TYP, String oRDER_OBS, String displayName,
    String sTATUS, String cLASS, String cLASSTYPE, String versionLastChanged, String versionFirstReleased,
    String exampleUnits, String relatedName, List<ChildModel> children) {
        super();
        LOINC_NUMBER = lOINC_NUMBER;
		LONG_COMMON_NAME = lONG_COMMON_NAME;
		ShortName = shortName;
		COMPONENT = cOMPONENT;
		METHOD_TYP = mETHOD_TYP;
		SYSTEM = sYSTEM;
		PROPERTY = pROPERTY;
		TIME_ASPCT = tIME_ASPCT;
		SCALE_TYP = sCALE_TYP;
		ORDER_OBS = oRDER_OBS;
		DisplayName = displayName;
		STATUS = sTATUS;
		CLASS = cLASS;
		CLASSTYPE = cLASSTYPE;
		VersionLastChanged = versionLastChanged;
		VersionFirstReleased = versionFirstReleased;
		RelatedNames = relatedName;
		ExampleUnits = exampleUnits;
		this.children = children;
    }

    public Model() {
        super();
    }
}
