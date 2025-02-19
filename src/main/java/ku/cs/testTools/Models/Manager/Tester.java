package ku.cs.testTools.Models.Manager;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Tester {
    @Id
    @Access(AccessType.FIELD)
    private String IdTester;
    private String nameTester;

    @ManyToOne
    private Manager projectName;
}
