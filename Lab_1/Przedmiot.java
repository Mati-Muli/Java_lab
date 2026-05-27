package org.example;

public class Przedmiot {
    private int id;
    private int value;
    private int weight;

    public Przedmiot(int id, int wartosc, int waga) {
        this.id = id;
        this.value = wartosc;
        this.weight = waga;
    }

    public int getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "No: " + id + " v: " + value + " w: " + weight;
    }
}