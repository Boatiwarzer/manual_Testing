package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "test_cases")
@NamedQuery(name = "find testcase by id", query = "Select t from TestCase t where t.idTC = :id")
public class TestCase {
    @Id
    @Access(AccessType.FIELD)
    private String idTC;
    private String nameTC;
    private String dateTC;
    private String useCase;
    private String descriptionTC;
    private String note;
    @OneToMany(mappedBy = "testCase")
    private List<TestCaseDetail> testCaseList;
    private String idTCDList;
    private int position;
    @Column(name = "pre_con") // Maps to `precon` in the database
    private String preCon;

    @Column(name = "post_con") // Maps to `postcon` in the database
    private String postCon;

    public TestCase(String idTC, String nameTC, String dateTC, String useCase, String descriptionTC, String note, int position, String preCon, String postCon) {
        this.idTC = idTC;
        this.nameTC = nameTC;
        this.dateTC = dateTC;
        this.useCase = useCase;
        this.descriptionTC = descriptionTC;
        this.note = note;
        this.position = position;
        this.preCon = preCon;
        this.postCon = postCon;
    }

    public TestCase(String idTC, String nameTC, String dateTC, String useCase, String descriptionTC, String note, int position) {
        this.idTC = idTC;
        this.nameTC = nameTC;
        this.dateTC = dateTC;
        this.useCase = useCase;
        this.descriptionTC = descriptionTC;
        this.note = note;
        this.position = position;
    }

    public TestCase(String idTC, String nameTC, String dateTC, String useCase, String descriptionTC, String note) {
        this.idTC = idTC;
        this.nameTC = nameTC;
        this.dateTC = dateTC;
        this.useCase = useCase;
        this.descriptionTC = descriptionTC;
        this.note = note;
    }

//    public TestCase(String idTC, String nameTC, String dateTC, String useCase, String descriptionTC, String note, String idTCDList) {
//        this.idTC = idTC;
//        this.nameTC = nameTC;
//        this.dateTC = dateTC;
//        this.useCase = useCase;
//        this.descriptionTC = descriptionTC;
//        this.note = note;
//        this.idTCDList = idTCDList;
//    }

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
