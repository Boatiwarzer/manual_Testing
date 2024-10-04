package ku.cs.testTools.Services;

import javafx.scene.control.TableColumn;

public class TableColumns<T, S> extends TableColumn<T, S> {
    private TableColumn<T, S> tableColumn;

    // Constructor
    public TableColumns(final TableColumn<T, S> tableColumn) {
        this.tableColumn = tableColumn;

        // ปิดการจัดเรียงและการเลื่อนคอลัมน์
        this.tableColumn.setSortable(false);
        this.tableColumn.setReorderable(false);
    }

    // ฟังก์ชันสำหรับตั้งค่าคุณสมบัติอื่นๆ
    public void configureColumn() {
        // สามารถเพิ่มการตั้งค่าที่ต้องการเพิ่มเติมในที่นี้ เช่น ความกว้างของคอลัมน์ หรือรูปแบบการแสดงผล
        this.tableColumn.setPrefWidth(150);  // ตัวอย่างการตั้งค่าความกว้าง
    }
}
