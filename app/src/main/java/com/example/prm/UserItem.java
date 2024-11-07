package com.example.prm;

public class UserItem {
    private long uid;
    private String username;
    private String password;
    private int role;

    public UserItem(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserItem(String username, String password, int role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public UserItem(long uid, String username, String password, int role)  {
        this.uid = uid;
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
