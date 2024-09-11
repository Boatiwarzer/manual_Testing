package ku.project.bashDream.Services;

import ku.project.bashDream.Models.Account;

import java.util.Comparator;

public class AccountComparable implements Comparator<Account> {
    @Override
    public int compare(Account o1, Account o2) {
        return o2.getDate().compareTo(o1.getDate());
    }

}
