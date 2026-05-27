package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Problem {
    private int n;
    private long seed;
    private int lowerBound;
    private int upperBound;
    private int sumVal;
    private int sumWeight;

    public List<Przedmiot> przedmioty;

    public Problem(int n, long seed, int lowerBound, int upperBound) {
        this.n = n;
        this.seed = seed;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.przedmioty = new ArrayList<>();
        this.sumVal = 0;
        this.sumWeight = 0;

        Random rand = new Random(seed);
        int zakres = (upperBound - lowerBound) + 1;

        for (int i = 0; i < n; i++) {
            int waga = rand.nextInt(zakres) + lowerBound;
            int wartosc = rand.nextInt(zakres) + lowerBound;

            przedmioty.add(new Przedmiot(i, wartosc, waga));

            this.sumVal += wartosc;
            this.sumWeight += waga;
        }
    }

    public Result Solve(int capacity) {
        Result result = new Result();
        int currentCapacity = capacity;

        List<Przedmiot> sorted = new ArrayList<>(this.przedmioty);
        sorted.sort((p1, p2) -> {
            double ratio1 = (double) p1.getValue() / p1.getWeight();
            double ratio2 = (double) p2.getValue() / p2.getWeight();
            return Double.compare(ratio2, ratio1);
        });

        for (Przedmiot p : sorted) {
            while (currentCapacity >= p.getWeight()) {
                result.items.add(p);
                result.sumWei += p.getWeight();
                result.sumVal += p.getValue();
                currentCapacity -= p.getWeight();
            }
            if (currentCapacity == 0) {
                break;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Przedmiot p : przedmioty) {
            sb.append(p.toString()).append("\n");
        }
        sb.append("--------------------------------\n");
        sb.append("Suma wag wszystkich przedmiotów: ").append(sumWeight).append("\n");
        sb.append("Suma wartości wszystkich przedmiotów: ").append(sumVal).append("\n");
        return sb.toString();
    }
}