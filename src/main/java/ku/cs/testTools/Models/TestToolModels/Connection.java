package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "Connection")
@NamedQuery(name = "find Connection by id", query = "Select t from Connection t where t.ID = :id")
public class Connection {
    @Id
    @UuidGenerator
    @Access(AccessType.FIELD)
    @Column(name = "id", nullable = false, unique = true)
    private UUID ID;

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
    @Column(name = "projectName", nullable = false, precision = 10, scale = 2)
    private String projectName;
    @Column(name = "tester", nullable = false, precision = 10, scale = 2)
    private String tester;

    public Connection(int connectionID, double startX, double startY, double endX, double endY, String label, String arrowHead, String lineType, String arrowTail, String note, String type, String projectName, String tester) {
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
        this.projectName = projectName;
        this.tester = tester;
    }


    public boolean isId(int idDS) {
        return this.connectionID == idDS;
    }
}
