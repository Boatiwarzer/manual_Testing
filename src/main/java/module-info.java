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


    exports ku.cs.testTools.Controllers.Home;
    exports ku.cs.testTools.Controllers.UseCase;
    exports ku.cs.testTools.Controllers.TestCase;
    exports ku.cs.testTools.Controllers.TestScript;
    exports ku.cs.testTools.Controllers.TestFlow;
    exports ku.cs.testTools.Controllers.TestResult;
    exports ku.cs.testTools.Controllers.usecasedesiner;
    exports ku.cs;
}
