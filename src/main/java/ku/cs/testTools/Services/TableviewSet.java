package ku.cs.testTools.Services;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TableviewSet<T> extends TableView<T> {
    private TableView tableView;
    public TableviewSet(final TableView tableView) {
        this.tableView = tableView;
        // สามารถคัดลอกการตั้งค่าหรือข้อมูลจาก existingTable ได้ที่นี่
        this.tableView.setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
        this.tableView.getColumns().clear();
        this.tableView.getItems().clear();
        this.tableView.refresh();
    }
}
