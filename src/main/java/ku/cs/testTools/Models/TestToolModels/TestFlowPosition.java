package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "TestFlow_Position")
@NamedQuery(name = "find testflowposition by id", query = "Select t from TestFlowPosition t where t.ID = :id")
public class TestFlowPosition {
    @Id
    @UuidGenerator
    @GeneratedValue
    @Access(AccessType.FIELD)
    @Column(name = "id", nullable = false, unique = true)
    private UUID ID;

    @Column(name = "position_id", nullable = false)
    private int positionID;

    @Column(name = "fit_width", nullable = false, precision = 10)
    private double fitWidth;

    @Column(name = "fit_height", nullable = false, precision = 10)
    private double fitHeight;

    @Column(name = "x_position", nullable = false, precision = 10)
    private double xPosition;

    @Column(name = "y_position", nullable = false, precision = 10)
    private double yPosition;

    @Column(name = "rotation", nullable = false, precision = 10)
    private double rotation;

    @NotNull
    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Column(name = "projectName", nullable = false, precision = 10)
    private String projectName;
    @Column(name = "tester", nullable = false, precision = 10)
    private String tester;
    public TestFlowPosition(int positionID, double fitWidth, double fitHeight, double xPosition, double yPosition, double rotation, String type) {
        this.positionID = positionID;
        this.fitWidth = fitWidth;
        this.fitHeight = fitHeight;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.rotation = rotation;
        this.type = type;
    }

    public TestFlowPosition(int positionID, double fitWidth, double fitHeight, double xPosition, double yPosition, double rotation, String type, String projectName, String tester) {
        this.positionID = positionID;
        this.fitWidth = fitWidth;
        this.fitHeight = fitHeight;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.rotation = rotation;
        this.type = type;
        this.projectName = projectName;
        this.tester = tester;
    }

    public boolean isId(int id) {
        return this.positionID == id;
    }

    public boolean isType(String type) {
        return this.type != null && this.type.equals(type);
    }
}
