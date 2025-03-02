package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "test_cases")
@NamedQuery(name = "find testcase by id", query = "Select t from TestCase t where t.idTC = :id")
public class TestCase {
    @Id
    @UuidGenerator
    @GeneratedValue
    @Access(AccessType.FIELD)
    @Column(name = "id", nullable = false, unique = true)
    private UUID ID;

    @Column(name = "id_tc", nullable = false)
    private String idTC;

    @Column(name = "name_tc", nullable = false)
    private String nameTC;

    @Column(name = "date_tc")
    private String dateTC;

    @Column(name = "use_case")
    private String useCase;

    @Column(name = "description_tc")
    private String descriptionTC;

    @Column(name = "note")
    private String note;

//    @OneToMany(mappedBy = "testCase", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<TestCaseDetail> testCaseList;

    @Column(name = "position", nullable = false)
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

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestCase testCase = (TestCase) o;
        return idTC != null && idTC.equals(testCase.idTC);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(idTC);
    }

    @Override
    public String toString() {
        return idTC + " : " + nameTC;
    }

    public boolean isId(String id) {
        return this.idTC != null && this.idTC.equals(id);
    }
}
