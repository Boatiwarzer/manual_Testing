package ku.cs.testTools.Models.TestToolModels;

import lombok.Data;

import java.util.*;

@Data
public class TestFlowPositionList {
    private ArrayList<TestFlowPosition> positionList;

    public TestFlowPositionList() {
        positionList = new ArrayList<>();
    }

    public void addPosition(TestFlowPosition position) {
        Optional<TestFlowPosition> existing = positionList.stream()
                .filter(p -> p.getPositionID().equals(position.getPositionID()))
                .findFirst();

        if (existing.isPresent()) {
            positionList.set(positionList.indexOf(existing.get()), position); // อัปเดต
        } else {
            positionList.add(position); // เพิ่มใหม่
        }
    }

    public void removePosition(TestFlowPosition position) {
        positionList.remove(position);
    }

    public TestFlowPosition findByPositionId(UUID positionId) {
        for (TestFlowPosition position : positionList) {
            if (position.getPositionID().equals(positionId)) { // ✅ ใช้ .equals() แทน ==
                return position;
            }
        }
        return null;
    }

    public List<TestFlowPosition> findAllByPositionId(UUID positionId, String projectName, String tester) {
        List<TestFlowPosition> matchedPositions = new ArrayList<>();

        for (TestFlowPosition position : positionList) {
            if (position.getPositionID().equals(positionId) &&  // ✅ ใช้ .equals()
                    position.getProjectName().trim().equalsIgnoreCase(projectName.trim()) &&
                    position.getTester().trim().equalsIgnoreCase(tester.trim())) {

                matchedPositions.add(position);
            }
        }

        return matchedPositions;
    }

    public TestFlowPosition findByPosition(double layoutX, double layoutY) {
        for (TestFlowPosition position : positionList) {
            if (position.getXPosition() == layoutX && position.getYPosition() == layoutY) {
                return position;
            }
        }
        return null;
    }

    public void updatePosition(UUID id, double newX, double newY) {
        for (TestFlowPosition position : positionList) {
            if (position.getPositionID().equals(id)) { // ✅ ใช้ .equals()
                position.setXPosition(newX);
                position.setYPosition(newY);
            }
        }
    }

    public void updateSize(UUID id, double newWidth, double newHeight) {
        for (TestFlowPosition position : positionList) {
            if (position.getPositionID().equals(id)) { // ✅ ใช้ .equals()
                position.setFitWidth(newWidth);
                position.setFitHeight(newHeight);
            }
        }
    }

    public void removePositionByID(UUID id) {
        Iterator<TestFlowPosition> iterator = positionList.iterator();
        while (iterator.hasNext()) {
            TestFlowPosition position = iterator.next();
            if (position.getPositionID().equals(id)) { // ✅ ใช้ .equals()
                iterator.remove();
                break;
            }
        }
    }

    public void clear() {
        positionList.clear();
    }
}
