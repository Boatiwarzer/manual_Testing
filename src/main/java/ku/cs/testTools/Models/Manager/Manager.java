package ku.cs.testTools.Models.Manager;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Data
@Entity
@Table(name = "Manager")
@NamedQuery(name = "find Manager by id", query = "Select t from Manager t where t.ID = :id")
public class Manager implements Comparable{
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true)
    private UUID ID;

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

    public void setStatusTrue(){
        this.setStatus("true");
    }
    public void setStatusFalse(){
        this.setStatus("false");
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    public boolean isId(String id) {
        return this.IDManager.equals(id);
    }
    public boolean isPN(String PN) {
        return this.projectName.equals(PN);
    }
}
