package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProblemTest {

    @Test
    public void OneItem() {
        Problem problem = new Problem(5, 12, 1, 10);
        Result result = problem.Solve(10);
        assertFalse(result.items.isEmpty(), "Plecak nie powinien być pusty!");
        assertTrue(result.items.size() >= 1);
    }

    @Test
    public void Empty() {
        Problem problem = new Problem(5, 45, 6, 10);
        Result result = problem.Solve(3);
        assertTrue(result.items.isEmpty(), "Żaden przedmiot nie powinien zmieścić się w plecaku!");
        assertEquals(0, result.sumWei, "Sumaryczna waga powinna wynosić 0!");
        assertEquals(0, result.sumVal, "Sumaryczna wartość powinna wynosić 0!");
    }

    @Test
    public void ItemBounds() {
        int lower = 3;
        int upper = 8;
        Problem problem = new Problem(20, 777, lower, upper);

        for (Przedmiot p : problem.przedmioty) {
            assertTrue(p.getWeight() >= lower && p.getWeight() <= upper,
                    "Waga przedmiotu o ID " + p.getId() + " wykracza poza zadeklarowany zakres!");
            assertTrue(p.getValue() >= lower && p.getValue() <= upper,
                    "Wartość przedmiotu o ID " + p.getId() + " wykracza poza zadeklarowany zakres!");
        }
    }


    @Test
    public void Predetermined() {
        Problem problem = new Problem(0, 1, 1, 10);
        problem.przedmioty.add(new Przedmiot(0, 12, 3));
        problem.przedmioty.add(new Przedmiot(1, 5, 2));
        Result result = problem.Solve(8);

        assertEquals(8, result.sumWei, "Obliczona sumaryczna waga plecaka jest błędna!");
        assertEquals(29, result.sumVal, "Obliczona sumaryczna wartość plecaka jest błędna!");
        assertEquals(3, result.items.size(), "Liczba przedmiotów wrzuconych do plecaka się nie zgadza!");
    }
}