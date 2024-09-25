package ku.cs.testTools.Services;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TableColumns  extends TableColumn {
    private TableColumn tableColumn;
    public TableColumns(final TableColumn tableColumn){
        this.tableColumn = tableColumn;
        this.tableColumn.setSortable(false);
        this.tableColumn.setReorderable(false);
    }
}
