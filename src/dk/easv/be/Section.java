package dk.easv.be;

import java.util.HashMap;

public class Section {
    int id;
    String sectionTitle;
    HashMap<Integer, String> problemidTitleMap;

    public Section(int id, String sectionTitle, HashMap<Integer, String> problemidTitleMap) {
        this.id = id;
        this.sectionTitle = sectionTitle;
        this.problemidTitleMap = problemidTitleMap;
    }

    public int getId() {
        return id;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public HashMap<Integer, String> getProblemidTitleMap() {
        return problemidTitleMap;
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", sectionTitle='" + sectionTitle + '\'' +
                ", problemidTitleMap=" + problemidTitleMap +
                '}';
    }
}
