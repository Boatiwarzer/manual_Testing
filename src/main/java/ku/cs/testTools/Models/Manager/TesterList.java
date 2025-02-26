package ku.cs.testTools.Models.Manager;

import ku.cs.testTools.Models.Manager.Tester;
import lombok.Data;

import java.util.ArrayList;
@Data
public class TesterList {
    private ArrayList<Tester> TesterList;

    public TesterList() {
        TesterList = new ArrayList<Tester>();
    }

    public void addTester(Tester Tester) {
        TesterList.add(Tester);
    }

    public void removeTester(Tester Tester) {
        TesterList.remove(Tester);
    }

    public void clear() {
        TesterList.clear();
    }
    public Tester findTCById(String id) {
        for (Tester Tester : TesterList) {
            if (Tester.isId(id) ) {
                return Tester;
            }
        }
        return null;
    }
    public void addOrUpdateTester(Tester Tester) {
        boolean exists = false;

        // Iterate through the list to check for an existing item with the same ID
        for (int i = 0; i < TesterList.size(); i++) {
            Tester existing = TesterList.get(i);

            if (existing.isId(Tester.getIdTester())) {
                // Update existing item
                TesterList.set(i, Tester);
                exists = true;
                break;
            }
        }

        // If the item does not exist, add it to the list
        if (!exists) {
            TesterList.add(Tester);
        }
    }
    public void deleteTester(Tester Tester) {
        // Iterate through the list to find the item to delete
        for (int i = 0; i < TesterList.size(); i++) {
            Tester existing = TesterList.get(i);
            if (existing.isId(Tester.getIdTester())) {
                // Remove the item from the list
                TesterList.remove(i);
                break; // Exit after removing the first match
            }
        }
    }
}
