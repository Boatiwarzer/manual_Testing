package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Data
@Entity
@Table(name = "use_case_detail")
@NamedQuery(name = "find usecasedetail by id", query = "Select t from UseCaseDetail t where t.ID = :id")
public class UseCaseDetail {
    @Id
    @UuidGenerator
    @GeneratedValue
    @Access(AccessType.FIELD)
    @Column(name = "id", nullable = false, unique = true)
    private UUID ID;

    @Column(name = "use_case_id", nullable = false)
    private String useCaseID;

    @Column(name = "action", length = 255)
    private String action;

    @Column(name = "number")
    private int number;

    @Column(name = "detail", columnDefinition = "TEXT")
    private String detail;

    public UseCaseDetail(String useCaseID, String action, int number, String detail) {
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
                "useCaseID='" + useCaseID + '\'' +
                ", action='" + action + '\'' +
                ", number=" + number +
                ", detail='" + detail + '\'' +
                '}';
    }
}
