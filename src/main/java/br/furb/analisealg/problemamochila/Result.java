package br.furb.analisealg.problemamochila;

import java.util.ArrayList;

public class Result {
    private int maxWeight;
    private ArrayList<Item> items;

    public Result(int maxWeight) {
        this.maxWeight = maxWeight;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(int maxWeight) {
        this.maxWeight = maxWeight;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
}
