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
    private String idTR;
    private String nameTR;
    private String dateTR;
    private String noteTR;
    private String statusTR;
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

    public TestResult(String idTR, String nameTR, String dateTR, String noteTR, String statusTR) {
        this.idTR = idTR;
        this.nameTR = nameTR;
        this.dateTR = dateTR;
        this.noteTR = noteTR;
        this.statusTR = statusTR;
    }
    public String setDateTR() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.dateTR = dateTime.format(formatter);
        return dateTR;
    }
    private boolean markedForDeletion = false;
//    @Override
//    public final int hashCode() {
//        return getClass().hashCode();
//    }

    public String getIdTR() {
        return idTR;
    }

    public String getNameTR() {
        return nameTR;
    }

    public String getDateTR() {
        return dateTR;
    }

    public String getNoteTR() {
        return noteTR;
    }

    public void setIdTR(String idTR) {
        this.idTR = idTR;
    }

    public void setNameTR(String nameTR) {
        this.nameTR = nameTR;
    }

    public void setDateTR(String dateTR) {
        this.dateTR = dateTR;
    }

    public void setNoteTR(String noteTR) {
        this.noteTR = noteTR;
    }

    public boolean isId(String idTR) {
        return this.idTR.equals(idTR);
    }

//    @Override
//    public String toString() {
//        return "TestResult{" +
//                "TestResultID='" + idTR + '\'' +
//                "TestResultName='" + nameTR + '\'' +
//                "TestResultDate='" + dateTR + '\'' +
//                "TestResultNote='" + noteTR + '\'' +
//                '}';
//    }
    @Override
    public String toString() {
    return idTR + " : " + nameTR;
}
}
