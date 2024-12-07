package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Data
@Entity
@Getter
@Table(name = "use_case_detail")
@NamedQuery(name = "find usecasedetail by id", query = "Select t from UseCaseDetail t where t.useCaseID = :id")
public class UseCaseDetail {
    @Id
    @Access(AccessType.FIELD)
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

    public UseCaseDetail() {

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
}
