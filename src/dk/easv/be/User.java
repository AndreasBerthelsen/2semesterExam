package dk.easv.be;

public class User {
    private int id;
    private String firstname;
    private String lastname;
    private String username;
    private UserType type;
    private int schoolID;
    private School school;

    public User(int id, String firstname, String lastname, String username, UserType type, int schoolID) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.type = type;
        this.schoolID = schoolID;
    }

    public User(int id, String firstname, String lastname, UserType type) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getSchoolID() {
        return school.getId();
    }


    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", username='" + username + '\'' +
                ", type=" + type +
                '}';
    }
}
