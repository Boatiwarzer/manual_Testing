module ku.cs {
    // JavaFX Modules
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.swing; // For integrating Swing components if needed

    // Core Java Modules
    requires java.desktop; // For AWT/Swing components
    requires static lombok; // Lombok annotations

    // Jakarta and Hibernate ORM Modules
    requires jakarta.persistence; // For JPA annotations
   // requires jakarta.transaction; // For transaction management (optional)
   // requires jakarta.activation; // For handling MIME types (optional)
    requires jakarta.inject;
    requires jakarta.validation;
    //requires  jakarta.cdi;// For dependency injection
    requires org.hibernate.orm.core; // Hibernate ORM library

    // Additional Libraries
    requires modelmapper; // For object mapping
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
    opens ku.cs.testTools.Models.TestToolModels to jakarta.persistence,jakarta.validation,jakarta.inject, org.hibernate.orm.core;
    opens ku.cs.testTools.Models.Manager to jakarta.persistence,jakarta.validation,jakarta.inject, org.hibernate.orm.core;

    // Exporting packages for external use
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
