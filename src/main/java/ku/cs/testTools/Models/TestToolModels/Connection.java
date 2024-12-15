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
    private int connectionID;
    private double startX;
    private double startY;
    private double endX;
    private double endY;
    private String label;
    private String arrowHead;
    private String lineType;
    private String arrowTail;
    private String note;
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
