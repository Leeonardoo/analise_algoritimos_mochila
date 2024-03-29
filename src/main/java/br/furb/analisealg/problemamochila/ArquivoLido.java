package br.furb.analisealg.problemamochila;

public class ArquivoLido {
    private int W;
    private int[][] items;

    public ArquivoLido(int W, int[][] items) {
        this.W = W;
        this.items = items;
    }

    public ArquivoLido(int W) {
        this.W = W;
    }

    public int getW() {
        return W;
    }

    public void setW(int W) {
        this.W = W;
    }

    public int[][] getItems() {
        return items;
    }

    public void setItems(int[][] items) {
        this.items = items;
    }
}
