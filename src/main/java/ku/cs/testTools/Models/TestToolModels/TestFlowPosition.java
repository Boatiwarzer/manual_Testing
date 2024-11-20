package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class TestFlowPosition {

    @Id
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
