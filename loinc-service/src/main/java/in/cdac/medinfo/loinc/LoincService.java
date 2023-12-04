package in.cdac.medinfo.loinc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.cdac.medinfo.loinc.commons.EnumClassTypes;
import in.cdac.medinfo.loinc.commons.EnumStatus;
import in.cdac.medinfo.loinc.exceptions.InternalServerException;
import in.cdac.medinfo.loinc.response.serv.model.Model;
import in.cdac.medinfo.loinc.response.serv.model.PanelModel;
import in.cdac.medinfo.loinc.response.serv.model.PartModel;
import in.cdac.medinfo.loinc.response.serv.model.SearchModel;

/**
 * @author Cheenmaya Bhutad
 * This class consists of service methods for all the APIs
 */

@Service
public class LoincService {
    
    @Autowired
    ElasticSearchClient loincServiceModel;

    public Model lookup(String loincNum) throws IOException {
        return loincServiceModel.lookup(loincNum);
    }

    public List<SearchModel> search(Boolean sortByRank, EnumClassTypes type, EnumStatus status, int limit, String className, String component, String exampleUnits, String panelType, String property, String method, String scale, String system, String term, String timing) {
        return loincServiceModel.search(sortByRank, type, status, limit, className, component, exampleUnits, panelType, property, method, scale, system, term, timing);
    }

    public List<PanelModel> expandPanel(String className, String panelType, String status) {
        return loincServiceModel.expandPanel(className, panelType, status);
    }

    public List<PartModel> classes(String text) throws IOException {
        return loincServiceModel.classes(text);
    }

    public List<PartModel> systems(String text) throws IOException {
        return loincServiceModel.systems(text);
    }

    public List<PartModel> scale(String text) throws IOException {
        return loincServiceModel.scale(text);
    }

    public List<PartModel> property(String text) throws IOException {
        return loincServiceModel.property(text);
    }

    public List<PartModel> timing(String text) throws IOException {
        return loincServiceModel.timing(text);
    }

    public List<PartModel> methods(String text) throws IOException {
        return loincServiceModel.methods(text);
    }

    public List<PartModel> components(String text) throws IOException {
        return loincServiceModel.component(text);
    }

    public List<String> exampleUnits() throws IOException {
        return loincServiceModel.exampleUnits();
    }

    public String getVersionLastChanged() throws IOException {
        return loincServiceModel.getVersionLastChanged();
    }

    /**
     * This method checks whether the CSV folder and specified loinc csv files exists or not
     */
    public void checkPathandFile(String directoryPath) {

        Path path = Paths.get(directoryPath);
        if(Files.exists(path) == false) {
            throw new InternalServerException("Specified Directory does not exist");
        }

        File folder = new File(path.toString());
        File[] listOfFiles = folder.listFiles();

        List<String> missingLoincFiles = new ArrayList<>();
        missingLoincFiles.add(Constants.LOINC);
        missingLoincFiles.add(Constants.PANEL_AND_FORMS);
        missingLoincFiles.add(Constants.PANEL_AND_FORMS);

        for(int i = 0; i < listOfFiles.length; i++) {
            if(listOfFiles[i].isFile()) {
                String fileName = listOfFiles[i].getName();
                if(missingLoincFiles.contains(fileName))
                    missingLoincFiles.remove(fileName);
            }
        }

        if(missingLoincFiles.size() > 0) {
            String missingFileString = String.join(", ", missingLoincFiles);
            throw new InternalServerException(missingFileString + " files not found.");
        }
    }

}
