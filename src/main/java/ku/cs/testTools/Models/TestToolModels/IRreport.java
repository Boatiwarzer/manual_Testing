package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "IRreport")
@NamedQuery(name = "find IRreport by id", query = "Select t from IRreport t where t.idIR = :id")
public class IRreport {
    @Id
    @Column(name = "id_ir", nullable = false)
    private String idIR;

    @Column(name = "name_ir", nullable = false)
    private String nameIR;

    @Column(name = "date_ir")
    private String dateIR;

    @Column(name = "note_ir")
    private String NoteIR;

    @Column(name = "tr_ir")
    private String trIR;

    @Column(name = "projectName", nullable = false, precision = 10)
    private String projectName;
    @Column(name = "tester", nullable = false, precision = 10)
    private String tester;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IRreport iRreport = (IRreport) o;
        return getIdIR() != null && Objects.equals(getIdIR(), iRreport.getIdIR());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }

    public IRreport(String idIR, String nameIR, String dateIR, String noteIR, String trIR) {
        this.idIR = idIR;
        this.nameIR = nameIR;
        this.dateIR = dateIR;
        this.NoteIR = noteIR;
        this.trIR = trIR;
    }

    public IRreport(String idIR, String nameIR, String dateIR, String noteIR, String trIR, String projectName, String tester) {
        this.idIR = idIR;
        this.nameIR = nameIR;
        this.dateIR = dateIR;
        this.NoteIR = noteIR;
        this.trIR = trIR;
        this.projectName = projectName;
        this.tester = tester;
    }

    public boolean isId(String idIR) {
        return this.idIR.equals(idIR);
    }

    public String setDateIR() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.dateIR = dateTime.format(formatter);
        return dateIR;
    }

    @Override
    public String toString() {
        return idIR + " : " + nameIR;
    }
}
