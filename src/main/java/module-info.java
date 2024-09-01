module ku.cs {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires javafx.swing;
    requires static lombok;
    requires jakarta.persistence;

    opens ku.cs.testTools.Controllers.Testscript to javafx.fxml;

    exports ku.cs.testTools.Controllers.Testscript;
    exports ku.cs;
}
