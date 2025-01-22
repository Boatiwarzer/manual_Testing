package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@Table(name = "use_case")
@NamedQuery(name = "find usecase by id", query = "Select t from UseCase t where t.useCaseID = :id")
public class UseCase {

    @Id
    @Access(AccessType.FIELD)
    @Column(name = "use_case_id", nullable = false)
    private String useCaseID;

    @Column(name = "use_case_name", length = 255, nullable = false)
    private String useCaseName;

    @Column(name = "actor", length = 255)
    private String actor;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "pre_condition", columnDefinition = "TEXT")
    private String preCondition;

    @Column(name = "post_condition", columnDefinition = "TEXT")
    private String postCondition;

    @Column(name = "note", length = 255)
    private String note;

    @Column(name = "date", columnDefinition = "DATETIME")
    private String date;

    // Default Constructor
    public UseCase() {}

    // Parametrized Constructor
    public UseCase(String useCaseID, String useCaseName, String actor, String description, String preCondition, String postCondition, String note, String date) {
        this.useCaseID = useCaseID;
        this.useCaseName = useCaseName;
        this.actor = actor;
        this.description = description;
        this.preCondition = preCondition;
        this.postCondition = postCondition;
        this.note = note;
        this.date = date;
    }

    // Automatically sets the date before the entity is persisted
    @PrePersist
    public void prePersist() {
        if (this.date == null) {
            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            this.date = dateTime.format(formatter);
        }
    }

    public boolean isId(String id) {
        return this.useCaseID.equals(id);
    }

    @Override
    public String toString() {
        return "UseCase{" +
                "useCaseID='" + useCaseID + '\'' +
                ", useCaseName='" + useCaseName + '\'' +
                ", actor='" + actor + '\'' +
                ", description='" + description + '\'' +
                ", preCondition='" + preCondition + '\'' +
                ", postCondition='" + postCondition + '\'' +
                ", note='" + note + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
