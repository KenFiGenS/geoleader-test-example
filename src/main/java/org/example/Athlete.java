package org.example;

public class Athlete {
    private String name;
    private String teamName;

    public Athlete(String name, String teaName) {
        this.name = name;
        this.teamName = teaName;
    }

    public String getName() {
        return name;
    }

    public String getTeamName() {
        return teamName;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return name + "," + teamName;
    }
}
