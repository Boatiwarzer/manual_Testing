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
public class Manager implements Comparable{
    @Id
    @Access(AccessType.FIELD)
    private String IDManager;
    private String projectName;
    private String nameManager;
    private String date;
    @OneToMany
    private List<Tester> nameTester;




    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
