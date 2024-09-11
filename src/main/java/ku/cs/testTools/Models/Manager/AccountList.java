package ku.project.bashDream.Models;

import ku.project.bashDream.Services.Datasource;

import java.util.*;

public class AccountList {
    private ArrayList<Account> accounts;


    public AccountList() {
        accounts = new ArrayList<>();
    }
    public void addNewAccount(String role, String name, String username, String password, String img, String date) {
        role = role.trim();
        name = name.trim();
        password = password.trim();
        img = img.trim();
        date = date.trim();
        if (!username.equals("") && !name.equals("")) {
            Account exist = findAccountByUserName(username);
            if (exist == null) {
                accounts.add(new Account(role.trim(), name.trim(), username.trim(), password.trim(), date.trim()));
            }
        }
    }
    public void addAccount(Account account) {
        accounts.add(account);
    }





    public Account findAccountByUserName(String username) {
        for (Account account : accounts) {
            if (account.isUserName(username) ) {
                return account;
            }
        }
        return null;
    }
    public void sort(){
        Collections.sort(accounts);
    }

    public void sort(Comparator<Account> cmp){
        Collections.sort(accounts, cmp);
    }


    public ArrayList<Account> getAccounts() {
        return accounts;
    }
    public ArrayList<Account> getAccountUser() {
        return accounts;
    }

    private Datasource<AccountList> datasource;

    @Override
    public String toString() {
        return "AccountList{" +
                "accounts=" + accounts +
                '}';
    }
}
