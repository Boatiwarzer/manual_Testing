package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
@Data
@Entity
@Getter
public class UseCaseDetail {
    @Id
    private String useCaseID;
    private String action;
    private int number;
    private String detail;

    public UseCaseDetail(String useCaseID, String action, int number, String detail) {
        this.useCaseID = useCaseID;
        this.action = action;
        this.number = number;
        this.detail = detail;
    }

    public void setUseCaseID(String useCaseID) {
        this.useCaseID = useCaseID;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getUseCaseID() {
        return useCaseID;
    }

    public String getAction() {
        return action;
    }

    public int getNumber() {
        return number;
    }

    public String getDetail() {
        return detail;
    }
}
