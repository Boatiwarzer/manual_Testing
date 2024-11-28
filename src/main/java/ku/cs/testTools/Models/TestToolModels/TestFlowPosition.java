package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
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
    private int positionID;
    private double xPosition;
    private double yPosition;
    private double fitWidth;
    private double fitHeight;
    private double rotation;

    public TestFlowPosition(int positionID, double fitWidth, double fitHeight, double xPosition, double yPosition,  double rotation) {
        this.positionID = positionID;
        this.fitWidth = fitWidth;
        this.fitHeight = fitHeight;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.rotation = rotation;
    }
    public boolean isId(int id) {
        return this.positionID == id;
    }
}
