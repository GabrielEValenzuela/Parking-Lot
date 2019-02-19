package com.concurrent.programming;

import com.concurrent.programming.customexceptions.NoPlaceInvariants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ComputeTInvariants {

    private Set<List<Integer>> result = new HashSet<>();
    private List<Integer> z = new ArrayList<>();
    private int[][] mat;

    /**
     * Deprecated class. It was originally idea it for calculate the places invariants
     * of a Petri Network using the Farkas algorithm.
     */

    public ComputeTInvariants(int[][] mat) {
        calcInvariantsFarkas(mat);
    }

    public Set<List<Integer>> calcInvariantsFarkas(int[][] mat) {
        this.mat = mat;
        int rows = mat.length;
        if (mat.length == 0 || mat[0].length == 0) {
            return new HashSet<>();
        }
        int cols = mat[0].length;
        int dcols = cols + rows;

        // initializes d as (C | E) with incidence matrix C and identity E
        // time: O(rows*(cols+rows))
        // place: O(rows*(cols+rows))
        List<List<Integer>> d = new ArrayList<>();
        for (int i = 0; i < rows; ++i) {
            List<Integer> row = new ArrayList<>();
            d.add(i, row);
            for (int j = 0; j < dcols; ++j) {
                if (j < cols) {
                    row.add(j, mat[i][j]);
                } else {
                    row.add(j, (i != j - cols) ? 0 : 1);
                }
            }
        }
        // for all columns (transitions)
        // time: O()
        // place: O()
        for (int i = 0; i < cols; ++i) {
            int offset = 1;
            // time: O()
            // place: O()
            do {
                rows = d.size();
                // for all pairs of rows in d
                // time: O()
                // place: O()
                for (int j1 = 0; j1 < rows - 1; ++j1) {
                    final List<Integer> z1 = d.get(j1);
                    for (int j2 = j1 + offset; j2 < rows; ++j2) {
                        //InterrupterRegistry.throwIfInterruptRequestedForCurrentThread();
                        final List<Integer> z2 = d.get(j2);
                        // check opposite signum at position i
                        if (Math.signum(z1.get(i)) * Math.signum(z2.get(i)) < 0) {
                            // z(i) = 0
                            final int z1abs = Math.abs(z1.get(i));
                            final int z2abs = Math.abs(z2.get(i));

                            // time: O(cols+rows)
                            // place: O(cols+rows)
                            for (int k = 0; k < dcols; ++k) {
                                int a = z2abs * z1.get(k);
                                int b = z1abs * z2.get(k);
                                z.add(k, a + b);
                                //z.add(k, z2abs * z1.get(k) + z1abs * z2.get(k));

                            }
                            // normalize z
                            final int gcd = MathTools.gcd(z);
                            if (gcd != 1) {
                                // time: O(cols+rows)
                                // place: O(1)
                                for (int k = 0; k < dcols; ++k) {
                                    z.set(k, z.get(k) / gcd);
                                }
                            }
                            d.add(z);
                        }
                    }
                }
                offset = rows;
                // check new added rows.
            } while (rows < d.size());
            // remove all rows z with z(i) != 0
            for (int j = d.size() - 1; j >= 0; --j) {
                if (d.get(j).get(i) != 0) {
                    d.remove(j);
                }
            }
        }

        // the result is at the right side of d (remove left transitions-count columns).
        // time: O()
        // place: O()
        for (List<Integer> z : d) {
            this.result.add(z.subList(cols, dcols));
        }
        return this.result;
    }

    public int[][] getTransitionsInvariants() throws NoPlaceInvariants {
        if (result == null) {
            throw new NoPlaceInvariants("No places");
        }
        Object[] obj = result.toArray();
        List<Integer> temp = new ArrayList<>();
        int[][] places = new int[obj.length][mat.length];
        for (Object t : obj) {
            temp = (List<Integer>) t;
        }
        for (int i = 0; i < obj.length; i++) {
            for (int j = 0; j < temp.size(); j++) {
                places[i][j] = temp.get(j);
            }
        }
        return places;
    }
}
