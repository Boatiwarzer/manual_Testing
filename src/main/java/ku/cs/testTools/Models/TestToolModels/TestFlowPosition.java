package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.*;
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
    private String type;

    public TestFlowPosition(int positionID, double xPosition, double yPosition, double fitWidth, double fitHeight, double rotation, String type) {
        this.positionID = positionID;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.fitWidth = fitWidth;
        this.fitHeight = fitHeight;
        this.rotation = rotation;
        this.type = type;
    }


    public boolean isId(int id) {
        return this.positionID == id;
    }
    public boolean isType(String type) {
        return this.type.equals(type);
    }

}
