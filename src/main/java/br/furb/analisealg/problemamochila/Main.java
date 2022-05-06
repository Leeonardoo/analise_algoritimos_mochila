package br.furb.analisealg.problemamochila;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Stream;

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
            int n = getItemCount(file);
            Mochila result = lerMochila(n, file);
            int[] v = result.getItems()[1];
            int[] w = result.getItems()[0];

            System.out.println(mochilaRecursivo(n, v, w, result.getW()));
            System.out.println(mochilaDinamicaBottomUp(n, v, w, result.getW()));
        }
    }

    /**
     * @param n item atual (começa em .length)
     * @param v Array de valores
     * @param w Array de pesos
     * @param W Capacidade da bolsa
     */
    private static int mochilaRecursivo(int n, int[] v, int[] w, int W) {
        if (n == 0 || W == 0)
            return 0;

        if (w[n - 1] > W) {
            return mochilaRecursivo(n - 1, v, w, W);
        } else {
            int usa = v[n - 1] + mochilaRecursivo(n - 1, v, w, W - w[n - 1]);
            int naoUsa = mochilaRecursivo(n - 1, v, w, W);

            return Math.max(usa, naoUsa);
        }
    }

    /**
     * @param n item atual (começa em .length)
     * @param v Array de valores
     * @param w Array de pesos
     * @param W Capacidade da bolsa
     */
    private static int mochilaDinamicaBottomUp(int n, int[] v, int[] w, int W) {
        int[][] M = new int[n + 1][W + 1];

        /* Já é inicializado com 0 no java
        for (int X = 0; X <= W; X++) {
            M[0][X] = 0;
        }

        for (int j = 0; j <= n; j++) {
            M[j][0] = 0;
        }*/

        //-1 no j pois acessa os outros arrays que tem uma linha e coluna a menos
        for (int j = 1; j <= n; j++) {
            for (int X = 0; X <= W; X++) {
                if (w[j - 1] > X) {
                    M[j - 1][X] = M[j - 1][X];
                } else {
                    int usa = v[j - 1] + M[j - 1][X - w[j - 1]]; //valor de uma posição qualquer da linha –1
                    int naoUsa = M[j - 1][X]; //item da linha anterior
                    M[j][X] = Math.max(usa, naoUsa);
                }
            }
        }

        return M[n][W];
    }

    private static int getItemCount(File file) throws IOException {
        Path path = file.toPath();
        Stream<String> lines = Files.lines(path);
        int lineCount  = (int) lines.count();
        lines.close();
        return lineCount;
    }

    private static Mochila lerMochila(int itemCount, File file) throws IOException {
        int[][] items = new int[2][itemCount];

        BufferedReader br = new BufferedReader(new FileReader(file));
        Mochila mochila = new Mochila(Integer.parseInt(br.readLine()));
        int i = 0;

        String line;
        while ((line = br.readLine()) != null) {
            String[] values = line.split(" ");

            items[0][i] = Integer.parseInt(values[0]);
            items[1][i] = Integer.parseInt(values[1]);
            i++;
        }

        mochila.setItems(items);
        br.close();
        return mochila;
    }
}
