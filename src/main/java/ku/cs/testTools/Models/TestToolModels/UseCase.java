package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Data
@Entity
@Table(name = "UseCase")
@NamedQuery(name = "find usecase by id", query = "Select t from UseCase t where t.ID = :id")
public class UseCase {

    @Id
    @UuidGenerator
    @GeneratedValue
    @Access(AccessType.FIELD)
    @Column(name = "id", nullable = false, unique = true)
    private UUID ID;

    @Column(name = "id_uc", nullable = false)
    private String useCaseID;

    @Column(name = "name_uc", length = 255, nullable = false)
    private String useCaseName;

    @Column(name = "actor_uc", length = 255)
    private String actor;

    @Column(name = "description_uc", columnDefinition = "TEXT")
    private String description;

    @Column(name = "pre_con_uc", columnDefinition = "TEXT")
    private String preCondition;

    @Column(name = "post_con_uc", columnDefinition = "TEXT")
    private String postCondition;

    @Column(name = "note_uc", length = 255)
    private String note;

    @Column(name = "date_uc", columnDefinition = "DATETIME")
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
