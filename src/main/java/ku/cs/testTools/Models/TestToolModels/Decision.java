package ku.cs.testTools.Models.TestToolModels;


import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "Decision")
@NamedQuery(name = "find Decision by id", query = "Select t from Decision t where t.idDS = :id")
public class Decision {
    @Id
    @Access(AccessType.FIELD)
    private int idDS;
    private String nameDS;
    private String note;
}
