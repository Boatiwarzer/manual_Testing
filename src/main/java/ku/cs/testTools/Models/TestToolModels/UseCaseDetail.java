package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Data
@Entity
@Table(name = "UseCase_Detail")
@NamedQuery(name = "find usecasedetail by id", query = "Select t from UseCaseDetail t where t.useCaseID = :id")
public class UseCaseDetail {
    @Id
    @Column(name = "id_ucd", nullable = false, unique = true)
    private String id;

    @Column(name = "idUC", nullable = false)
    private String useCaseID;

    @Column(name = "action_ucd", length = 255)
    private String action;

    @Column(name = "number_ucd")
    private int number;

    @Column(name = "detail_ucd", columnDefinition = "TEXT")
    private String detail;


    public UseCaseDetail(String id, String useCaseID, String action, int number, String detail) {
        this.id = id;
        this.useCaseID = useCaseID;
        this.action = action;
        this.number = number;
        this.detail = detail;
    }

    public UseCaseDetail() {
        // Default constructor
    }

    public boolean isId(String id) {
        return this.useCaseID.equals(id);
    }

    @Override
    public String toString() {
        return "UseCaseDetail{" +
                "useCaseID='" + id + '\'' +
                "useCaseID='" + useCaseID + '\'' +
                ", action='" + action + '\'' +
                ", number=" + number +
                ", detail='" + detail + '\'' +
                '}';
    }
}
