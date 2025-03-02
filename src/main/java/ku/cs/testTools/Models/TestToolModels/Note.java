package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "Note")
@NamedQuery(name = "find Note by id", query = "Select t from Note t where t.noteID = :id")
public class Note {
    @Id
    @Access(AccessType.FIELD)
    private String noteID;
    private String note;
    @Column(name = "projectName", nullable = false, precision = 10, scale = 2)
    private String projectName;
    @Column(name = "tester", nullable = false, precision = 10, scale = 2)
    private String tester;



    public String toString() {
        return "SubSystemID: " + noteID + ", Note: " + note;
    }

    public boolean isId(String noteID) {
        return this.noteID.equals(noteID);
    }
}
