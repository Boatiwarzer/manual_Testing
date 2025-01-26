package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "test_results")
@NamedQuery(name = "find testresults by id", query = "Select t from TestResult t where t.idTR = :id")
public class TestResult {
    @Id
    @Access(AccessType.FIELD)
    @Column(name = "idTR")  // Add the Column annotation
    private String idTR;

    @Column(name = "nameTR")  // Add the Column annotation
    private String nameTR;

    @Column(name = "dateTR")  // Add the Column annotation
    private String dateTR;

    @Column(name = "noteTR")  // Add the Column annotation
    private String noteTR;

    @Column(name = "markedForDeletion")  // Add the Column annotation
    private boolean markedForDeletion = false;

    public TestResult(String idTR, String nameTR, String dateTR, String noteTR) {
        this.idTR = idTR;
        this.nameTR = nameTR;
        this.dateTR = dateTR;
//        this.noteTR = noteTR;
        this.noteTR = "-";
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
