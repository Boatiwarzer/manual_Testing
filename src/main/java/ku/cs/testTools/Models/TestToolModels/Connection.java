package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "Decision")
@NamedQuery(name = "find Connection by id", query = "Select t from Connection t where t.connectionID = :id")
public class Connection {
    @Id
    @Access(AccessType.FIELD)
    @Column(name = "connection_id", nullable = false, unique = true) // กำหนดชื่อคอลัมน์ในฐานข้อมูล
    private int connectionID;

    @Column(name = "start_x", nullable = false)
    private double startX;

    @Column(name = "start_y", nullable = false)
    private double startY;

    @Column(name = "end_x", nullable = false)
    private double endX;

    @Column(name = "end_y", nullable = false)
    private double endY;

    @Column(name = "label", length = 255) // กำหนดความยาวสูงสุด
    private String label;

    @Column(name = "arrow_head", length = 50)
    private String arrowHead;

    @Column(name = "line_type", length = 50)
    private String lineType;

    @Column(name = "arrow_tail", length = 50)
    private String arrowTail;

    @Column(name = "note", length = 500)
    private String note;

    @Column(name = "type", length = 100)
    private String type;

    public Connection(int connectionID, double startX, double startY, double endX, double endY, String label, String arrowHead, String lineType, String arrowTail, String note, String type) {
        this.connectionID = connectionID;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.label = label;
        this.arrowHead = arrowHead;
        this.lineType = lineType;
        this.arrowTail = arrowTail;
        this.note = note;
        this.type = type;
    }

    public boolean isId(int idDS) {
        return this.connectionID == idDS;
    }
}
