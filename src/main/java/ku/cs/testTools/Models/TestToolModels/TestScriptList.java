package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.Entity;
import ku.cs.testTools.Models.UsecaseModels.Actor;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;

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
}
