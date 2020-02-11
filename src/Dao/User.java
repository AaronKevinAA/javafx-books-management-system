package Dao;

public class User {
    private String acc;
    private String pwd;
    private int role;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getCclass() {
        return cclass;
    }

    public void setCclass(int cclass) {
        this.cclass = cclass;
    }

    private String sex;
    private int grade;
    private int cclass;
    public String getAcc() {
        return acc;
    }

    public void setAcc(String acc) {
        this.acc = acc;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public boolean getStates() {
        return states;
    }

    public void setStates(boolean states) {
        this.states = states;
    }

    private boolean states;

}
