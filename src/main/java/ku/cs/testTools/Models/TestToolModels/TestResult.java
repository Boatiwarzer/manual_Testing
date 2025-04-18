package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "TestResult")
@NamedQuery(name = "find testresults by id", query = "Select t from TestResult t where t.idTR = :id")
public class TestResult {
    @Id
    @Column(name = "id_tr")  // Add the Column annotation
    private String idTR;

    @Column(name = "name_tr")  // Add the Column annotation
    private String nameTR;

    @Column(name = "date_tr")  // Add the Column annotation
    private String dateTR;

    @Column(name = "note_tr")  // Add the Column annotation
    private String noteTR;

    @Column(name = "projectName", nullable = false, precision = 10)
    private String projectName;
    @Column(name = "tester", nullable = false, precision = 10)
    private String tester;




    public TestResult(String idTR, String nameTR, String dateTR, String noteTR, String projectName, String tester) {
        this.idTR = idTR;
        this.nameTR = nameTR;
        this.dateTR = dateTR;
        this.noteTR = noteTR;
        this.projectName = projectName;
        this.tester = tester;
    }

    public String setDateTR() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.dateTR = dateTime.format(formatter);
        return dateTR;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestResult testResult = (TestResult) o;
        return getIdTR() != null && Objects.equals(getIdTR(), testResult.getIdTR());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }

    public boolean isId(String idTR) {
        return this.idTR.equals(idTR);
    }

    @Override
    public String toString() {
        return idTR + " : " + nameTR;
    }
}
