package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Data
@Table(name = "TestScript")
@NamedQuery(name = "find testscript by id", query = "Select t from TestScript t where t.idTS = :id")
public class TestScript {
    @Id
    @Column(name = "id_ts") // Maps to `idts` in the database
    private String idTS;

    @Column(name = "name_ts") // Maps to `namets` in the database
    private String nameTS;

    @Column(name = "date_ts") // Maps to `datets` in the database
    private String dateTS;

    @Column(name = "use_case_ts") // Maps to `usecase` in the database
    private String useCase;

    @Column(name = "description_ts") // Maps to `descriptionts` in the database
    private String descriptionTS;

    @Column(name = "test_case_ts") // Maps to `testcase` in the database
    private String testCase;

    @Column(name = "pre_con_ts") // Maps to `precon` in the database
    private String preCon;

    @Column(name = "post_con_ts") // Maps to `postcon` in the database
    private String postCon;

    @Column(name = "note_ts") // Maps to `freetext` in the database
    private String freeText;

    @Column(name = "position_ts") // Maps to `position` in the database
    private UUID position;

    @Column(name = "projectName", nullable = false, precision = 10)
    private String projectName;
    @Column(name = "tester", nullable = false, precision = 10)
    private String tester;
    private boolean markedForDeletion = false; // ฟิลด์สำหรับระบุสถานะว่าต้องการลบหรือไม่

    public TestScript(String idTS, String nameTS, String dateTS, String useCase, String descriptionTS, String testCase, String preCon, String postCon, String freeText, UUID position) {
        this.idTS = idTS;
        this.nameTS = nameTS;
        this.dateTS = dateTS;
        this.useCase = useCase;
        this.descriptionTS = descriptionTS;
        this.testCase = testCase;
        this.preCon = preCon;
        this.postCon = postCon;
        this.freeText = freeText;
        this.position = position;
    }

    public TestScript(String idTS, String nameTS, String dateTS, String useCase, String descriptionTS, String testCase, String preCon, String postCon, String freeText, UUID position, String projectName, String tester) {
        this.idTS = idTS;
        this.nameTS = nameTS;
        this.dateTS = dateTS;
        this.useCase = useCase;
        this.descriptionTS = descriptionTS;
        this.testCase = testCase;
        this.preCon = preCon;
        this.postCon = postCon;
        this.freeText = freeText;
        this.position = position;
        this.projectName = projectName;
        this.tester = tester;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestScript that = (TestScript) o;
        return getIdTS() != null && Objects.equals(getIdTS(), that.getIdTS());
    }

    @Override
    public String toString() {
        return idTS + " : " + nameTS;
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }

    public boolean isId(String id) {
        return this.idTS.equals(id);
    }


}
