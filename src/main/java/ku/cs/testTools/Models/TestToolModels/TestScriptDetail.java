package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class TestScriptDetail {
    @Id
    private String idTSD;
    private String steps;
    private String inputData;
    private String expected;

    public TestScriptDetail(String idTSD, String steps, String inputData, String expected) {
        this.idTSD = idTSD;
        this.steps = steps;
        this.inputData = inputData;
        this.expected = expected;
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
}
