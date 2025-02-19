package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
@NamedQuery(name = "find IRreportDetail by id", query = "Select t from IRreportDetail t where t.idIRD = :id")
public class IRreportDetail {
    @Id
    @Access(AccessType.FIELD)
    @Column(name = "id_ird", nullable = false, unique = true)
    private String idIRD;

    @Column(name = "test_no_ird", nullable = false)
    private String testNoIRD;

    @Column(name = "tester_ird")
    private String testerIRD;

    @Column(name = "ts_id_ird")
    private String tsIdIRD;

    @Column(name = "tc_id_ird")
    private String tcIdIRD;

    @Column(name = "description_ird")
    private String descriptIRD;

    @Column(name = "condition_ird")
    private String conditionIRD;

    @Column(name = "image_ird")
    private String imageIRD;

    @Column(name = "retest_ird")
    private String retestIRD;

    @Column(name = "priority_ird")
    private String priorityIRD;

    @Column(name = "rca_ird")
    private String rcaIRD;

    @Column(name = "manager_ird")
    private String managerIRD;

    @Column(name = "status_ird")
    private String statusIRD;

    @Column(name = "remark_ird")
    private String remarkIRD;


    @ManyToOne
    //@JoinColumn(name = "id_ir", referencedColumnName = "idIR", nullable = false)
    private IRreport iRreport;
    private String idIR;
    private String idTRD;

    public IRreportDetail(String idIRD, String testNoIRD, String testerIRD, String tsIdIRD, String tcIdIRD, String descriptIRD, String conditionIRD, String imageIRD, String retestIRD, String priorityIRD, String rcaIRD, String managerIRD, String statusIRD, String remarkIRD, String idIR, String idTRD) {
        this.idIRD = idIRD;
        this.testNoIRD = testNoIRD;
        this.testerIRD = testerIRD;
        this.tsIdIRD = tsIdIRD;
        this.tcIdIRD = tcIdIRD;
        this.descriptIRD = descriptIRD;
        this.conditionIRD = conditionIRD;
        this.imageIRD = imageIRD;
        this.retestIRD = retestIRD;
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
        return idIRD != null && idIRD.equals(that.idIRD);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(idIRD);
    }

    public boolean isId(String id) {
        return this.idIRD != null && this.idIRD.equals(id);
    }
    public boolean isIrd(String id) {
        return this.idIR!= null && this.idIR.equals(id);
    }
    public boolean isTrd(String id) {
        return this.idTRD.equals(id);
    }
}
