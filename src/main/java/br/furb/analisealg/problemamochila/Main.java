package br.furb.analisealg.problemamochila;

import com.bethecoder.ascii_table.ASCIITable;
import com.bethecoder.ascii_table.spec.IASCIITable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    private static final File inputDir = new File("input");

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void main(String[] args) throws Exception {
        inputDir.mkdirs();
        File[] inputFiles = inputDir.listFiles();
        ArrayList<Benchmark> benchmarks = new ArrayList<>();

        if (inputFiles == null || inputFiles.length == 0) {
            throw new IllegalStateException("Nenhum arquivo foi encontrado na pasta input (localizada em: " + inputDir.getAbsolutePath() + ").");
        }

        for (File file : inputFiles) {
            Benchmark benchmark = new Benchmark(file.getName());
            benchmarks.add(benchmark);
            System.out.println("Arquivo " + file.getName());

            //Inicializar arquivo
            int n = getItemCount(file);
            ArquivoLido arquivo = lerArquivo(n, file);
            int[] v = arquivo.getItems()[1];
            int[] w = arquivo.getItems()[0];

            System.out.println("Recursivo: ");
            ArrayList<Item> listaItensRecursivo = new ArrayList<>();
            long l1Recursivo = System.nanoTime();
            System.out.println(mochilaRecursivo(n, v, w, arquivo.getW(), listaItensRecursivo));
            long l2Recursivo = System.nanoTime();
            System.out.println("Itens adicionados: ");
            String itensRecursivo = listaItensRecursivo
                    .stream()
                    .map(Item::toString)
                    .collect(Collectors.joining("\n"));
            System.out.println(itensRecursivo + "\n");

            benchmark.setRecursiveDuration(l1Recursivo, l2Recursivo);

            System.out.println("\nBottomUp: ");
            long l1BottomUp = System.nanoTime();
            Result solucaoBottomUp = mochilaDinamicaBottomUp(n, v, w, arquivo.getW());
            long l2BottomUp = System.nanoTime();
            benchmark.setBottomUpDuration(l1BottomUp, l2BottomUp);

            //Resultado bottomUp
            System.out.println(solucaoBottomUp.getMaxWeight());
            System.out.println("Itens adicionados: ");
            String itensBottomUp = solucaoBottomUp.getItems()
                    .stream()
                    .map(Item::toString)
                    .collect(Collectors.joining("\n"));
            System.out.println(itensBottomUp + "\n\n");
        }

        String[] header = new String[]{"", "Método recursivo", "Método Bottom-up"};
        String[][] data = benchmarks.stream().map((item) -> new String[]{item.getFilename(), item.getRecursiveDuration(), item.getBottomUpDuration()}).toArray(String[][]::new);
        ASCIITable.getInstance().printTable(header, IASCIITable.ALIGN_CENTER, data, IASCIITable.ALIGN_RIGHT);
    }

    /**
     * @param n item atual (começa em .length)
     * @param v Array de valores
     * @param w Array de pesos
     * @param W Capacidade da bolsa
     */
    private static int mochilaRecursivo(int n, int[] v, int[] w, int W, List<Item> itensUsados) {
        if (n == 0 || W == 0) {
            return 0;
        }

        if (w[n - 1] > W) {
            List<Item> escolhaInicial = new ArrayList<>();
            int usa = mochilaRecursivo(n - 1, v, w, W, escolhaInicial);
            itensUsados.addAll(escolhaInicial);
            return usa;
        } else {
            List<Item> itensUsa = new ArrayList<>();
            List<Item> itensNaoUsa = new ArrayList<>();
            int usa = v[n - 1] + mochilaRecursivo(n - 1, v, w, W - w[n - 1], itensUsa);
            int naoUsa = mochilaRecursivo(n - 1, v, w, W, itensNaoUsa);
            if (usa > naoUsa) {
                itensUsados.addAll(itensUsa);
                itensUsados.add(new Item(w[n - 1], v[n - 1]));
                return usa;
            } else {
                itensUsados.addAll(itensNaoUsa);
                return naoUsa;
            }
        }
    }

    /**
     * @param n item atual (começa em .length)
     * @param v Array de valores
     * @param w Array de pesos
     * @param W Capacidade da bolsa
     */
    private static Result mochilaDinamicaBottomUp(int n, int[] v, int[] w, int W) {
        int j, X;
        int[][] M = new int[n + 1][W + 1];

        //-1 no j pois acessa os outros arrays que tem uma linha e coluna a menos
        for (j = 1; j <= n; j++) {
            for (X = 0; X <= W; X++) {
                if (w[j - 1] > X) {
                    M[j][X] = M[j - 1][X];
                } else {
                    int usa = v[j - 1] + M[j - 1][X - w[j - 1]];
                    int naoUsa = M[j - 1][X];
                    M[j][X] = Math.max(usa, naoUsa);
                }
            }
        }

        Result solucao = new Result(M[n][W]);
        ArrayList<Item> itensUsados = new ArrayList<>();

        //Montar a lista de cada item incluído
        int valorMaximo = M[n][W];
        X = W;
        for (j = n; j > 0 && valorMaximo > 0; j--) {
            if (valorMaximo != M[j - 1][X]) {
                itensUsados.add(new Item(w[j - 1], v[j - 1]));
                valorMaximo -= v[j - 1];
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
