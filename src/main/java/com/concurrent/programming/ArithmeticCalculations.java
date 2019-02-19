package com.concurrent.programming;

import com.concurrent.programming.customexceptions.IllegalDimensionException;
import com.concurrent.programming.customexceptions.NullMatrixException;

public class ArithmeticCalculations {

    @SuppressWarnings("all")

    /**
     * Class use it to perform all arithmetic operations
     */

    /**
     * Multiplication of matrix by a vector
     * @param A
     * @param B
     * @param transpose
     * @return
     * @throws IllegalDimensionException
     * @throws NullMatrixException
     */

    public static int[] matrixByVector(int[][] A, int[] B, boolean transpose) throws IllegalDimensionException, NullMatrixException {
        if (A[0].length != B.length) {
            throw new IllegalDimensionException("The dimension of A and B are not equal, A:" + A[0].length + " B: " + B.length);
        }
        if (A == null || B == null) {
            throw new NullMatrixException("A or B is null");
        }
        int[] result = new int[A.length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                result[i] += B[j] * (transpose ? A[j][i] : A[i][j]);
            }

        }
        return result;
    }

    /**
     * Dot product of two vectors
     * @param x
     * @param y
     * @return
     */

    public static int dotProduct(int[] x, int[] y) {
        if (x.length != y.length) {
            throw new RuntimeException("Illegal vector dimensions (Dot product).");
        }
        int sum = 0;
        for (int i = 0; i < x.length; i++) {
            sum += x[i] * y[i];
        }
        return sum;
    }

    /**
     * Conjuntion of two vectors
     * @param x
     * @param y
     * @return
     */

    public static boolean[] vectorANDvector(boolean[] x, boolean[] y) {
        if (x.length != y.length) {
            throw new RuntimeException("Illegal vector dimensions.");
        }
        boolean[] AndAnd = new boolean[x.length];
        for (int i = 0; i < x.length; i++) {
            AndAnd[i] = y[i] && x[i];
        }
        return AndAnd;
    }

    /**
     * Check of dimensions
     * @param matrix
     * @param rows
     * @param columns
     * @return
     */

    public static boolean checkDimensionsAndMatrix(int[][] matrix, int rows, int columns) {
        for (int[] row : matrix) {
            rows--;
            if (columns != row.length) {
                return true;
            }
        }
        return rows != 0;
    }

    /**
     * Check of binary
     * @param matrix
     * @return
     */

    public static boolean isBinary(int[][] matrix) {
        for (int[] row : matrix) {
            for (int element : row) {
                if (element != 0 && element != 1)
                    return false;
            }
        }
        return true;
    }
}