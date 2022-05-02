package br.furb.analisealg.problemamochila;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    private static final File inputDir = new File("input");

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void main(String[] args) throws Exception {
        inputDir.mkdirs();
        File[] inputFiles = inputDir.listFiles();

        if (inputFiles == null || inputFiles.length == 0) {
            throw new IllegalStateException("Nenhum arquivo foi encontrado na pasta input (localizada em: " + inputDir.getAbsolutePath() + ").");
        }

        for (File file : inputFiles) {
            var result = loadFile(file);
        }
    }

    private static int[][] loadFile(File file) throws Exception {
        int[][] mArr;

        BufferedReader br = new BufferedReader(new FileReader(file));
        int capacity = Integer.parseInt(br.readLine());

        List<Item> items = br
                .lines()
                .map((line) -> {
                    String[] values = line.split(" ");
                    return new Item(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
                })
                .collect(Collectors.toList());

        mArr = new int[items.size()][capacity];

        return mArr;
    }
}
