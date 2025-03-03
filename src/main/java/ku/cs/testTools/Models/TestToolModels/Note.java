package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.Objects;
import java.util.UUID;

@Entity
@RequiredArgsConstructor
@Data
@NamedQuery(name = "find Note by id", query = "Select t from Note t where t.noteID = :id")
@Table(name = "Note")
public class Note {
    @Id
    private String noteID;
    private String note;
    @Column(name = "projectName", nullable = false, precision = 10, scale = 2)
    private String projectName;
    @Column(name = "tester", nullable = false, precision = 10, scale = 2)
    private String tester;

    public Note(String noteID, String note, String projectName, String tester) {
        this.noteID = noteID;
        this.note = note;
        this.projectName = projectName;
        this.tester = tester;
    }

    public String toString() {
        return "SubSystemID: " + noteID + ", Note: " + note;
    }

    public boolean isId(String noteID) {
        return this.noteID.equals(noteID);
    }
}
