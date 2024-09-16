package ku.cs.testTools.Services;

import ku.cs.testTools.Models.Manager.*;
import java.util.Comparator;

public class ManagerComparable implements Comparator<Manager> {
    @Override
    public int compare(Manager o1, Manager o2) {
        return o2.getDate().compareTo(o1.getDate());
    }

}
