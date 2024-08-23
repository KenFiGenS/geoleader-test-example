package org.example;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class App {

    public static final String FILE_NAME_ATHLETES = "athletes.txt";
    public static final String FILE_NAME_RESULT = "resultList.txt";

    public static void main(String[] args) {
        List<Athlete> allAthletes = new ArrayList<>();
        try(FileReader fr = new FileReader(FILE_NAME_ATHLETES)) {
            BufferedReader br = new BufferedReader(fr);
            Iterator iterator = br.lines().iterator();
            while (iterator.hasNext()) {
                String[] currentLine = iterator.next().toString().split(",");
                allAthletes.add(new Athlete(currentLine[0], currentLine[1]));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        List<Athlete> result = getResult(allAthletes);

        try ( BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME_RESULT))){
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

        int maxSize = sortByTeamNameAndSizeTeam.get(sortByTeamNameAndSizeTeam.size()-1).size();
        List<Athlete> result = new ArrayList<>();
        for (int i = 0; i < maxSize; i++) {
            for (List<Athlete> list : sortByTeamNameAndSizeTeam) {
                if (list.size() > i) {
                    result.add(list.get(i));
                }
            }
        }
        return result;
    }
}
