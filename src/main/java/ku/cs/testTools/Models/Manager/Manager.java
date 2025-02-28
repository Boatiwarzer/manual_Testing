package ku.cs.testTools.Models.Manager;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import lombok.Setter;

@Getter
@Data
@Entity
@Table(name = "Manager")
@NamedQuery(name = "find Manager by id", query = "Select t from Manager t where t.IDManager = :id")
public class Manager implements Comparable{
    @Id
    @Access(AccessType.FIELD)
    private String IDManager;
    private String projectName;
    private String nameManager;
    private String date;
    private String status;

    public Manager(String IDManager, String projectName, String nameManager, String date, String status) {
        this.IDManager = IDManager;
        this.projectName = projectName;
        this.nameManager = nameManager;
        this.date = date;
        this.status = status;
    }

    public Manager(String IDManager, String projectName, String nameManager, String date) {
        this.IDManager = IDManager;
        this.projectName = projectName;
        this.nameManager = nameManager;
        this.date = date;
    }


    public Manager() {

    }



    @Override
    public int compareTo(Object o) {
        return 0;
    }

    public boolean isId(String id) {
        return this.IDManager.equals(id);
    }
}
