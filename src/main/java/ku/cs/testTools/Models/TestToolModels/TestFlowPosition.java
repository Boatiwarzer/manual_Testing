package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "test_flow_position")
@NamedQuery(name = "find testflowposition by id", query = "Select t from TestFlowPosition t where t.positionID = :id")
public class TestFlowPosition {

    @Id
    @Access(AccessType.FIELD)
    @Column(name = "position_id", nullable = false, unique = true)
    private int positionID;

    @Column(name = "fit_width", nullable = false, precision = 10, scale = 2)
    private double fitWidth;

    @Column(name = "fit_height", nullable = false, precision = 10, scale = 2)
    private double fitHeight;

    @Column(name = "x_position", nullable = false, precision = 10, scale = 2)
    private double xPosition;

    @Column(name = "y_position", nullable = false, precision = 10, scale = 2)
    private double yPosition;

    @Column(name = "rotation", nullable = false, precision = 10, scale = 2)
    private double rotation;

    @NotNull
    @Column(name = "type", nullable = false, length = 50)
    private String type;

    public TestFlowPosition(int positionID, double fitWidth, double fitHeight, double xPosition, double yPosition, double rotation, String type) {
        this.positionID = positionID;
        this.fitWidth = fitWidth;
        this.fitHeight = fitHeight;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.rotation = rotation;
        this.type = type;
    }

    public boolean isId(int id) {
        return this.positionID == id;
    }

    public boolean isType(String type) {
        return this.type != null && this.type.equals(type);
    }
}
