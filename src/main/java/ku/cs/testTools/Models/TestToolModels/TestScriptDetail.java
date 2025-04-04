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
@NamedQuery(name = "find testscriptdetail by id", query = "Select t from TestScriptDetail t where t.idTSD = :id")
public class TestScriptDetail {

    @Id
    @Column(name = "id_tsd")  // Optional: You can add Column annotation for clarity
    private String idTSD;

    @Column(name = "test_no_tsd")  // Add Column annotation for testNo
    private String testNo;

    @Column(name = "steps_tsd")  // Add Column annotation for steps
    private String steps;

    @Column(name = "idTS")  // Add Column annotation for idTS
    private String idTS;

    @Column(name = "date_tsd")  // Add Column annotation for dateTSD
    private String dateTSD;


    public TestScriptDetail(String idTSD, String testNo, String steps, String idTS, String dateTSD) {
        this.idTSD = idTSD;
        this.testNo = testNo;
        this.steps = steps;
        this.idTS = idTS;
        this.dateTSD = dateTSD;
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
        this.idTS = null;
        this.dateTSD = null;
    }

    public String[] toArray() {
        return new String[]{
                this.getIdTSD(),
                this.getTestNo(),
                this.getSteps(),
                this.getDateTSD()
        };
    }
}
