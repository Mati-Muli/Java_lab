package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int lowerBound = 1;
        int upperBound = 10;

        System.out.println("Give number of items:");
        int n = scanner.nextInt();

        System.out.println("Give seed:");
        long seed = scanner.nextLong();

        System.out.println("Give knapsack capacity:");
        int capacity = scanner.nextInt();

        System.out.println("--------------------------------");

        Problem problem = new Problem(n, seed, lowerBound, upperBound);
        System.out.print(problem.toString());

        Result wynik = problem.Solve(capacity);
        System.out.println(wynik.toString());

        scanner.close();
    }
}