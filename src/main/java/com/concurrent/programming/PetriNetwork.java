package com.concurrent.programming;

import com.concurrent.programming.customexceptions.*;
import com.concurrent.programming.loaders.LoadPetriNetwork;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


//@apiNote  Arrojaria exepcion si el vector esta vacio [0 0 0 ....] y otra distinta para null \
public class PetriNetwork {
    private LoadPetriNetwork loader;
    private CheckerPlacesInvariants checkP;

    /**
     * This class it's the manager of the Petri Network. It control the fire of the diferents transitions
     * without be worry about the Thread managment that it's, any protective mechanism are implemented. His central method, {@link #makeShot(int) make shot} is based
     * on the "Generalized state equation" development by the Dr. Orlando Milcolini and associates. For more information see:
     * <a href="https://www.researchgate.net/publication/329130744_Generalized_state_equation_for_non-autonomous_Petri_nets_with_different_types_of_arcs">Generalized state equation</a>
     * @param load Class with all configurations validate for the Petri Network
     * @throws ConfigException
     * @throws NoPlaceInvariants
     */
    @Contract("null -> fail") //IntelliJ property
    public PetriNetwork(LoadPetriNetwork load) throws ConfigException, NoPlaceInvariants {
        if (load == null) {
            throw new ConfigException("The network can not be created. Please check the file or path");
        }
        this.loader = load.clone();
        new CheckConfiguration(this.loader);

        if (this.isATemporalNetwork()) {
            this.loader.timestampVector = new long[this.loader.I_IncidenceMatrix[0].length];
            boolean[] SensitizedAuxiliar = sensitizedTransitions();
            long markTime = System.currentTimeMillis();
            for (int i = 0; i < this.loader.timestampVector.length; i++) {
                this.loader.timestampVector[i] = SensitizedAuxiliar[i] ? markTime : 0;
            }
        }

        this.checkP = new CheckerPlacesInvariants(this.loader.invariantPlaceMatrix, this.loader.sumInvariantPlaces);
        this.checkP.check(this.loader.Sigma_MarkVector);
    }

    /**
     *  The kernel of the operation with the Petri Network. The @param is the number of transition to fire, first
     *  is checked to see if it's a valid parameter.
     *  To fire a transition,the method check if the fire it's sensitized by inhibitors arcs, that is:
     *  B = H*Q
     *  After, check if the fire it's sensitized by readers arcs:
     *  R = L*W
     *  If the condition holds true, the next step is calculate the new vector mark and check
     *  its consistence. Later, if the network is not temporal, the sigma mark vector it's load by the new mark. Else
     *  the new sensitized parameters are load relying on the mark time take it at the begining of the calculations wich it's
     *  the elemnt store on the time stamp vector.
     *  Finally ckeck the invariants places and return the flag, validShot
     * @param transition
     * @return <code>True</code> if the fire can do.<code>False</code> in opposite direction.
     * @throws FiringException
     * @throws NoPlaceInvariants
     * @throws NullMatrixException
     * @throws IllegalDimensionException
     */

    public boolean makeShot(int transition) throws FiringException, NoPlaceInvariants, NullMatrixException, IllegalDimensionException {

        if (transition > this.getI_IncidenceMatrix()[0].length || transition < 1) {

            throw new FiringException("Can not make the shot,because transition does not exist or it's less than 1.");

        }
        int[] newMarkVector;
        boolean validShot = true;
        long markTimeFire = System.currentTimeMillis();
        if (this.isExtendedByInhibitorsArcs()) {
            validShot = validForInhibitors(transition);
        }
        if (validShot && this.isExtendedByReadersArcs()) {
            validShot = validForReaders(transition);
        }

        if (validShot && this.isATemporalNetwork()) {
            validShot = this.genSensitizedTemporal(markTimeFire)[transition - 1];
        }

        newMarkVector = validShot ? this.nextMark(transition) : null;
        validShot = newMarkVector != null && this.isValidMark(newMarkVector);

        if (validShot) {
            if (!this.isATemporalNetwork()) {
                this.loader.Sigma_MarkVector = newMarkVector;
            } else {
                boolean[] oldSensitized = getSensitizedArray(true);
                this.loader.Sigma_MarkVector = newMarkVector;
                boolean[] newSensitized = getSensitizedArray(true);
                for (int i = 0; i < newSensitized.length; i++) {
                    if (!oldSensitized[i] && newSensitized[i]) {
                        this.loader.timestampVector[i] = markTimeFire;
                    }
                }
            }

        }

        if (validShot) {
            this.checkP.check(this.loader.Sigma_MarkVector);
        }

        return validShot;
    }

    /**
     * Return the time in milliseconds remainding to make avaible the transition
     * in accordance with the temporal window. The elements store in the vector are:
     *  _ 0 on the case what it's not temporal
     *  _ -1 if alpha it's gone
     *  _ less to 0, remaining time
     * @return A vector of times
     * @throws NullMatrixException
     * @throws IllegalDimensionException
     */

    public long[] getWaitTime() throws NullMatrixException, IllegalDimensionException {
        long[] timesToWait = new long[this.loader.I_IncidenceMatrix[0].length];

        if (!this.isATemporalNetwork())
            return timesToWait;

        long markTime = System.currentTimeMillis();
        boolean[] sensitized = this.getSensitizedArray(true);
        long alphaTime; // Sensitized time
        for (int i = 0; i < timesToWait.length; i++) {
            /* Temporal window chek*/
            if (sensitized[i] && this.loader.Z_MatrixTemporal[0][i] != 0) {
                // Sensitized time
                alphaTime = (markTime - this.loader.timestampVector[i]);
                // If the sensitized time is less to alpha, calculate the remaining time.
                if (alphaTime < this.loader.Z_MatrixTemporal[0][i])
                    timesToWait[i] = this.loader.Z_MatrixTemporal[0][i] - alphaTime;
                else
                    timesToWait[i] = -1;
            }
        }
        return timesToWait;
    }

    /**
     * Return the specific time of a transition.
     * @param transition
     * @return Specifict time measure in milliseconds
     * @throws NullMatrixException
     * @throws IllegalDimensionException
     */

    public long getSpecificWaitTime(int transition) throws NullMatrixException, IllegalDimensionException {
        return (this.getWaitTime())[transition - 1];
    }

    /**
     * Return a boolean vector of transitions sensitized inside of the temporal windows
     * @param timestamp
     * @return <code>True</code> if the transition it's inside of the temporal window, <code>False</code> if itsn't
     */

    @Contract(pure = true)
    private boolean[] genSensitizedTemporal(long timestamp) {

        boolean[] sensitizedTransitions = new boolean[this.loader.I_IncidenceMatrix[0].length];

        for (int i = 0; i < sensitizedTransitions.length; i++) {
            sensitizedTransitions[i] = true;
            if (this.loader.Z_MatrixTemporal[0][i] != 0) {
                sensitizedTransitions[i] = this.loader.Z_MatrixTemporal[0][i] < (timestamp - this.loader.timestampVector[i]);
            }
            if (sensitizedTransitions[i] && this.loader.Z_MatrixTemporal[1][i] != 0) {
                sensitizedTransitions[i] = this.loader.Z_MatrixTemporal[1][i] > (timestamp - this.loader.timestampVector[i]);
            }
        }


        return sensitizedTransitions;
    }

    /**
     * Boundend check
     * @return <code>True</code> if it's a boundend network
     */

    @Contract(pure = true)
    private boolean isExtendedByMaximumTokens() {
        return (this.loader.bounderMarkVector != null);
    }

    /**
     * Inhibitor check
     * @return <code>True</code> if there is a H matrix
     */

    @Contract(pure = true)
    private boolean isExtendedByInhibitorsArcs() {
        return (this.loader.H_IncidenceInhibitorArcsMatrix != null);
    }

    /**
     * Reader check
     * @return <code>True</code> if there is a R matrix
     */

    @Contract(pure = true)
    private boolean isExtendedByReadersArcs() {
        return (this.loader.R_IncidenceReaderArcsMatrix != null);
    }

    /**
     * Temporal check
     * @return <code>True</code> if there is a Z matrix
     */

    @Contract(pure = true)
    private boolean isATemporalNetwork() {
        return (this.loader.Z_MatrixTemporal != null);
    }

    public boolean[] sensitizedTransitions() {
        boolean[] sensitized = new boolean[this.getI_IncidenceMatrix()[0].length];
        try {
            for (int i = 0; i < sensitized.length; i++) {
                sensitized[i] = this.isValidMark(this.nextMark(i + 1));
            }
        } catch (Exception e) {
        }
        return sensitized;
    }

    /**
     * Return a boolean vector of transitions sensitized, ignoring the temporal window
     * @return <code>True</code> if the transition it's sensitized
     * @throws NullMatrixException
     * @throws IllegalDimensionException
     */

    public boolean[] getSensitizedArray() throws NullMatrixException, IllegalDimensionException {
        return getSensitizedArray(false);
    }

    /**
     * Return a boolean vector of transitions sensitized
     * @param ignoreTime
     * @return <code>True</code> if the transition it's sensitized
     * @throws NullMatrixException
     * @throws IllegalDimensionException
     */
    private boolean[] getSensitizedArray(boolean ignoreTime) throws NullMatrixException, IllegalDimensionException {
        boolean[] sensitizedTransitions = new boolean[this.loader.I_IncidenceMatrix[0].length];

        try {
            for (int i = 0; i < sensitizedTransitions.length; i++)
                sensitizedTransitions[i] = this.isValidMark(this.nextMark(i + 1));
        } catch (FiringException e) {
            Log.logSevere("An inexplicable error happened");
        }


        /* B = H*Q */
        if (this.isExtendedByInhibitorsArcs()) {
            int[] notSensitizedB = ArithmeticCalculations.matrixByVector(this.loader.H_IncidenceInhibitorArcsMatrix, this.getVectorQorW(true), false);
            for (int i = 0; i < sensitizedTransitions.length; i++)
                sensitizedTransitions[i] &= (notSensitizedB[i] == 0); // If it's 0, it's sensitized
        }

        /* L = R*W */
        if (this.isExtendedByReadersArcs()) {
            int[] notSensitizedR = ArithmeticCalculations.matrixByVector(this.loader.H_IncidenceInhibitorArcsMatrix, this.getVectorQorW(false), false);
            for (int i = 0; i < sensitizedTransitions.length; i++)
                sensitizedTransitions[i] &= (notSensitizedR[i] == 0); // If it's 0, it's sensitized
        }

        /* Time sensitized */
        if (!ignoreTime && this.isATemporalNetwork()) {
            boolean[] sensitizedTemporal = this.genSensitizedTemporal(System.currentTimeMillis());
            for (int i = 0; i < sensitizedTransitions.length; i++)
                sensitizedTransitions[i] &= sensitizedTemporal[i]; // If it's 1, it's sensitized
        }

        return sensitizedTransitions;

    }

    /**
     * Check for inhibitors arcs
     * @param firing
     * @return <code>True</code> if a valid shot
     */

    private boolean validForInhibitors(int firing) {
        return (ArithmeticCalculations.dotProduct(getSpecificColumn(this.getH_IncidenceMatrix(), firing - 1), getVectorQorW(true)) == 0);
    }

    /**
     * Check for readers arcs
     * @param firing
     * @return <code>True</code> if a valid shot
     */

    private boolean validForReaders(int firing) {
        return (ArithmeticCalculations.dotProduct(getSpecificColumn(this.getR_IncidenceMatrix(), firing - 1), getVectorQorW(false)) == 0);
    }


    /**
     * Method used to get the Q or W vector
     * @param select If select if True, return the Q vector. Else, return the W vector
     * @return
     */
    @Contract(pure = true)
    private int[] getVectorQorW(boolean select) {
        int[] vector = new int[this.loader.Sigma_MarkVector.length];
        for (int i = 0; i < vector.length; i++) {
            vector[i] = select ? (this.loader.Sigma_MarkVector[i] == 0 ? 0 : 1) : (this.loader.Sigma_MarkVector[i] == 0 ? 1 : 0);
        }
        return vector;
    }

    /**
     * Check all the elements of the vector mark to see if anybody if less to 0.
     * @param vectorMark
     * @return True if the vector mark is valid. Else if any element is less to 0
     */

    private boolean isValidMark(@NotNull int[] vectorMark) {
        boolean valid = true;
        for (int i = 0; i < vectorMark.length; i++) {
            if (vectorMark[i] < 0) {
                valid = false;
                break;
            }
            if (this.isExtendedByMaximumTokens()) {
                if (this.getBounderMarkVector()[i] != 0 && vectorMark[i] > this.getBounderMarkVector()[i]) {
                    valid = false;
                    break;
                }
            }
        }
        return valid;
    }

    /**
     * Calculte the new mark of the network relying on the number of transition
     * @param transition
     * @return The new mark
     * @throws FiringException
     */


    private int[] nextMark(int transition) throws FiringException {
        if (transition > this.getI_IncidenceMatrix()[0].length || transition < 0) {
            throw new FiringException("Can not make the shot,because transition does not exist or it's less than 0.");
        }

        int[] fireVector = new int[this.getI_IncidenceMatrix()[0].length];
        fireVector[transition - 1] = 1;

        int[] newMark = new int[0];
        try {
            newMark = ArithmeticCalculations.matrixByVector(this.getI_IncidenceMatrix(), fireVector, false);
        } catch (IllegalDimensionException e) {
            e.printStackTrace();
        } catch (NullMatrixException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < newMark.length; i++) {
            newMark[i] += this.loader.Sigma_MarkVector[i];
        }
        return newMark;
    }

    /* *************************************************************************************************
       *                                                                                               *
       *                                    Getters                                                    *
       *                                                                                               *
       * ***********************************************************************************************
     */

    public int[][] getI_IncidenceMatrix() {
        return this.loader.I_IncidenceMatrix.clone();
    }

    public int[] getBounderMarkVector() {
        return this.loader.bounderMarkVector.clone();
    }


    public int[][] getH_IncidenceMatrix() {
        return this.isExtendedByInhibitorsArcs() ? this.loader.H_IncidenceInhibitorArcsMatrix.clone() : null;
    }

    public int[][] getR_IncidenceMatrix() {
        return this.isExtendedByReadersArcs() ? this.loader.R_IncidenceReaderArcsMatrix.clone() : null;
    }

    public int[][] getTransitionsInvariantsMatrix() {
        return (loader.invariantTransitionMatrix != null) ? this.loader.invariantTransitionMatrix.clone() : null;
    }

    public int[][] getPlacesInvariantsMatrix() {
        return (loader.invariantPlaceMatrix != null) ? this.loader.invariantPlaceMatrix.clone() : null;
    }

    public int[] getSigma_MarkVector() {
        return this.loader.Sigma_MarkVector.clone();
    }

    public int[] getSpecificColumn(int[][] mat, int index) {
        int[] column = new int[mat[0].length];
        for (int i = 0; i < column.length; i++) {
            column[i] = mat[index][i];
        }
        return column;
    }

}