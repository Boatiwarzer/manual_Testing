module ku.cs.testTools {
    // JavaFX Modules
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing; // For integrating Swing components if needed
    requires javafx.graphics;
    // Core Java Modules
    requires java.desktop; // For AWT/Swing components (if needed)
    requires static lombok; // Lombok annotations (compile-time only)

    // Jakarta and Hibernate ORM Modules
    requires jakarta.persistence; // For JPA annotations
    requires jakarta.inject; // For Dependency Injection
    requires jakarta.validation; // For validation annotations
    requires org.hibernate.orm.core; // Hibernate ORM library
    requires java.sql;

    // Additional Libraries
    requires org.apache.poi.ooxml; // For handling Microsoft Excel files
    requires org.controlsfx.controls; // ControlsFX library for UI components
    requires atlantafx.base; // Custom JavaFX theme library

    // Opening packages to specific modules
    opens ku.cs.testTools.Controllers.Home to javafx.fxml, org.hibernate.orm.core;
    opens ku.cs.testTools.Controllers.UseCase to javafx.fxml, org.hibernate.orm.core;
    opens ku.cs.testTools.Controllers.TestCase to javafx.fxml, org.hibernate.orm.core;
    opens ku.cs.testTools.Controllers.TestScript to javafx.fxml, org.hibernate.orm.core;
    opens ku.cs.testTools.Controllers.TestFlow to javafx.fxml, org.hibernate.orm.core;
    opens ku.cs.testTools.Controllers.TestResult to javafx.fxml, org.hibernate.orm.core;
    opens ku.cs.testTools.Controllers.Manager to javafx.fxml, org.hibernate.orm.core;
    opens ku.cs.testTools.Models.TestToolModels to javafx.base,jakarta.persistence, jakarta.validation, jakarta.inject, org.hibernate.orm.core, lombok;
    opens ku.cs.testTools.Models.Manager to  javafx.base,jakarta.persistence, jakarta.validation, jakarta.inject, org.hibernate.orm.core, lombok;
    //opens ku.cs.testTools to javafx.fxml, org.hibernate.orm.core;

    // Exporting packages for external use
    exports ku.cs.testTools.Controllers.Home;
    exports ku.cs.testTools.Controllers.UseCase;
    exports ku.cs.testTools.Controllers.TestCase;
    exports ku.cs.testTools.Controllers.TestScript;
    exports ku.cs.testTools.Controllers.TestFlow;
    exports ku.cs.testTools.Controllers.TestResult;
    exports ku.cs.testTools.Controllers.Manager;
    exports ku.cs.testTools;
}
