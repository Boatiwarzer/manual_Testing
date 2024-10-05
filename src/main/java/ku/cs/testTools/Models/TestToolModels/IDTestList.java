package ku.cs.testTools.Models.TestToolModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
@Data
@AllArgsConstructor
public class IDTestList {
    private ArrayList<IDTest> idTestList;

    public IDTestList() {
        idTestList = new ArrayList<IDTest>();
    }

    public void addID(IDTest idTest) {
        idTestList.add(idTest);
    }
    public IDTest findTCById(String id) {
        for (IDTest idTest : idTestList) {
            if (idTest.isId(id) ) {
                return idTest;
            }
        }
        return null;
    }
}
