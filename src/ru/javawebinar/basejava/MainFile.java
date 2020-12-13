package ru.javawebinar.basejava;

import java.io.File;
import java.io.IOException;

public class MainFile {

    public static void main(String[] args) {
        String filePath = "./";

        File file = new File(filePath);
        try {
            System.out.println(file.getCanonicalPath());
        } catch (IOException e) {
            throw new RuntimeException("Error", e);
        }

        File dir = new File(filePath);
        getAll(dir, 0);
    }

    private static String pad(String str, int size, char padChar) {
        StringBuilder padded = new StringBuilder(str);
        while (padded.length() < size) {
            padded.append(padChar);
        }
        return padded.toString();
    }

    private static void getAll(File dir, int level) {
        String margin = pad("", level * 2, ' ');
        if (dir.isDirectory()) {
            System.out.println(margin.concat("+ ".concat(dir.getName())));
            File[] list = dir.listFiles();
            if (list != null) {
                for (File file : list) {
                    getAll(file, level + 1);
                }
            }
        } else {
            System.out.println(margin.concat("- ".concat(dir.getName())));
        }
    }

}
