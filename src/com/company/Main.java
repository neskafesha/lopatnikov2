package com.company;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main { // OMG

    private static String enterString;
    private static Scanner in;

    private static final String sum = "+";
    private static final String sub = "-";
    private static final String mult = "*";
    private static final String div = "/";
    private static final String left = "(";
    private static final String right = ")";

    public static void main(String[] args) {
        in = new Scanner(System.in);
        System.out.println("Введите выражение:");
        enterString = in.nextLine();
        checkStringEmpty();
    }

    private static void checkStringEmpty() {
        if (enterString.isEmpty()) {
            System.out.println("Ошибка! Вы ввели пустую строку.");
        } else {
            System.out.println("Ответ: " + stringPrepare());
            System.out.println("\nВведите выражение:");
            enterString = in.nextLine();
            checkStringEmpty();
        }
    }

    private static String stringPrepare() {
        enterString = enterString.trim();

        ArrayList<String> listSymbols = new ArrayList<>();
        Pattern pattern = Pattern.compile("[+]|-|[*]|/|[(][)]|[(]|[)]|[0-9]+|^");
        Matcher matcher = pattern.matcher(enterString);
        while (matcher.find()) {
            listSymbols.add(matcher.group());
        }

        enterString = searchValue(listSymbols);
        return listSymbols.get(0);
    }

    private static String searchValue(ArrayList<String> listSymbols) {
        for (int elementIndex = 0; elementIndex < listSymbols.size(); elementIndex++) {
            String value = listSymbols.get(elementIndex);

            enterString = listSymbols.stream().map(String::valueOf).collect(Collectors.joining()); //Преобразование списка символов в строку

            if (value.equals(left)) {
                System.out.println(listSymbols);
                if (listSymbols.get(elementIndex + 1).equals(left)) continue;
                if (listSymbols.size() == 1) return listSymbols.get(0);
                int secondRight = listSymbols.indexOf(right);
                int diff = secondRight - elementIndex;
                ArrayList<String> listItems = new ArrayList<>();
                for (int i = elementIndex + 1; i < secondRight; i++) {
                        listItems.add(listSymbols.get(i));
                }



                String valueS = searchValue(listItems);
                listSymbols.set(elementIndex, valueS);
                for (int i = 0; i < diff; i++) listSymbols.remove(elementIndex + 1);
            }
                if ((value.equals(mult)) && (!listSymbols.contains(left))) {
                    calcList(listSymbols, elementIndex, mult);
                } else if ((value.equals(div)) && (!listSymbols.contains(left))) {
                    calcList(listSymbols, elementIndex, div);
                } else if (value.equals(sum) && !(listSymbols.contains(mult) || listSymbols.contains(div)) && (!listSymbols.contains(left))) {
                    calcList(listSymbols, elementIndex, sum);
                } else if (value.equals(sub) && !(listSymbols.contains(mult) || listSymbols.contains(div)) && (!listSymbols.contains(left))) {
                    calcList(listSymbols, elementIndex, sub);
                }
        }
        if (listSymbols.size() > 1){
            searchValue(listSymbols);
        }else {
            return enterString;
        }
        return enterString;
    }

    private static void calcList(ArrayList<String> listSymbols, int elementIndex, String action) {
        listSymbols.set(elementIndex - 1, String.valueOf(calculateValue(action, listSymbols, elementIndex)));
        listSymbols.remove(elementIndex);
        listSymbols.remove(elementIndex);
        searchValue(listSymbols);
    }

    private static double calculateValue(String operation, ArrayList<String> listSymbol, int index) {
        double previous = Double.parseDouble(listSymbol.get(index - 1));
        double next = Double.parseDouble(listSymbol.get(index + 1));
        switch (operation) {
            case sum: return previous + next;
            case sub: return previous - next;
            case mult: return previous * next;
            case div: return previous / next;
        }
        return previous;
    }
}
