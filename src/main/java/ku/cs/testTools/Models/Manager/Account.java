package ku.cs.testTools.Models.Manager;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.Getter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import ku.cs.testTools.Services.AccountComparable;
import lombok.Setter;

@Getter
@Data
@Entity
public class Account implements Comparable{
    private String role;
    @Setter
    private String name;
    @Setter
    private String username;
    private String password;
    private String img;
    private String date;

    public Account(String role, String name, String username, String password, String date) {
        this.role = role;
        this.name = name;
        this.username = username;
        this.password = password;
        this.img = "Profile-picture/Profile_1.jpg";
        this.date = date;
    }

    public void changeName(String name) {
        if (!name.trim().equals("")) {
            this.name = name.trim();
        }
    }
    public void changePassword(String password) {
        if (!name.trim().equals("")) {
            this.name = name.trim();
        }
    }

    public void setImagePath(String img) {
        this.img = img;
    }

    public void setLoginTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.date = now.format(dtf);
    }

    public boolean isUserName(String username) {
        return this.username.equals(username);
    }
    public boolean isUser() { return this.role.equals("User");}
    public boolean isAdmin(){
        return  this.role.equals("Admin");

    }

    public int compareTo(Account o) {
        return date.compareTo(o.getDate());
    }

    @Override
    public String toString() {
        return "role: '" + role + '\'' +
                ", name: '" + name + '\'' +
                ", username: '" + username + '\'' +
                ", date: '" + date + '\'' ;

    }


    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
