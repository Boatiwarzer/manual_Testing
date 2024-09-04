module ku.cs {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires javafx.swing;
    requires static lombok;
    requires jakarta.persistence;

    opens ku.cs.testTools.Controllers.testScript to javafx.fxml;

    exports ku.cs.testTools.Controllers.testScript;
    exports ku.cs;
}
