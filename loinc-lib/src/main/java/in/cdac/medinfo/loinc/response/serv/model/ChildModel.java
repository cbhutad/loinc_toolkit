package in.cdac.medinfo.loinc.response.serv.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"LOINC_NUMBER", "Name", "ObservationRequiredInPanel", "Example_UCUM_UNITS", "grandChildren"})
public class ChildModel {

    private String ObservationRequiredInPanel;
	private String Name;
	private String LOINC_NUMBER;
	private String Example_UCUM_UNITS;
	private List<ChildModel> grandChildren;
	
    @JsonProperty("grandChildren")
	public List<ChildModel> getGrandChildren() {
		return grandChildren;
	}

	@JsonProperty("grandChildren")
	public void setGrandChildren(List<ChildModel> grandChildren) {
		this.grandChildren = grandChildren;
	}

   @JsonProperty("ObservationRequiredInPanel")
	public String getObservationRequiredInPanel() {
		return ObservationRequiredInPanel;
	}

	@JsonProperty("ObservationRequiredInPanel")
	public void setObservationRequiredInPanel(String observationRequiredInPanel) {
		ObservationRequiredInPanel = observationRequiredInPanel;
	}

	@JsonProperty("Name")
	public String getName() {
		return Name;
	}

	@JsonProperty("Name")
	public void setName(String name) {
		Name = name;
	}

	@JsonProperty("LOINC_NUMBER")
	public String getLOINC() {
		return LOINC_NUMBER;
	}

	@JsonProperty("LOINC_NUMBER")
	public void setLOINC(String lOINC) {
		LOINC_NUMBER = lOINC;
	}

	@JsonProperty("Example_UCUM_UNITS")
	public String getExample_UCUM_UNITS() {
		return Example_UCUM_UNITS;
	}

	@JsonProperty("Example_UCUM_UNITS")
	public void setExample_UCUM_UNITS(String example_UCUM_UNITS) {
		Example_UCUM_UNITS = example_UCUM_UNITS;
	}

	public ChildModel() {
		super();
	}

	public ChildModel(String observationRequiredInPanel, String name, String lOINC_NUMBER, String example_UCUM_UNITS,
			List<ChildModel> grandChildren) {
		super();
		ObservationRequiredInPanel = observationRequiredInPanel;
		Name = name;
		LOINC_NUMBER = lOINC_NUMBER;
		Example_UCUM_UNITS = example_UCUM_UNITS;
		this.grandChildren = grandChildren;
	}
    
}
