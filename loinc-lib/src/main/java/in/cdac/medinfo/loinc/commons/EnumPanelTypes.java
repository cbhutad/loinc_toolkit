package in.cdac.medinfo.loinc.commons;

/**
 * @author cheenmayab
 * This class consist of Panel types defined in Loinc Standard
 *
 */

public enum EnumPanelTypes {

	PANEL("panel"), ORGANIZER("organizer"), CONVENIENCE_GROUP("convenience_group"), ALL("all");

	public final String panelType;

	private EnumPanelTypes(String panelType) {
		this.panelType = panelType;
	}

	public String panelType() {
		return this.panelType;
	}
}
