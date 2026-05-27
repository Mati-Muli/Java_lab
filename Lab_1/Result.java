package org.example;

import java.util.ArrayList;
import java.util.List;

public class Result {
    public List<Przedmiot> items;
    public int sumVal;
    public int sumWei;

    public Result() {
        this.items = new ArrayList<>();
        this.sumVal = 0;
        this.sumWei = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("-------\n");
        for (Przedmiot p : items) {
            sb.append(p.toString()).append("\n");
        }
        sb.append("Weight: ").append(sumWei).append("\n");
        sb.append("Value: ").append(sumVal);
        return sb.toString();
    }
}