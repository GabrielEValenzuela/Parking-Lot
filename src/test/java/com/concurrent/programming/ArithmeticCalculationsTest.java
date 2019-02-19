package com.concurrent.programming;

import com.concurrent.programming.customexceptions.IllegalDimensionException;
import com.concurrent.programming.customexceptions.NullMatrixException;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArithmeticCalculationsTest {

    @Test
    public void matrixByVector() {
        boolean pass = false;
        try {
            assertArrayEquals(new int[]{10, 9}, ArithmeticCalculations.matrixByVector(new int[][]{{1, 2}, {3, -1}}, new int[]{4, 3}, false));
            assertArrayEquals(new int[]{9, -13}, ArithmeticCalculations.matrixByVector(new int[][]{{1, 1, 1}, {-2, 1, -3}}, new int[]{2, 3, 4}, false));
            assertArrayEquals(new int[]{9, -13}, ArithmeticCalculations.matrixByVector(new int[][]{{1, 1, 1}, {-2, 1, -3}}, new int[]{2, 3, 4, 5}, false));
            assertArrayEquals(new int[]{9, -13}, ArithmeticCalculations.matrixByVector(new int[][]{{1, 1, 1}, {-2, 1, -3}}, null, false));

        } catch (IllegalDimensionException e) {
            pass = true;
        } catch (NullMatrixException e) {
            pass = true;
        }
        assertTrue(pass);

    }

    @Test
    public void dotProduct() {
        boolean pass = false;
        assertEquals(10, ArithmeticCalculations.dotProduct(new int[]{1, 2}, new int[]{4, 3}));
        assertEquals(9, ArithmeticCalculations.dotProduct(new int[]{1, 1, 1}, new int[]{2, 3, 4}));
        assertEquals(0, ArithmeticCalculations.dotProduct(new int[]{1, -1, 2, 3}, new int[]{1, 1, -3, 2}));
        try {
            ArithmeticCalculations.dotProduct(null, null);
        } catch (Exception e) {
            pass = true;
        }
        assertTrue(pass);
    }

    @Test
    public void transposeMatrix() {
    }

    @Test
    public void vectorANDvector() {
    }
}