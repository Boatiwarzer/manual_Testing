package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.Entity;
import javafx.scene.layout.StackPane;
import ku.cs.testTools.Services.TestScriptComparable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.*;

@Data
public class TestScriptList {
    private ArrayList<TestScript> testScriptList;

    public TestScriptList() {
        testScriptList = new ArrayList<TestScript>();
    }

    public void addTestScript(TestScript testScript) {
        testScriptList.add(testScript);
    }
    public void clear() {
        testScriptList.clear();
    }
    public void clearTestScript(String ID) {
        testScriptList.removeIf(testScript -> testScript.getIdTS().equals(ID));
    }
    public TestScript findTSById(String id) {
        for (TestScript testScript : testScriptList) {
            if (testScript.isId(id) ) {
                return testScript;
            }
        }
        return null;
    }

    public TestScript findByTestScriptId(String id) {
        for (TestScript testScript : testScriptList) {
            if (testScript.getIdTS().equals(id)) {
                return testScript;
            }
        }
        return null;
    }

    public TestScript findTSByPosition(int id) {
        for (TestScript testScript : testScriptList) {
            if (testScript.getPosition() == id ) {
                return testScript;
            }
        }
        return null;
    }
    public void addOrUpdateTestScript(TestScript testScript) {
        boolean exists = false;

        // Iterate through the list to check for an existing item with the same ID or name
        for (int i = 0; i < testScriptList.size(); i++) {
            TestScript existing = testScriptList.get(i);

            if (existing.isId(testScript.getIdTS())) {
                // Update existing item
                testScriptList.set(i, testScript);
                exists = true;
                break;
            }
        }

        // If the item does not exist, add it to the list
        if (!exists) {
            testScriptList.add(testScript);
        }
    }

    public void deleteTestScript(TestScript testScript) {
        // Iterate through the list to find the item to delete
        for (int i = 0; i < testScriptList.size(); i++) {
            TestScript existing = testScriptList.get(i);
            if (existing.isId(testScript.getIdTS())) {
                // Remove the item from the list
                existing.setMarkedForDeletion(true);
                testScriptList.remove(i);
                break; // Exit after removing the first match
            }
        }
    }

    public void sort(Comparator<TestScript> cmp) {
        Collections.sort(testScriptList, cmp);
    }

    public TestScript findByPositionId(int positionId) {
        for (TestScript testScript : testScriptList) {
            if (testScript.getPosition() == positionId) {
                return testScript;
            }
        }
        return null;
    }

    public void deleteTestScriptByPositionID(int id) {
        boolean found = false;

        // Iterate through the list to find and remove the item with the matching position ID
        for (int i = 0; i < testScriptList.size(); i++) {
            TestScript existing = testScriptList.get(i);
            if (existing.getPosition() == id) {
                testScriptList.remove(i);
                found = true;
                break;
            }
        }

        // Log or handle the case where no matching item was found
        if (!found) {
            System.out.println("No TestScript found with position ID: " + id);
        }
    }

    public TestScript findByUseCaseID(String useCaseID) {
        for (TestScript script : this.testScriptList) { // this.testScripts คือรายการ TestScript ที่คุณเก็บอยู่
            if (script.getUseCase().equals(useCaseID)) { // เปรียบเทียบ UseCase ID
                return script;
            }
        }
        return null; // หากไม่มี TestScript ที่ตรงกับ UseCase ID
    }
    public List<TestScript> findAllByUseCaseID(String useCaseID) {
        List<TestScript> result = new ArrayList<>();
        for (TestScript script : this.testScriptList) {
            if (script.getUseCase().equals(useCaseID)) {
                result.add(script);
            }
        }
        return result;
    }



}
