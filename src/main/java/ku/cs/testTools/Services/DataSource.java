package ku.cs.testTools.Services;

import ku.cs.testTools.Models.TestToolModels.TestFlowPositionList;
import ku.cs.testTools.Models.TestToolModels.TestScriptDetailList;

public interface DataSource <T> {
    T readData();
    void writeData(T t);
}
