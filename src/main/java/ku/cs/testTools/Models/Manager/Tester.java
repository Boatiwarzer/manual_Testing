package ku.cs.testTools.Models.Manager;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Tester")
@NamedQuery(name = "find Tester by id", query = "Select t from Tester t where t.IdTester = :id")
public class Tester {
    @Id
    @Access(AccessType.FIELD)
    private String IdTester;
    private String nameTester;
    private String projectName;
    private String manager;

    public Tester(String idTester, String nameTester, String projectName) {
        this.IdTester = idTester;
        this.nameTester = nameTester;
        this.projectName = projectName;
    }

    public Tester(String idTester, String nameTester, String projectName, String manager) {
        IdTester = idTester;
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
