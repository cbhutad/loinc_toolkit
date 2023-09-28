import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import in.cdac.medinfo.loinc.commons.*;

public class CommonsTest {

	private final StringToClassTypesConverter classTypeConverter = new StringToClassTypesConverter();	
	private final StringToPanelTypesConverter panelTypeConverter = new StringToPanelTypesConverter();
	private final StringToStatusConverter statusConverter = new StringToStatusConverter();

	@Test
	void classTypeConversionTest() {
		assertEquals(EnumClassTypes.LABORATORY, classTypeConverter.convert("laboratory"));
		assertEquals(EnumClassTypes.CLINICAL, classTypeConverter.convert("clinical"));
		assertEquals(EnumClassTypes.SURVEY, classTypeConverter.convert("survey"));
		assertEquals(EnumClassTypes.ATTACHMENT, classTypeConverter.convert("attachment"));
		assertEquals(EnumClassTypes.ALL, classTypeConverter.convert("all"));
	}
	@Test
	void panelTypeConversionTest() {
		assertEquals(EnumPanelTypes.PANEL, panelTypeConverter.convert("panel"));
		assertEquals(EnumPanelTypes.ORGANIZER, panelTypeConverter.convert("organizer"));
		assertEquals(EnumPanelTypes.CONVENIENCE_GROUP, panelTypeConverter.convert("convenience_group"));
		assertEquals(EnumPanelTypes.ALL, panelTypeConverter.convert("all"));
	}
	@Test
	void statusConversionTest() {
		assertEquals(EnumStatus.ACTIVE, statusConverter.convert("active"));
		assertEquals(EnumStatus.TRIAL, statusConverter.convert("trial"));
		assertEquals(EnumStatus.DISCOURAGED, statusConverter.convert("discouraged"));
		assertEquals(EnumStatus.DEPRECATED, statusConverter.convert("deprecated"));
		assertEquals(EnumStatus.ALL, statusConverter.convert("all"));
	}

}
