package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Data
@Entity
public class UseCase {
    @Id
    private String useCaseID;
    private String useCaseName;
    private String actor;
    private String description;
    private String preCondition;
    private String postCondition;
    private String note;
    private String date;

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

    public UseCase() {
        this.useCaseID = useCaseID;
        this.useCaseName = useCaseName;
        this.actor = actor;
        this.description = description;
        this.preCondition = preCondition;
        this.postCondition = postCondition;
        this.note = note;
        this.date = date;
    }

//    public UseCase(String useCaseID, String useCaseName, String actor, String description, String preCondition, String postCondition) {
//        this.useCaseID = useCaseID;
//        this.useCaseName = useCaseName;
//        this.actor = actor;
//        this.description = description;
//        this.preCondition = preCondition;
//        this.postCondition = postCondition;
//        this.note = "None";
//    }

    public String getDate() {
        return date;
    }

    public void setUseCaseID(String useCaseID) {
        this.useCaseID = useCaseID;
    }

    public void setUseCaseName(String useCaseName) {
        this.useCaseName = useCaseName;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPreCondition(String preCondition) {
        this.preCondition = preCondition;
    }

    public void setPostCondition(String postCondition) {
        this.postCondition = postCondition;
    }

    public void setNote (String note) {
        this.note = note;
    }

    public String getUseCaseID() {
        return useCaseID;
    }

    public String getUseCaseName() {
        return useCaseName;
    }

    public String getActor() {
        return actor;
    }

    public String getDescription() {
        return description;
    }

    public String getPreCondition() {
        return preCondition;
    }

    public String getPostCondition() {
        return postCondition;
    }

    public String getNote() {
        return note;
    }

    public String setDate() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.date = dateTime.format(formatter);
        return date;
    }
    public boolean isId(String id) {
        return this.useCaseID.equals(id);
    }

//    @Override
//    public String toString() {
//        return useCaseID + " : " + useCaseName;
//    }

    @Override
    public String toString() {
        return "UseCase{" +
                "useCaseID='" + useCaseID + '\'' +
                "useCaseName='" + useCaseName + '\'' +
                "useCaseActor='" + actor + '\'' +
                "useCaseDescript='" + description + '\'' +
                "useCasePreCon='" + preCondition + '\'' +
                "useCasePostCon='" + postCondition + '\'' +
                "useCaseNote='" + note + '\'' +
                "useCaseDate='" + date + '\'' +
                '}';
    }

}
