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
    private String roleTRD;
    private String descriptTRD;
    private String stepsTRD;
    private String expectedTRD;
    private String actualTRD;
    private String dateTRD;
    private String testerTRD;
    @ManyToOne
    private TestResult testResult;
    private String idTR;

    public TestResultDetail(String idTRD, String testNo, String tsIdTRD, String roleTRD, String descriptTRD, String stepsTRD, String expectedTRD, String actualTRD, String dateTRD, String testerTRD) {
        this.idTRD = idTRD;
        this.testNo = testNo;
        this.tsIdTRD = tsIdTRD;
        this.roleTRD = roleTRD;
        this.descriptTRD = descriptTRD;
        this.stepsTRD = stepsTRD;
        this.expectedTRD = expectedTRD;
        this.actualTRD = actualTRD;
        this.dateTRD = dateTRD;
        this.testerTRD = testerTRD;
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
