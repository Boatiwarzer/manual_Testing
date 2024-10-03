package ku.cs.testTools.Models.TestToolModels;

import lombok.Getter;

@Getter
public class UseCase {
    private String useCaseID;
    private String useCaseName;
    private String actor;
    private String description;
    private String preCondition;
    private String postCondition;
    private String note;

    public UseCase(String useCaseID, String useCaseName, String actor, String description, String preCondition, String postCondition, String note) {
        this.useCaseID = useCaseID;
        this.useCaseName = useCaseName;
        this.actor = actor;
        this.description = description;
        this.preCondition = preCondition;
        this.postCondition = postCondition;
        this.note = note;
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


//    @Override
//    public String toString() {
//        return "UseCase{" +
//                "useCaseID='" + useCaseID + '\'' +
//                '}';
//    }
}
