package ku.cs.testTools.Services;

import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.ManageDataSource;
import ku.cs.testTools.Models.Manager.*;
import java.util.Comparator;

public class AccountComparable implements Comparator<Account> {
    @Override
    public int compare(Account o1, Account o2) {
        return o2.getDate().compareTo(o1.getDate());
    }

}
