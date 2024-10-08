package ku.cs.testTools.Services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

public class AutoCompleteTextField<T> extends TextField {
    private final ObservableList<T> data;
    private final ListView<T> listView;
    private final Popup popup;

    public AutoCompleteTextField(ObservableList<T> items) {
        super();
        this.data = items;
        this.listView = new ListView<>();
        this.popup = new Popup();

        // Configure the ListView
        listView.setMaxHeight(120); // Limit dropdown height
        popup.getContent().add(new VBox(listView));
        popup.setAutoHide(true);  // Auto hide popup when clicked outside

        // Event handling for the TextField
        this.setOnKeyReleased(this::handleKeyReleased);

        // Event handling for selecting an item from ListView
        listView.setOnMouseClicked(e -> {
            T selectedItem = listView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                setText(selectedItem.toString());
                popup.hide();
            }
        });

        // Hide the popup when the focus is lost
        this.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                popup.hide();
            }
        });

        // Hide the popup when ListView loses focus
        listView.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                popup.hide();
            }
        });
    }

    private void handleKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.DOWN && !listView.getItems().isEmpty()) {
            listView.requestFocus(); // Focus the list when pressing the down arrow
            listView.getSelectionModel().select(0); // Select the first item
            return;
        }

        String typedText = this.getText().toLowerCase();
        if (typedText.isEmpty()) {
            popup.hide();
            return;
        }

        // Filter the list based on the typed text
        ObservableList<T> filteredList = FXCollections.observableArrayList();
        for (T item : data) {
            if (item.toString().toLowerCase().contains(typedText)) {
                filteredList.add(item);
            }
        }

        if (!filteredList.isEmpty()) {
            listView.setItems(filteredList);
            if (!popup.isShowing()) {
                // Show the popup right below the TextField
                popup.show(this, this.localToScreen(0, this.getHeight()).getX(),
                        this.localToScreen(0, this.getHeight()).getY());
            }
        } else {
            popup.hide();
        }
    }
}
