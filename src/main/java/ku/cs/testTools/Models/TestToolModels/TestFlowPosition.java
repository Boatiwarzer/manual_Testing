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
@AllArgsConstructor
public class TestFlowPosition {

    @Id
    private int positionID;
    private double xPosition;
    private double yPosition;
    private double fitWidth;
    private double fitHeight;
    private double rotation;
}
