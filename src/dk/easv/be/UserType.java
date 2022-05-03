package dk.easv.be;

public enum UserType {
    TEACHER(1),
    ADMIN(2),
    STUDENT(3);

    private final int i;
    UserType(int i) { this.i = i; }
    public int getI() {
        return i;
    }
}
