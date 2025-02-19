package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "test_result_detail")
@NamedQuery(name = "find testresultdetail by id", query = "Select t from TestResultDetail t where t.idTRD = :id")
public class TestResultDetail {
    @Id
    @Access(AccessType.FIELD)
    @Column(name = "idTRD")  // Add Column annotation for idTRD
    private String idTRD;

    @Column(name = "testNo")  // Add Column annotation for testNo
    private String testNo;

    @Column(name = "tsIdTRD")  // Add Column annotation for tsIdTRD
    private String tsIdTRD;

    @Column(name = "tcIdTRD")  // Add Column annotation for tcIdTRD
    private String tcIdTRD;

    @Column(name = "actorTRD")  // Add Column annotation for actorTRD
    private String actorTRD;

    @Column(name = "descriptTRD")  // Add Column annotation for descriptTRD
    private String descriptTRD;

    @Column(name = "inputdataTRD")  // Add Column annotation for inputdataTRD
    private String inputdataTRD;

    @Column(name = "stepsTRD")  // Add Column annotation for stepsTRD
    private String stepsTRD;

    @Column(name = "expectedTRD")  // Add Column annotation for expectedTRD
    private String expectedTRD;

    @Column(name = "actualTRD")  // Add Column annotation for actualTRD
    private String actualTRD;

    @Column(name = "statusTRD")  // Add Column annotation for statusTRD
    private String statusTRD;

    @Column(name = "priorityTRD")  // Add Column annotation for priorityTRD
    private String priorityTRD;

    @Column(name = "dateTRD")  // Add Column annotation for dateTRD
    private String dateTRD;

    @Column(name = "testerTRD")  // Add Column annotation for testerTRD
    private String testerTRD;

    @Column(name = "imageTRD")  // Add Column annotation for imageTRD
    private String imageTRD;

    @Column(name = "retestTRD")
    private String retestTRD;

    @Column(name = "approveTRD")  // Add Column annotation for approveTRD
    private String approveTRD;

    @Column(name = "remarkTRD")  // Add Column annotation for remarkTRD
    private String remarkTRD;

    @ManyToOne
    @JoinColumn(name = "testResult_id")  // Optional: Add a @JoinColumn annotation for clarity
    private TestResult testResult;

    @Column(name = "idTR")  // Add Column annotation for idTR
    private String idTR;

    public TestResultDetail(String idTRD, String testNo, String tsIdTRD, String tcIdTRD, String actorTRD, String descriptTRD, String inputdataTRD, String stepsTRD, String expectedTRD, String actualTRD, String statusTRD, String priorityTRD, String dateTRD, String testerTRD, String imageTRD, String retestTRD, String approveTRD, String remarkTRD, String idTR) {
        this.idTRD = idTRD;
        this.testNo = testNo;
        this.tsIdTRD = tsIdTRD;
        this.tcIdTRD = tcIdTRD;
        this.actorTRD = actorTRD;
        this.descriptTRD = descriptTRD;
        this.inputdataTRD = inputdataTRD;
        this.stepsTRD = stepsTRD;
        this.expectedTRD = expectedTRD;
        this.actualTRD = actualTRD;
        this.statusTRD = statusTRD;
        this.priorityTRD = priorityTRD;
        this.dateTRD = dateTRD;
        this.testerTRD = testerTRD;
        this.imageTRD = imageTRD;
        this.retestTRD = retestTRD;
        this.approveTRD = approveTRD;
        this.remarkTRD = remarkTRD;
        this.idTR = idTR;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestResultDetail that = (TestResultDetail) o;
        return getIdTRD() != null && Objects.equals(getIdTRD(), that.getIdTRD());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }

    public boolean isId(String id) {
        return this.idTRD.equals(id);
    }

    public boolean isIdTR(String id) {
        return this.idTR.equals(id);
    }
}
