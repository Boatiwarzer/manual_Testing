package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IDTest {
    private String id = "id";
    private int idUC;
    private int idTC;
    private int idTCD;
    private int idTS;
    private int idTSD;
    private int idTR;
    private int idTRD;

    public boolean isId(String id) {
        return this.id.equals(id);
    }
}
