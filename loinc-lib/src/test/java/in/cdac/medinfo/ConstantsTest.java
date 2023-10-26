package in.cdac.medinfo;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import in.cdac.medinfo.loinc.Constants;

public class ConstantsTest {

    @Test
    void constantValueTest() {
        assertTrue(Constants.LOINC.equals("Loinc.csv"));
        assertTrue(Constants.PANEL_AND_FORMS.equals("PanelsAndForms.csv"));
        assertTrue(Constants.PART.equals("Part.csv"));
    }
    
}
