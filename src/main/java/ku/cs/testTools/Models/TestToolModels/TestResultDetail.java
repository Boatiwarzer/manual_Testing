package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.ArrayList;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Data
@AllArgsConstructor
public class TestResultDetail {
    @Id
    private String idTRD;
    private String testNo;
    private String tsIdTRD;
    private String actorTRD;
    private String descriptTRD;
    private String inputdataTRD;
    private String stepsTRD;
    private String expectedTRD;
    private String actualTRD;
    private String statusTRD;
    private String priorityTRD;
    private String dateTRD;
    private String testerTRD;
    private String imageTRD;
    private String approveTRD;
    private String remarkTRD;
    @ManyToOne
    private TestResult testResult;
    private String idTR;

    public TestResultDetail(String idTRD, String testNo, String tsIdTRD, String actorTRD, String descriptTRD, String inputdataTRD, String stepsTRD, String expectedTRD, String actualTRD,String statusTRD, String priorityTRD, String dateTRD, String testerTRD, String imageTRD, String approveTRD, String remarkTRD, String idTR) {
        this.idTRD = idTRD;
        this.testNo = testNo;
        this.tsIdTRD = tsIdTRD;
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
}
