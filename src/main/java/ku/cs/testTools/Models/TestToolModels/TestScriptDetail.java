package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "TestScript_Detail")
@NamedQuery(name = "find testscriptdetail by id", query = "Select t from TestScriptDetail t where t.ID = :id")
public class TestScriptDetail {

    @Id
    @UuidGenerator
    @GeneratedValue
    @Access(AccessType.FIELD)
    @Column(name = "id", nullable = false, unique = true)
    private UUID ID;

    @Column(name = "id_tsd")  // Optional: You can add Column annotation for clarity
    private String idTSD;

    @Column(name = "test_no_tsd")  // Add Column annotation for testNo
    private String testNo;

    @Column(name = "steps_tsd")  // Add Column annotation for steps
    private String steps;

    @Column(name = "inputdata_tsd")  // Add Column annotation for inputData
    private String inputData;

    @Column(name = "expected_tsd")  // Add Column annotation for expected
    private String expected;

    @Column(name = "idTS")  // Add Column annotation for idTS
    private String idTS;

    @Column(name = "date_tsd")  // Add Column annotation for dateTSD
    private String dateTSD;

    @ManyToOne
    @JoinColumn(name = "id_ts")  // Optional: Add a @JoinColumn annotation for clarity
    private TestScript testScript;

    // Uncomment and complete if you need this relationship
    // @ManyToOne
    // @JoinColumn(name = "test_script_id")  // Optional: Add JoinColumn annotation
    // private TestScript testScript;

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

    public TestScriptDetail(String idTSD, String testNo, String steps, String inputData, String expected, String idTS) {
        this.idTSD = idTSD;
        this.testNo = testNo;
        this.steps = steps;
        this.inputData = inputData;
        this.expected = expected;
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
        return Objects.hash(idTSD);
    }

    public boolean isId(String id) {
        return this.idTSD.equals(id);
    }

    public boolean isIdTS(String id) {
        return this.idTS.equals(id);
    }

    public void clear() {
        this.testNo = null;
        this.steps = null;
        this.inputData = null;
        this.expected = null;
        this.idTS = null;
        this.dateTSD = null;
    }
}
