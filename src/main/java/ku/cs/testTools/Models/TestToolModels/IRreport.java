package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class IRreport {
    @Id
    @Column(name = "id_ir", nullable = false, unique = true)
    private String idIR;

    @Column(name = "name_ir", nullable = false)
    private String nameIR;

    @Column(name = "date_ir")
    private String dateIR;

    @Column(name = "tr_ir")
    private String trIR;

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

    public IRreport(String idIR, String nameIR, String dateIR, String trIR) {
        this.idIR = idIR;
        this.nameIR = nameIR;
        this.dateIR = dateIR;
        this.trIR = trIR;
    }

    public boolean isId(String idIR) {
        return this.idIR.equals(idIR);
    }

    public String setDateIR() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.dateIR = dateTime.format(formatter);
        return dateIR;
    }

    @Override
    public String toString() {
        return idIR + " : " + nameIR;
    }
}
