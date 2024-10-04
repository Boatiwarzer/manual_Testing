package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Data
@AllArgsConstructor
public class TestScriptDetail {
    @Id
    private String idTSD;
    private String testNo;
    private String steps;
    private String inputData;
    private String expected;
    @ManyToOne
    private TestScript testScript;
    private String idTS;
    private String dateTSD;

    public TestScriptDetail(String idTSD, String testNo, String steps, String inputData, String expected, String idTS, String dateTSD) {
        this.idTSD = idTSD;
        this.testNo = testNo;
        this.steps = steps;
        this.inputData = inputData;
        this.expected = expected;
        this.idTS = idTS;
        this.dateTSD = dateTSD;
    }

    public TestScriptDetail(String idTSD, String testNo, String steps, String inputData, String expected) {
        this.idTSD = idTSD;
        this.testNo = testNo;
        this.steps = steps;
        this.inputData = inputData;
        this.expected = expected;
    }

    public TestScriptDetail(String idTSD, String testNo, String steps, String inputData, String expected, TestScript testScript) {
        this.idTSD = idTSD;
        this.testNo = testNo;
        this.steps = steps;
        this.inputData = inputData;
        this.expected = expected;
        this.testScript = testScript;
    }

    public TestScriptDetail(String idTSD, String testNo, String steps, String inputData, String expected, String idTS) {
        this.idTSD = idTSD;
        this.testNo = testNo;
        this.steps = steps;
        this.inputData = inputData;
        this.expected = expected;
        this.idTS = idTS;
    }

    public TestScriptDetail(String idTSD, String testNo, String steps, String inputData, String expected, TestScript testScript, String idTS) {
        this.idTSD = idTSD;
        this.testNo = testNo;
        this.steps = steps;
        this.inputData = inputData;
        this.expected = expected;
        this.testScript = testScript;
        this.idTS = idTS;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestScriptDetail that = (TestScriptDetail) o;
        return getIdTSD() != null && Objects.equals(getIdTSD(), that.getIdTSD());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }

    public boolean isId(String id) {
        return this.idTSD.equals(id);
    }
}
