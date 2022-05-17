package dk.easv.be;

public enum UserType {
    TEACHER(1, "Lærer"),
    ADMIN(2, "Admin"),
    STUDENT(3, "Elev");

    private final String guiTitle;
    private final int i;
    UserType(int i, String guiTitle) {
        this.guiTitle = guiTitle;
        this.i = i; }
    public int getI() {
        return i;
    }

    @Override
    public String toString() {
        return guiTitle;
    }
}
