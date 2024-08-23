package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class App {

    public static final String FILE_NAME_ATHLETES = "athletes.txt";
    public static final String FILE_NAME_RESULT = "resultList.txt";

    public static void main(String[] args) {
        List<Athlete> allAthletes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME_ATHLETES))) {
            Iterator<String> iterator = br.lines().iterator();
            while (iterator.hasNext()) {
                String[] currentLine = iterator.next().split(",");
                allAthletes.add(new Athlete(currentLine[0], currentLine[1]));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        List<Athlete> result = getResult(allAthletes);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME_RESULT))) {
            for (Athlete athlete : result) {
                bw.write(athlete + "\n");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<Athlete> getResult(List<Athlete> allAthletes) {
        List<List<Athlete>> sortByTeamNameAndSizeTeam = new ArrayList<>(allAthletes.stream()
                .collect(Collectors.groupingBy(Athlete::getTeamName)).values())
                .stream()
                .sorted(Comparator.comparing(List::size))
                .collect(Collectors.toList());

        int maxSize = sortByTeamNameAndSizeTeam.get(sortByTeamNameAndSizeTeam.size() - 1).size();
        List<Athlete> result = new ArrayList<>();
        for (int i = 0; i < maxSize; i++) {
            for (List<Athlete> list : sortByTeamNameAndSizeTeam) {
                if (list.size() > i) {
                    result.add(list.get(i));
                }
            }
        }
        if (result.get(result.size() - 1).getTeamName().equals(result.get(result.size() - 2).getTeamName())) {
            return changeList(result);
        }
        return result;
    }

    public static List<Athlete> changeList(List<Athlete> athletesList) {
        List<Athlete> result = new ArrayList<>();
        for (int i = 0; i < athletesList.size() - 2; i++) {
            Athlete current = athletesList.get(i);
            Athlete next = athletesList.get(i + 1);
            Athlete last = athletesList.get(athletesList.size() - 1);
            if (!current.getTeamName().equals(next.getTeamName()) &&
                    !current.getTeamName().equals(last.getTeamName()) &&
                    !next.getTeamName().equals(last.getTeamName())) {
                for (int j = 0; j < athletesList.size() - 1; j++) {
                    if (i == j - 1) {
                        result.add(last);
                    }
                    result.add(athletesList.get(j));
                }
                break;
            }
        }
        if (result.get(result.size() - 1).getTeamName().equals(result.get(result.size() - 2).getTeamName())) {
            return changeList(result);
        }
        return result;
    }
}
