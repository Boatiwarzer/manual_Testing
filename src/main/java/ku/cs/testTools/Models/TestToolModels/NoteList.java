package ku.cs.testTools.Models.TestToolModels;

import lombok.Data;

import java.util.ArrayList;
import java.util.Objects;

@Data

public class NoteList {
    private ArrayList<Note> noteList;

    public NoteList() {
        noteList = new ArrayList<Note>();
    }

    public ArrayList<Note> getNoteList() {
        return noteList;
    }

    public void addNote(Note note) {
        boolean exists = false;

        // Iterate through the list to check for an existing item with the same ID
        for (int i = 0; i < noteList.size(); i++) {
            Note existing = noteList.get(i);

            if (existing.isId(note.getNoteID())) {
                // Update existing item
                noteList.set(i, note);
                exists = true;
                break;
            }
        }
        if (!exists) {
            noteList.add(note);
        }
    }
    public void removeNote(Note note) {
        noteList.remove(note);
    }

    public Note findBynoteID(String noteID) {
        for (Note note : noteList) {
            if (Objects.equals(note.getNoteID(), noteID)) {
                return note;
            }
        }
        return null;
    }

    public void updateNoteBynoteID(String noteID, String text) {
        for (Note note : noteList) {
            if (Objects.equals(note.getNoteID(), noteID)) {
                note.setNote(text);
            }
        }
    }
}
