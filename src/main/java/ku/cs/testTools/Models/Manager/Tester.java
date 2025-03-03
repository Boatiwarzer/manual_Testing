package ku.cs.testTools.Models.Manager;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Data
@Entity
@Table(name = "Tester")
@NamedQuery(name = "find Tester by id", query = "Select t from Tester t where t.ID = :id")
public class Tester {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true)
    private UUID ID;
    private String IdTester;
    private String nameTester;
    private String projectName;
    private String manager;



    public Tester(String idTester, String nameTester, String projectName, String manager) {
        this.IdTester = idTester;
        this.nameTester = nameTester;
        this.projectName = projectName;
        this.manager = manager;
    }

    public Tester() {

    }

    public boolean isId(String id) {
        return this.IdTester.equals(id);
    }
}
