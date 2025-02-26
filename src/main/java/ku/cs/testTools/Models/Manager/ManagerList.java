package ku.cs.testTools.Models.Manager;

import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;

@Data
public class ManagerList {
    private ArrayList<Manager> ManagerList;

    public ManagerList() {
        ManagerList = new ArrayList<Manager>();
    }

    public void addManager(Manager Manager) {
        ManagerList.add(Manager);
    }

    public void removeManager(Manager Manager) {
        ManagerList.remove(Manager);
    }

    public void clear() {
        ManagerList.clear();
    }
    public Manager findTCById(String id) {
        for (Manager Manager : ManagerList) {
            if (Manager.isId(id) ) {
                return Manager;
            }
        }
        return null;
    }
    public void addOrUpdateManager(Manager Manager) {
        boolean exists = false;

        // Iterate through the list to check for an existing item with the same ID
        for (int i = 0; i < ManagerList.size(); i++) {
            Manager existing = ManagerList.get(i);

            if (existing.isId(Manager.getIDManager())) {
                // Update existing item
                ManagerList.set(i, Manager);
                exists = true;
                break;
            }
        }

        // If the item does not exist, add it to the list
        if (!exists) {
            ManagerList.add(Manager);
        }
    }
    public void deleteManager(Manager Manager) {
        // Iterate through the list to find the item to delete
        for (int i = 0; i < ManagerList.size(); i++) {
            Manager existing = ManagerList.get(i);
            if (existing.isId(Manager.getIDManager())) {
                // Remove the item from the list
                ManagerList.remove(i);
                break; // Exit after removing the first match
            }
        }
    }
    


    
}
