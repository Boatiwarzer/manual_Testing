module ku.cs {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires javafx.swing;
    requires static lombok;
    requires jakarta.persistence;
    requires modelmapper;
    requires org.controlsfx.controls; // Add this line for ControlsFX
    requires org.hibernate.orm.core;
    requires org.apache.poi.ooxml;
    requires atlantafx.base;

    opens ku.cs.testTools.Controllers.Home to javafx.fxml, jakarta.persistence, lombok, org.controlsfx.controls;
    opens ku.cs.testTools.Controllers.UseCase to javafx.fxml, jakarta.persistence, lombok, org.controlsfx.controls;
    opens ku.cs.testTools.Controllers.TestCase to javafx.fxml, jakarta.persistence, lombok, org.controlsfx.controls;
    opens ku.cs.testTools.Controllers.TestScript to javafx.fxml, jakarta.persistence, lombok, org.controlsfx.controls;
    opens ku.cs.testTools.Controllers.TestFlow to javafx.fxml, jakarta.persistence, lombok, org.controlsfx.controls;
    opens ku.cs.testTools.Controllers.TestResult to javafx.fxml, jakarta.persistence, lombok, org.controlsfx.controls;
    opens ku.cs.testTools.Models.TestToolModels to jakarta.persistence, lombok, jakarta.transaction,jakarta.activation,jakarta.inject, org.hibernate.orm.core;
    opens ku.cs.testTools.Models.Manager to  jakarta.persistence, lombok, org.controlsfx.controls;

    exports ku.cs.testTools.Controllers.Home;
    exports ku.cs.testTools.Controllers.UseCase;
    exports ku.cs.testTools.Controllers.TestCase;
    exports ku.cs.testTools.Controllers.TestScript;
    exports ku.cs.testTools.Controllers.TestFlow;
    exports ku.cs.testTools.Controllers.TestResult;
    exports ku.cs;
    exports ku.cs.testTools.Models.TestToolModels;
    exports ku.cs.testTools.Models.Manager;



}

