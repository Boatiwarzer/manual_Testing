package ku.cs.testTools.Models.TestToolModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Iterator;

@Data
public class TestFlowPositionList {
    private  ArrayList<TestFlowPosition> positionList;

    public TestFlowPositionList() {
        positionList = new ArrayList<>();
    }


    public void addPosition(TestFlowPosition position) {
        positionList.add(position);
    }

//    public void addPosition(TestFlowPosition position) {
//        boolean exists = false;
//        for (int i = 0; i < positionList.size(); i++) {
//            TestFlowPosition existing = positionList.get(i);
//
//            if (existing.isId(position.getPositionID())) {
//                // Update existing item
//                positionList.set(i, position);
//                exists = true;
//                break;
//            }
//        }
//        if (!exists) {
//            positionList.add(position);
//        }
//    }

    public void removePosition(TestFlowPosition position) {
        positionList.remove(position);
    }

    public TestFlowPosition findByPositionId(int positionId) {
        for (TestFlowPosition position : positionList) {
            if (position.getPositionID() == positionId) {
                return position;
            }
        }
        return null;
    }
    public TestFlowPosition findPositionById(int positionId) {
        for (TestFlowPosition position : positionList) {
            if (position.getPositionID() == positionId) {
                return position;
            }
        }
        return null;
    }
    public TestFlowPosition findByPositionTypeStart(String name) {
        for (TestFlowPosition position : positionList) {
            String type = position.getType();
            if (type.equals(name) && type.equals("start")) {
                return position;
            }
        }
        return null;
    }
    public TestFlowPosition findByPositionTypeDecision(String name) {


        for (TestFlowPosition position : positionList) {
            String type = position.getType();
            if (type.equals(name) && type.equals("decision")) {
                return position;
            }
        }
        return null;
    }
    public TestFlowPosition findByPositionTypeTestCase(String name) {


        for (TestFlowPosition position : positionList) {
            String type = position.getType();
            if (type.equals(name) && type.equals("testcase")) {
                return position;
            }
        }
        return null;
    }
    public TestFlowPosition findByPositionTypeTestScript(String name) {


        for (TestFlowPosition position : positionList) {
            String type = position.getType();
            if (type.equals(name) && type.equals("testscript")) {
                return position;
            }
        }
        return null;
    }

    public TestFlowPosition findByPositionTypeEnd(String name) {

        for (TestFlowPosition position : positionList) {
            String type = position.getType();
            if (type.equals(name) && type.equals("end")) {
                return position;
            }
        }
        return null;
    }
    public TestFlowPosition findByPositionTypeArrow(String name) {

        for (TestFlowPosition position : positionList) {
            String type = position.getType();
            if (type.equals(name) && type.equals("arrow")) {
                return position;
            }
        }
        return null;
    }
    public TestFlowPosition findByPositionTypeLine(String name) {
        if (positionList == null || name == null) {
            return null; // Return early if inputs are invalid
        }

        for (TestFlowPosition position : positionList) {
            String type = position.getType();
            if (type.equals(name) && type.equals("line")) {
                return position;
            }
        }
        return null;
    }

    public double findLastPositionId() {
        double lastPositionId = 0;
        for (TestFlowPosition position : positionList) {
            if (position.getPositionID() > lastPositionId) {
                lastPositionId = position.getPositionID();
            }
        }
        return lastPositionId;
    }

    public TestFlowPosition findByPosition(double layoutX, double layoutY) {
        for (TestFlowPosition position : positionList) {
            if (position.getXPosition() == layoutX && position.getYPosition() == layoutY) {
                return position;
            }
        }
        return null;
    }

//    public ArrayList<TestFlowPosition> findBySubSystemID(int subSystemID) {
//        ArrayList<TestFlowPosition> positions = new ArrayList<>();
//        for (TestFlowPosition position : positionList) {
//            if (position.getSubSystemID() == subSystemID) {
//                positions.add(position);
//            }
//        }
//        return positions;
//    }



    public void updatePosition(int id, double newX, double newY) {
        for (TestFlowPosition position : positionList) {
            if (position.getPositionID() == id) {
                position.setXPosition(newX);
                position.setYPosition(newY);
            }
        }
    }
    public boolean isIDExist(int ID) {
        for (TestFlowPosition position : positionList) {
            if (position.getPositionID() == ID) {
                return true;
            }
        }
        return false;
    }

    public void updateSize(int id, double newWidth, double newHeight) {
        for (TestFlowPosition position : positionList) {
            if (position.getPositionID() == id) {
                position.setFitWidth(newWidth);
                position.setFitHeight(newHeight);
            }
        }
    }

    public void removePositionByID(int id) {
        Iterator<TestFlowPosition> iterator = positionList.iterator();
        while (iterator.hasNext()) {
            TestFlowPosition position = iterator.next();
            if (position.getPositionID() == id) {
                iterator.remove();
                break;
            }
        }
    }

    public void updateRotation(int id, double angle) {
        for (TestFlowPosition position : positionList) {
            if (position.getPositionID() == id) {
                position.setRotation(angle);
            }
        }
    }

    public void clear() {
        positionList.clear();
    }
}
