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
        //используем try с ресурсом для считывания данных их файла
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME_ATHLETES))) {
            Iterator<String> iterator = br.lines().iterator();
            //читаем, извлекаем данные из строк, переводим данные в класс Athlete для более удобной манипуляции данными
            while (iterator.hasNext()) {
                String[] currentLine = iterator.next().split(",");
                allAthletes.add(new Athlete(currentLine[0], currentLine[1]));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        //вызываем метод для перемешивания участников
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
        // сначала группируем по названию команды и тут же сортируем по количеству участников в команде
        List<List<Athlete>> sortByTeamNameAndSizeTeam = new ArrayList<>(allAthletes.stream()
                .collect(Collectors.groupingBy(Athlete::getTeamName)).values())
                .stream()
                .sorted(Comparator.comparing(List::size))
                .collect(Collectors.toList());

        int maxSize = sortByTeamNameAndSizeTeam.get(sortByTeamNameAndSizeTeam.size() - 1).size();
        //перемешиваем участников поочередно исходя их количества участников самой большой команды
        List<Athlete> result = new ArrayList<>();
        for (int i = 0; i < maxSize; i++) {
            for (List<Athlete> list : sortByTeamNameAndSizeTeam) {
                //участники сортируются если лимит не исчерпан
                if (list.size() > i) {
                    result.add(list.get(i));
                }
            }
        }
        // в случае если участников одной из команд существенно больше чем в других и в конце списка они идут подряд, то вызывается следующий метод
        if (result.get(result.size() - 1).getTeamName().equals(result.get(result.size() - 2).getTeamName())) {
            return changeList(result);
        }
        return result;
    }

    //метод пытается найти куда можно перераспределить участников
    public static List<Athlete> changeList(List<Athlete> athletesList) {
        List<Athlete> result = new ArrayList<>();
        for (int i = 0; i < athletesList.size() - 2; i++) {
            // определяем участника по текущему индексу, следующему и последнему
            Athlete current = athletesList.get(i);
            Athlete next = athletesList.get(i + 1);
            Athlete last = athletesList.get(athletesList.size() - 1);
            // если на текущем индексе i у всех троих разные команды - значит есть возможность перераспределить участников
            if (!current.getTeamName().equals(next.getTeamName()) &&
                !current.getTeamName().equals(last.getTeamName()) &&
                !next.getTeamName().equals(last.getTeamName())) {
                for (int j = 0; j < athletesList.size() - 1; j++) {
                    // в случае выполнения условия последний участник записывается на текущем индексе j
                    if (i == j - 1) {
                        result.add(last);
                    }
                    // при этом считывание данных продолжается далее
                    result.add(athletesList.get(j));
                }
                break;
            }
        }
        // если result пуст - значит свободных мест для распределения не нашлось и возвращается исходный список
        if (result.isEmpty()) {
            return athletesList;
        }
        // выполняется рекурсия если и далее в конце списка участники одной команды идут подряд
        if (result.get(result.size() - 1).getTeamName().equals(result.get(result.size() - 2).getTeamName())) {
            return changeList(result);
        }
        // возвращение результата удовлетворяющего всем требованиям
        return result;
    }
}
