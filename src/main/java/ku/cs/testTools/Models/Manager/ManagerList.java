package ku.cs.testTools.Models.Manager;

import jakarta.persistence.Entity;
import ku.cs.testTools.Services.DataSource;
import lombok.Data;

import java.util.*;
@Data
public class ManagerList {
    private ArrayList<Manager> managers;


    public ManagerList() {
        managers = new ArrayList<>();
    }
    public void addNewAccount(String role, String name, String username, String password, String img, String date) {
        role = role.trim();
        name = name.trim();
        password = password.trim();
        img = img.trim();
        date = date.trim();
        if (!username.equals("") && !name.equals("")) {
            Manager exist = findAccountByUserName(username);
            if (exist == null) {
                managers.add(new Manager(role.trim(), name.trim(), username.trim(), password.trim(), date.trim()));
            }
        }
    }
    public void addAccount(Manager manager) {
        managers.add(manager);
    }





    public Manager findAccountByUserName(String username) {
        for (Manager manager : managers) {
            if (manager.isUserName(username) ) {
                return manager;
            }
        }
        return null;
    }
    public void sort(){
        Collections.sort(managers);
    }

    public void sort(Comparator<Manager> cmp){
        Collections.sort(managers, cmp);
    }


    public ArrayList<Manager> getManagers() {
        return managers;
    }
    public ArrayList<Manager> getAccountUser() {
        return managers;
    }

    private DataSource<ManagerList> datasource;

    @Override
    public String toString() {
        return "AccountList{" +
                "accounts=" + managers +
                '}';
    }
}
