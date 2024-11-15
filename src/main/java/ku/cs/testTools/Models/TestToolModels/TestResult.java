package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class TestResult {
    @Id
    private String idTR;
    private String nameTR;
    private String dateTR;
    private String noteTR;

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

    public TestResult(String idTR, String nameTR, String dateTR, String noteTR) {
        this.idTR = idTR;
        this.nameTR = nameTR;
        this.dateTR = dateTR;
        this.noteTR = noteTR;
    }
    public String setDateTR() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.dateTR = dateTime.format(formatter);
        return dateTR;
    }
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

    @Override
    public String toString() {
        return "TestResult{" +
                "TestResultID='" + idTR + '\'' +
                '}';
    }
}
