package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class Note {
    @Id
    @Access(AccessType.FIELD)
    private String noteID;
    private String note;




    public String toString() {
        return "SubSystemID: " + noteID + ", Note: " + note;
    }

    public boolean isId(String noteID) {
        return Objects.equals(this.noteID, noteID);
    }
}
