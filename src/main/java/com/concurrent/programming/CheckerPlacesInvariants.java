package com.concurrent.programming;

import com.concurrent.programming.customexceptions.NoPlaceInvariants;

public class CheckerPlacesInvariants {
    /**
     * Matriz de invariantes de plaza, cada fila representa un conjunto de invariantes de plaza
     * cada columnas en cada fila representa el numero de plaza
     * la matriz es de dimencion NxM Donde N es la cantidad de invariantes de plaza y M es la cantidad maxima
     * del los invariantes de plaza
     */
    private final int[][] matrixPInvariant;
    /**
     * De dimencion N, posee el valor que debe poseer el invariante de plaza
     */
    private final int[] invariantP;

    /**
     * Chequeador de invariantes de plaza
     *
     * @param matrixPInvariant Matriz de invariantes de plaza (Dimencion NxM)
     * @param invariantP       Posee el valor que debe poseer el invariante de plaza (Dimencion N)
     */
    CheckerPlacesInvariants(int[][] matrixPInvariant, int[] invariantP) {
        this.matrixPInvariant = matrixPInvariant;
        this.invariantP = invariantP;
    }

    /**
     * Comprueba si la marca cumple con todos los invariantes de plaza
     *
     * @param mark del sistema
     * @throws NoPlaceInvariants Si no cumple con los invariantes de plaza
     */
    void check(int[] mark) throws NoPlaceInvariants {
        // Si no hay invariantes de plaza retorno true
        if (this.matrixPInvariant == null || this.invariantP == null)
            return;

        boolean invalid = false;

        // vector de resultados de las suma de cada invariante
        int[] resInv = new int[matrixPInvariant.length]; // |matrixPInvariant| == |invariantP|

        for (int i = 0; i < matrixPInvariant.length; i++) {
            resInv[i] = ArithmeticCalculations.dotProduct(matrixPInvariant[i], mark);
            if (resInv[i] != this.invariantP[i]) invalid = true;
        }

        if (invalid) throw new NoPlaceInvariants("Error");

    }
}
