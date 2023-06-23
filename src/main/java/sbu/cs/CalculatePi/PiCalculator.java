package sbu.cs.CalculatePi;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PiCalculator {

    /**
     * Calculate pi and represent it as a BigDecimal object with the given floating point number (digits after . )
     * There are several algorithms designed for calculating pi, it's up to you to decide which one to implement.
     Experiment with different algorithms to find accurate results.

     * You must design a multithreaded program to calculate pi. Creating a thread pool is recommended.
     * Create as many classes and threads as you need.
     * Your code must pass all of the test cases provided in the test folder.

     * @param floatingPoint the exact number of digits after the floating point
     * @return pi in string format (the string representation of the BigDecimal object)
     */

    private static final int THREAD_COUNT = 10;

    public String calculate(int floatingPoint)
    {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        final BigDecimal[] pi = {BigDecimal.ZERO};

        for (int k = 0; k < 100; k++) {
            int finalK = k;
            executorService.execute(() -> {
                BigDecimal term = BigDecimal.valueOf(0);
                int scale = floatingPoint + 10;
                int iterationLimit = finalK == 0 ? 1 : (int) Math.pow(2, finalK + 1);

                BigDecimal piThread = pi[0];

                for (int i = 0; i < iterationLimit; i++) {
                    BigDecimal numerator = BigDecimal.valueOf(4).pow(finalK).multiply(BigDecimal.valueOf(8 * i + 1))
                            .subtract(BigDecimal.valueOf(2).multiply(BigDecimal.valueOf(4 * i + 2)).multiply(BigDecimal.valueOf(2).pow(finalK)))
                            .subtract(BigDecimal.valueOf(1).multiply(BigDecimal.valueOf(8 * i + 4)).multiply(BigDecimal.valueOf(2).pow(2 * finalK)))
                            .subtract(BigDecimal.valueOf(1).multiply(BigDecimal.valueOf(8 * i + 5)).multiply(BigDecimal.valueOf(2).pow(finalK)))
                            .subtract(BigDecimal.valueOf(1).multiply(BigDecimal.valueOf(8 * i + 6)).multiply(BigDecimal.valueOf(2).pow(finalK)));
                    BigDecimal denominator = BigDecimal.valueOf(16).pow(finalK).multiply(BigDecimal.valueOf(i).add(BigDecimal.valueOf(1)))
                            .multiply(BigDecimal.valueOf(8).multiply(BigDecimal.valueOf(i)).add(BigDecimal.valueOf(7)));
                    term = term.add(numerator.divide(denominator, scale, BigDecimal.ROUND_HALF_UP));
                }

                piThread = piThread.add(term);
                pi[0] = piThread;
            });
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return pi[0].setScale(floatingPoint, BigDecimal.ROUND_HALF_UP).toString();
    }

    public static void main(String[] args) {
        // Create a new instance of PiCalculator
        PiCalculator piCalculator = new PiCalculator();

        // Call the calculate method with the desired number of floating point digits
        String pi = piCalculator.calculate(100);

        // Print the result
        System.out.println(pi);
    }
}
