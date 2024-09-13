module ku.cs {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires javafx.swing;
    requires static lombok;
    requires jakarta.persistence;


    opens ku.cs.testTools.Controllers.Home to javafx.fxml;
    opens ku.cs.testTools.Controllers.UseCase to javafx.fxml;
    opens ku.cs.testTools.Controllers.TestCase to javafx.fxml;
    opens ku.cs.testTools.Controllers.TestScript to javafx.fxml;
    opens ku.cs.testTools.Controllers.TestFlow to javafx.fxml;
    opens ku.cs.testTools.Controllers.TestResult to javafx.fxml;
    opens ku.cs.testTools.Controllers.usecasedesiner to javafx.fxml;
    opens ku.cs.testTools.Models.TestToolModels to javafx.base;
    opens ku.cs.testTools.Models.UsecaseModels to javafx.base;
    opens ku.cs.testTools.Models.Manager to javafx.base;

    exports ku.cs.testTools.Controllers.Home;
    exports ku.cs.testTools.Controllers.UseCase;
    exports ku.cs.testTools.Controllers.TestCase;
    exports ku.cs.testTools.Controllers.TestScript;
    exports ku.cs.testTools.Controllers.TestFlow;
    exports ku.cs.testTools.Controllers.TestResult;
    exports ku.cs.testTools.Controllers.usecasedesiner;
    exports ku.cs;
    exports ku.cs.testTools.Models.TestToolModels;
    exports ku.cs.testTools.Models.UsecaseModels;
    exports ku.cs.testTools.Models.Manager;



}
