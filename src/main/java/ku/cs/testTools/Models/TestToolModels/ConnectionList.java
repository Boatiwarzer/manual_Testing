package ku.cs.testTools.Models.TestToolModels;

import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.UUID;

@Data
public class ConnectionList {
    private ArrayList<Connection> connectionList;

    public ConnectionList() {
        connectionList = new ArrayList<>();
    }

    public void addOrUpdate(Connection connection) {
        for (int i = 0; i < connectionList.size(); i++) {
            Connection existing = connectionList.get(i);
            if (existing.getConnectionID().equals(connection.getConnectionID())) { // ✅ ใช้ .equals()
                connectionList.set(i, connection);
                return; // อัปเดตแล้วออกจากเมธอดทันที
            }
        }
        connectionList.add(connection); // ถ้ายังไม่มี ให้เพิ่มใหม่
    }

    public void deleteTestCase(Connection connection) {
        connectionList.removeIf(existing -> existing.getConnectionID().equals(connection.getConnectionID())); // ✅ ใช้ .equals()
    }

    public void sort(Comparator<Connection> cmp) {
        connectionList.sort(cmp);
    }

    public void deleteDecisionByID(UUID id) {
        Iterator<Connection> iterator = connectionList.iterator();
        while (iterator.hasNext()) {
            Connection existing = iterator.next();
            if (existing.getConnectionID().equals(id)) { // ✅ ใช้ .equals()
                iterator.remove();
                return; // ลบแล้วออกจากเมธอดทันที
            }
        }
        System.out.println("No Connection found with ID: " + id);
    }

    public UUID findLastConnectionID() {
        if (connectionList.isEmpty()) {
            return null; // หรือ UUID.randomUUID() ถ้าอยากให้มีค่า default
        }
        return connectionList.get(connectionList.size() - 1).getConnectionID();
    }

    public void updateConnection(UUID connectionID, double startX, double startY, double endX, double endY) {
        for (Connection connection : connectionList) {
            if (connection.getConnectionID().equals(connectionID)) { // ✅ ใช้ .equals()
                connection.setStartX(startX);
                connection.setStartY(startY);
                connection.setEndX(endX);
                connection.setEndY(endY);
                return; // อัปเดตแล้วออกจากเมธอดทันที
            }
        }
    }

    public void updateConnection(Connection connection) {
        for (Connection c : connectionList) {
            if (c.getConnectionID().equals(connection.getConnectionID())) { // ✅ ใช้ .equals()
                c.setStartX(connection.getStartX());
                c.setStartY(connection.getStartY());
                c.setEndX(connection.getEndX());
                c.setEndY(connection.getEndY());
                return; // อัปเดตแล้วออกจากเมธอดทันที
            }
        }
    }

    public void clear() {
        connectionList.clear();
    }

    public void addConnection(Connection connection) {
        connectionList.add(connection);
    }
}
