package ru.javawebinar.basejava;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainStream {

    public static void main(String[] args) {
        System.out.println(minValue(new int[]{1, 2, 3, 3, 2, 3}));
        System.out.println(minValue(new int[]{9, 8}));
        System.out.println(oddOrEven(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 6, 5, 0))));
        System.out.println(oddOrEven(new ArrayList<>(Arrays.asList(9,8))));
    }

    private static int minValue(int[] values) {

        return Arrays.stream(values)
                .distinct()
                .sorted()
                .reduce(0, (a, b) -> a * 10 + b);
    }

    private static List<Integer> oddOrEven(List<Integer> integers) {
        int sum = integers.stream().mapToInt(a -> a).sum();
        return integers.stream().mapToInt(a -> a)
                .filter((value) -> value % 2 != sum % 2)
                .boxed()
                .collect(Collectors.toList());
    }

}