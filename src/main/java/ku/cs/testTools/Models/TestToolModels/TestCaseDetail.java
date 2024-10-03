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
public class TestCaseDetail {
    @Id
    private String idTCD;
    private String testNo;
    private String nameTCD;
    private String variableTCD;
    private String dateTCD;

    @ManyToOne
    private TestCase testCase;
    private String idTC;
    public TestCaseDetail(String idTCD, String testNo, String nameTCD, String variableTCD, String dateTCD ,String idTC) {
        this.idTCD = idTCD;
        this.testNo = testNo;
        this.nameTCD = nameTCD;
        this.variableTCD = variableTCD;
        this.idTC = idTC;
        this.dateTCD = dateTCD;
    }

    public TestCaseDetail(String idTCD, String testNo, String nameTCD, String variableTCD, String idTC) {
        this.idTCD = idTCD;
        this.testNo = testNo;
        this.nameTCD = nameTCD;
        this.variableTCD = variableTCD;
        this.idTC = idTC;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestCaseDetail that = (TestCaseDetail) o;
        return getIdTCD() != null && Objects.equals(getIdTCD(), that.getIdTCD());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
    public boolean isId(String id) {
        return this.idTCD.equals(id);
    }
}
