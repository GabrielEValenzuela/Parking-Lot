package com.concurrent.programming;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;

/**
 * Some methods for mathematical calculations like gcd and modulo.
 *
 * @author Manuel Gieseking
 */
public class MathTools {

    /**
     * Hidden constructor.
     */
    private MathTools() {
    }

    /**
     * Calculates the gcd of a given set of integers.
     *
     * @param set - the integers to calculate the gcd from.
     * @return the gcd of the integers of the given set.
     */
    public static int gcd(Collection<Integer> set) {
        if (set.isEmpty())
            return 0;
        Iterator<Integer> iter = set.iterator();
        int gcd = iter.next();
        while (iter.hasNext()) {
            gcd = gcd(gcd, iter.next());
            if (gcd == 1) return 1;
        }
        return gcd;
    }

    /**
     * Calculates the gcd of a given list of integers.
     *
     * @param set the integers to calculate the gcd from.
     * @return the gcd of the integers of the given set.
     */
    public static int gcd(int... set) {
        if (set.length == 0)
            return 0;
        int gcd = Math.abs(set[0]);
        for (int i = 1; i < set.length; i++)
            gcd = gcd(gcd, set[i]);
        return gcd;
    }

    /**
     * Calculates the gcd of two integers.
     *
     * @param a - first integer for calculating the gcd.
     * @param b - second integer for calculating the gcd.
     * @return the gcd of the two given integers.
     */
    public static int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (a != 0) {
            int tmp = a;
            a = b % a;
            b = tmp;
        }
        return b;
    }

    /**
     * Calculates the lcm of two numbers.
     *
     * @param a - first number for calculating the lcm.
     * @param b - second number for calculating the lcm.
     * @return the lcm of the two given numbers.
     */
    public static BigInteger lcm(BigInteger a, BigInteger b) {
        return a.divide(a.gcd(b)).multiply(b).abs();
    }

    /**
     * Calculates the lcm of two integers.
     *
     * @param a - first integer for calculating the lcm.
     * @param b - second integer for calculating the lcm.
     * @return the lcm of the two given integers.
     */
    public static int lcm(int a, int b) {
        return bigIntToInt(lcm(BigInteger.valueOf(a), BigInteger.valueOf(b)));
    }

    private static int bigIntToInt(BigInteger value) {
        if (value.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0 ||
                value.compareTo(BigInteger.valueOf(Integer.MIN_VALUE)) < 0)
            throw new ArithmeticException("Cannot represent value as int: " + value);
        return value.intValue();
    }

    /**
     * Calculates the mathematical modulo, so that it's given the positive value.
     *
     * @param a - divisor.
     * @param b - dividend.
     * @return a modulo b.
     */
    public static int mod(int a, int b) {
        return BigInteger.valueOf(a).mod(BigInteger.valueOf(b)).intValue();
    }

    /**
     * Calculate the average of two numbers, rounding towards minus infinity. This does the equivalent of
     * <pre>(x+y)&gt;&gt;1</pre>, but handles integer over- and underflow correctly.
     *
     * @param x First number
     * @param y Second number
     * @return The average.
     */
    public static int meanTowardsMinusInfinity(int x, int y) {
        return ((x ^ y) >> 1) + (x & y);
    }
}