package ku.cs.testTools.Controllers.Home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ku.cs.fxrouter.FXRouter;

import java.io.IOException;

public class RoleController {

    @FXML
    private Button onManagerButton;

    @FXML
    private Button onTesterButton;

    @FXML
    void onManagerButton(ActionEvent event) {
        try {
            FXRouter.popup("landing_openproject",true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onTesterButton(ActionEvent event) {
        try {
            FXRouter.popup("landing_page",true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
