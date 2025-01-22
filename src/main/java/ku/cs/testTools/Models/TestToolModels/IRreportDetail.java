package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Data
@AllArgsConstructor
public class IRreportDetail {
    @Id
    private String idIRD;
    private String testNoIRD;
    private String testerIRD;
    private String tsIdIRD;
    private String tcIdIRD;
    private String descriptIRD;
    private String conditionIRD;
    private String imageIRD;
    private String priorityIRD;
    private String rcaIRD;
    private String managerIRD;
    private String statusIRD;
    private String remarkIRD;
    @ManyToOne
    private IRreport iRreport;
    private String idIR;
    private String idTRD;

    public IRreportDetail(String idIRD, String testNoIRD, String testerIRD, String tsIdIRD, String tcIdIRD, String descriptIRD, String conditionIRD, String imageIRD, String priorityIRD, String rcaIRD, String managerIRD, String statusIRD, String remarkIRD, String idIR, String idTRD) {
        this.idIRD = idIRD;
        this.testNoIRD = testNoIRD;
        this.testerIRD = testerIRD;
        this.tsIdIRD = tsIdIRD;
        this.tcIdIRD = tcIdIRD;
        this.descriptIRD = descriptIRD;
        this.conditionIRD = conditionIRD;
        this.imageIRD = imageIRD;
        this.priorityIRD = priorityIRD;
        this.rcaIRD = rcaIRD;
        this.managerIRD = managerIRD;
        this.statusIRD = statusIRD;
        this.remarkIRD = remarkIRD;
        this.idIR = idIR;
        this.idTRD = idTRD;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IRreportDetail that = (IRreportDetail) o;
        return getIdIRD() != null && Objects.equals(getIdIRD(), that.getIdIRD());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }

    public boolean isId(String id) {
        return this.idIRD.equals(id);
    }
    public boolean isTrd(String id) {
        return this.idTRD.equals(id);
    }
}
