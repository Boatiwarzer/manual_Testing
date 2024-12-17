package ku.cs.testTools.Models.TestToolModels;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
@Data
public class ConnectionList {
    private ArrayList<Connection> connectionList;

    public ConnectionList() {
        connectionList = new ArrayList<Connection>();
    }

    public void addOrUpdate(Connection connection) {
        boolean exists = false;

        // Iterate through the list to check for an existing item with the same ID
        for (int i = 0; i < connectionList.size(); i++) {
            Connection existing = connectionList.get(i);

            if (existing.isId(connection.getConnectionID())) {
                // Update existing item
                connectionList.set(i, connection);
                exists = true;
                break;
            }
        }

        // If the item does not exist, add it to the list
        if (!exists) {
            connectionList.add(connection);
        }
    }
    public void deleteTestCase(Connection connection) {
        // Iterate through the list to find the item to delete
        for (int i = 0; i < connectionList.size(); i++) {
            Connection existing = connectionList.get(i);
            if (existing.isId(connection.getConnectionID())) {
                // Remove the item from the list
                connectionList.remove(i);
                break; // Exit after removing the first match
            }
        }
    }
    public void sort(Comparator<Connection> cmp) {
        connectionList.sort(cmp);
    }

    public void deleteDecisionByID(int id) {
        boolean found = false;

        // Use an iterator to safely remove items while iterating
        Iterator<Connection> iterator = connectionList.iterator();
        while (iterator.hasNext()) {
            Connection existing = iterator.next();
            int i = existing.getConnectionID();
            if (i == id) {
                iterator.remove(); // Safely remove the item
                found = true;
            }
        }

        // Log or handle the case where no matching item was found
        if (!found) {
            System.out.println("No TestCaseDetail found with ID: " + id);
        }
    }
    public void removeConnectionByID(int id) {
        for (Connection connection : connectionList) {
            if (connection.getConnectionID() == id) {
                connectionList.remove(connection);
                break;
            }
        }
    }
    public int findLastConnectionID() {
        int lastConnectionID = 0;
        for (Connection connection : connectionList) {
            if (connection.getConnectionID() > lastConnectionID) {
                lastConnectionID = connection.getConnectionID();
            }
        }
        return lastConnectionID;
    }
    public void updateConnection(int ConnectionID, double startX, double startY, double endX, double endY) {
        for (Connection connection : connectionList) {
            if (connection.getConnectionID() == ConnectionID) {
                connection.setStartX(startX);
                connection.setStartY(startY);
                connection.setEndX(endX);
                connection.setEndY(endY);
            }
        }
    }
    public void updateConnection(Connection connection) {
        for (Connection c : connectionList) {
            if (c.getConnectionID() == connection.getConnectionID()) {
                c.setStartX(connection.getStartX());
                c.setStartY(connection.getStartY());
                c.setEndX(connection.getEndX());
                c.setEndY(connection.getEndY());
            }
        }
    }

    public void clear() {
        connectionList.clear();
    }


    public Connection findByConnectionID(int id) {
        for (Connection connection : connectionList) {
            if (connection.getConnectionID() == id) {
                return connection;
            }
        }
        return null;
    }

    public void addConnection(Connection connection) {
        connectionList.add(connection);
    }
}
