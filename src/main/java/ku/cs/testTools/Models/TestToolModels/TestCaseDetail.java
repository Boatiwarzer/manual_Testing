package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "TestCase_Detail")
@NamedQuery(name = "find testcasesdetail by id", query = "Select t from TestCaseDetail t where t.idTCD = :id")
public class TestCaseDetail {
    @Id
    @Column(name = "id_tcd", nullable = false)
    private String idTCD;

    @Column(name = "test_no_tcd", nullable = false)
    private String testNo;

    @Column(name = "variable_tcd", nullable = false)
    private String variableTCD;

    @Column(name = "expected_tcd")
    private String expectedTCD;

    @Column(name = "date_tcd")
    private String dateTCD;

    @Column(name = "idTC")
    private String idTC;

    public TestCaseDetail(String idTCD, String testNo, String variableTCD, String expectedTCD, String dateTCD, String testCase) {
        this.idTCD = idTCD;
        this.testNo = testNo;
        this.variableTCD = variableTCD;
        this.expectedTCD = expectedTCD;
        this.dateTCD = dateTCD;
        this.idTC = testCase;
    }
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestCaseDetail that = (TestCaseDetail) o;
        return idTCD != null && idTCD.equals(that.idTCD);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(idTCD);
    }

    public boolean isId(String id) {
        return this.idTCD != null && this.idTCD.equals(id);
    }

    public String[] toArray() {
        return new String[]{
                this.getIdTCD(),
                this.getTestNo(),
                this.getVariableTCD(),
                this.getExpectedTCD(),
                this.getDateTCD()
        };
    }
}
