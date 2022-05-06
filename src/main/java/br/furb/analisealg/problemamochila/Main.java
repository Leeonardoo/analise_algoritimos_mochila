package br.furb.analisealg.problemamochila;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Collectors;
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
            ArquivoLido arquivo = lerArquivo(n, file);
            int[] v = arquivo.getItems()[1];
            int[] w = arquivo.getItems()[0];

            System.out.println(mochilaRecursivo(n, v, w, arquivo.getW()));

            SolucaoMochila solucaoBottomUp = mochilaDinamicaBottomUp(n, v, w, arquivo.getW());
            System.out.println(solucaoBottomUp.getMaxWeight());
            System.out.println("Itens adicionados: ");
            String itensBottomUp = solucaoBottomUp.getItems()
                    .stream()
                    .map(Item::toString)
                    .collect(Collectors.joining("\n"));
            System.out.println(itensBottomUp);
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
    private static SolucaoMochila mochilaDinamicaBottomUp(int n, int[] v, int[] w, int W) {
        int[][] M = new int[n + 1][W + 1];
        int j, X;

        //-1 no j pois acessa os outros arrays que tem uma linha e coluna a menos
        for (j = 1; j <= n; j++) {
            for (X = 0; X <= W; X++) {
                if (w[j - 1] > X) {
                    M[j - 1][X] = M[j - 1][X];
                } else {
                    int usa = v[j - 1] + M[j - 1][X - w[j - 1]]; //valor de uma posição qualquer da linha –1
                    int naoUsa = M[j - 1][X]; //item da linha anterior
                    M[j][X] = Math.max(usa, naoUsa);
                }
            }
        }

        SolucaoMochila solucao = new SolucaoMochila(M[n][W]);
        ArrayList<Item> itensUsados = new ArrayList<>();

        //Montar a lista de cada item incluído
        int pesoMaximo = M[n][W];
        X = W;
        j = n;
        for (j = n; j > 0 && pesoMaximo > 0; j--) {
            if (pesoMaximo == M[j - 1][X]) {
                continue;
            } else {
                itensUsados.add(new Item(w[j - 1], v[j - 1]));
                pesoMaximo -= v[j - 1];
                X = X - w[j - 1];
            }
        }

        solucao.setItems(itensUsados);
        return solucao;
    }

    private static int getItemCount(File file) throws IOException {
        Path path = file.toPath();
        Stream<String> lines = Files.lines(path);
        int lineCount = (int) lines.count();
        lines.close();
        return lineCount;
    }

    private static ArquivoLido lerArquivo(int itemCount, File file) throws IOException {
        int[][] items = new int[2][itemCount];

        BufferedReader br = new BufferedReader(new FileReader(file));
        ArquivoLido arquivo = new ArquivoLido(Integer.parseInt(br.readLine()));
        int i = 0;

        String line;
        while ((line = br.readLine()) != null) {
            String[] values = line.split(" ");

            items[0][i] = Integer.parseInt(values[0]);
            items[1][i] = Integer.parseInt(values[1]);
            i++;
        }

        arquivo.setItems(items);
        br.close();
        return arquivo;
    }
}
