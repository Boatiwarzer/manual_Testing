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
@AllArgsConstructor
public class TestCase {
    @Id
    private String idTC;
    private String nameTC;
    private String dateTC;
    private String useCase;
    private String descriptionTC;
    private String note;
    @OneToMany(mappedBy = "testCase")
    private List<TestScriptDetail> testScriptCaseList;
    private String idTCDList;

    public TestCase(String idTC, String nameTC, String dateTC, String useCase, String descriptionTC, String note) {
        this.idTC = idTC;
        this.nameTC = nameTC;
        this.dateTC = dateTC;
        this.useCase = useCase;
        this.descriptionTC = descriptionTC;
        this.note = note;
    }

    public TestCase(String idTC, String nameTC, String dateTC, String useCase, String descriptionTC, String note, String idTCDList) {
        this.idTC = idTC;
        this.nameTC = nameTC;
        this.dateTC = dateTC;
        this.useCase = useCase;
        this.descriptionTC = descriptionTC;
        this.note = note;
        this.idTCDList = idTCDList;
    }

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
