package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "TestResult_Detail")
@NamedQuery(name = "find testresultdetail by id", query = "Select t from TestResultDetail t where t.idTRD = :id")
public class TestResultDetail {
    @Id
    @Column(name = "id_trd")  // Add Column annotation for idTRD
    private String idTRD;

    @Column(name = "test_no_trd")  // Add Column annotation for testNo
    private String testNo;

    @Column(name = "ts_id_trd")  // Add Column annotation for tsIdTRD
    private String tsIdTRD;

    @Column(name = "tc_id_trd")  // Add Column annotation for tcIdTRD
    private String tcIdTRD;

    @Column(name = "actor_trd")  // Add Column annotation for actorTRD
    private String actorTRD;

    @Column(name = "description_trd", length = 2000)  // Add Column annotation for descriptTRD
    private String descriptTRD;

    @Column(name = "inputdata_trd", length = 2000)  // Add Column annotation for inputdataTRD
    private String inputdataTRD;

    @Column(name = "steps_trd", length = 2000)  // Add Column annotation for stepsTRD
    private String stepsTRD;

    @Column(name = "expected_trd", length = 2000)  // Add Column annotation for expectedTRD
    private String expectedTRD;

    @Column(name = "actual_trd", length = 2000)  // Add Column annotation for actualTRD
    private String actualTRD;

    @Column(name = "status_trd")  // Add Column annotation for statusTRD
    private String statusTRD;

    @Column(name = "priority_trd")  // Add Column annotation for priorityTRD
    private String priorityTRD;

    @Column(name = "date_trd")  // Add Column annotation for dateTRD
    private String dateTRD;

    @Column(name = "tester_trd")  // Add Column annotation for testerTRD
    private String testerTRD;

    @Column(name = "image_trd", length = 2000)
//    @Lob
    private String imageTRD;

    @Column(name = "retest_trd")
    private String retestTRD;

    @Column(name = "approve_trd")  // Add Column annotation for approveTRD
    private String approveTRD;

    @Column(name = "remark_trd")  // Add Column annotation for remarkTRD
    private String remarkTRD;

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

    public String[] toArray() {
        return new String[]{
                this.getIdTRD(),
                this.getTestNo(),
                this.getTsIdTRD(),
                this.getTcIdTRD(),
                this.getActorTRD(),
                this.getDescriptTRD(),
                this.getInputdataTRD(),
                this.getStepsTRD(),
                this.getExpectedTRD(),
                this.getActualTRD(),
                this.getStatusTRD(),
                this.getPriorityTRD(),
                this.getDateTRD(),
                this.getTesterTRD(),
                this.getImageTRD(),
                this.getRetestTRD(),
                this.getApproveTRD(),
                this.getRemarkTRD()
        };
    }
}
