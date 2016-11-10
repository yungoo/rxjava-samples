package com.rainbow.streams;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by Administrator on 2016/11/5.
 */
public class PrimeResolver {

    public static boolean isPrime(int number) {
        boolean divisble = false;

        for (int i = 2; i < number; i++) {
            if (number % i == 0) {
                divisble = true;
                break;
            }
        }

        return number > 1 && !divisble;
    }

    public static List<Double> sqrtOfFirst100Primes() {
        List<Double> sqrtOfFirst100Primes = new ArrayList<Double>();

        int index = 1;
        while (sqrtOfFirst100Primes.size() < 100) {
            if (isPrime(index)) {
                sqrtOfFirst100Primes.add(Math.sqrt(index));
            }
            index++;
        }

        return sqrtOfFirst100Primes;
    }

    public static boolean isPrimeNew(int number) {
        return number > 1 &&
                IntStream.range(2, number)
                .noneMatch(i -> number % i == 0);
    }

    public static List<Double>  sqrtOfFirstNPrimesNew(int n) {
        List<Double> topn =  Stream.iterate(1, e -> e + 1)
                .filter(PrimeResolver::isPrimeNew)
                .map(Math::sqrt)
                .limit(n)
                .collect(Collectors.toList());
        return topn;
    }

    public static void main(String[] args) {
//        List<Double> sqrtOfFirst100Primes = sqrtOfFirst100Primes();
        List<Double> sqrtOfFirst100Primes = sqrtOfFirstNPrimesNew(100);
        System.out.println(
                String.format("Computer %d values, first is %g, last is %g",
                        sqrtOfFirst100Primes.size(),
                        sqrtOfFirst100Primes.get(0),
                        sqrtOfFirst100Primes.get(sqrtOfFirst100Primes.size() - 1)));
    }

}
