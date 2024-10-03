package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class TestCase {
    @Id
    private String idTC;
    private String nameTC;
    private LocalDateTime dateTC;
    private String useCase;
    private String descriptionTC;
    private String note;
    @OneToMany(mappedBy = "testCase")
    private List<TestScriptDetail> testScriptCaseList;
    private String idTCDList;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestCase testCase = (TestCase) o;
        return getIdTC() != null && Objects.equals(getIdTC(), testCase.getIdTC());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
    @Override
    public String toString() {
        return idTC + " : " + nameTC;
    }

    public boolean isId(String id) {
        return this.idTC.equals(id);
    }
}
