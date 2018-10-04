package me.dangoslen.pathz.models;

public class TeamMate {
    private String name;
    private String phoneNumber;
    private String handle;

    private TeamMate() { }

    public TeamMate(String name, String phoneNumber, String handle) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.handle = handle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

}
