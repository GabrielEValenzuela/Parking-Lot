package com.concurrent.programming.loaders;

import com.concurrent.programming.ComputePInvariants;
import com.concurrent.programming.ComputeTInvariants;
import com.concurrent.programming.Log;
import com.concurrent.programming.customexceptions.NoPlaceInvariants;

public class LoadPetriNetwork implements Cloneable {
    public String title;
    public String absstract;
    public String description;
    public int[][] I_IncidenceMatrix;
    public int[][] H_IncidenceInhibitorArcsMatrix;
    public int[][] R_IncidenceReaderArcsMatrix;
    public int[] Sigma_MarkVector;
    public int[] bounderMarkVector;
    public int[][] invariantPlaceMatrix;
    public int[][] invariantTransitionMatrix;
    public long[][] Z_MatrixTemporal;
    public long[] timestampVector;
    public int[] sumInvariantPlaces;
    public int[][] matrixStatic;

    public LoadPetriNetwork clone() {
        Object object = null;
        try {
            object = super.clone();
        } catch (CloneNotSupportedException e) {
            Log.logSevere("CloneNotSupportedException " + e.toString());
            System.exit(-1);
        }
        return (LoadPetriNetwork) object;
    }

    public void loadTInvariants() throws NoPlaceInvariants {
        this.invariantTransitionMatrix = new ComputeTInvariants(this.I_IncidenceMatrix).getTransitionsInvariants();
        if (invariantTransitionMatrix != null) {
            for (int i = 0; i < invariantTransitionMatrix.length; i++) {
                for (int j = 0; j < invariantTransitionMatrix[0].length; j++) {
                    System.out.println(invariantTransitionMatrix[i][j]);
                }
                System.out.println("");
            }
        }

    }

    public void loadPInvariants() throws NoPlaceInvariants {
        this.invariantPlaceMatrix = new ComputePInvariants(this.I_IncidenceMatrix).getPlacesInvariantes();
        if (invariantPlaceMatrix != null) {
            for (int i = 0; i < invariantPlaceMatrix.length; i++) {
                for (int j = 0; j < invariantPlaceMatrix[0].length; j++) {
                    System.out.println(invariantPlaceMatrix[i][j]);
                }
                System.out.println("");
            }
        }
    }
}
