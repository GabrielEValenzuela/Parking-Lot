package com.concurrent.programming;

import com.concurrent.programming.customexceptions.ConfigException;
import com.concurrent.programming.loaders.LoadPetriNetwork;

public class CheckConfiguration {
    /**
     * Check that all parameters of the Petri Network are validate
     * @param loader
     * @throws ConfigException
     */
    public CheckConfiguration(LoadPetriNetwork loader) throws ConfigException {
        int sizeTransitions;
        int sizePlaces;

        if (loader.I_IncidenceMatrix == null || loader.Sigma_MarkVector == null) {
            throw new ConfigException("It must exist a incidence matrix and a mark vector, sigma");
        }

        sizeTransitions = loader.I_IncidenceMatrix[0].length;
        sizePlaces = loader.I_IncidenceMatrix.length;

        if (ArithmeticCalculations.checkDimensionsAndMatrix(loader.I_IncidenceMatrix, sizePlaces, sizeTransitions)) {
            throw new ConfigException("Elements no consistent");
        }

        if (sizePlaces != loader.Sigma_MarkVector.length) {
            throw new ConfigException("Places must match with the incidence matrix");
        } else {
            for (int i = 0; i < loader.Sigma_MarkVector.length; i++) {
                if (loader.Sigma_MarkVector[i] < 0) {
                    throw new ConfigException("Only positive elements are allow");
                }
            }
        }

        if (loader.bounderMarkVector != null) {
            if (sizePlaces != loader.bounderMarkVector.length) {
                throw new ConfigException("Places must match with the incidence matrix");
            } else {
                for (int i = 0; i < loader.bounderMarkVector.length; i++) {
                    if (loader.bounderMarkVector[i] < 0) {
                        throw new ConfigException("Only positive elements are allow");
                    }
                }
            }
        }

        if (loader.H_IncidenceInhibitorArcsMatrix != null) {
            if (ArithmeticCalculations.checkDimensionsAndMatrix(loader.H_IncidenceInhibitorArcsMatrix, sizeTransitions, sizePlaces)) {
                throw new ConfigException("Elements no consistent");
            } else if (!ArithmeticCalculations.isBinary(loader.H_IncidenceInhibitorArcsMatrix)) {
                throw new ConfigException("Matrix it is not binary");
            }
        }

        if (loader.R_IncidenceReaderArcsMatrix != null) {
            if (ArithmeticCalculations.checkDimensionsAndMatrix(loader.R_IncidenceReaderArcsMatrix, sizeTransitions, sizePlaces)) {
                throw new ConfigException("Elements no consistent");
            } else if (!ArithmeticCalculations.isBinary(loader.R_IncidenceReaderArcsMatrix)) {
                throw new ConfigException("Matrix it is not binary");
            }
        }

        if (loader.Z_MatrixTemporal != null) {
            int dimension = loader.Z_MatrixTemporal.length;

            if (dimension != 2) {
                throw new ConfigException("The Z matrix must have only two columns. One for the initial time and the other for the final time.");
            } else if (loader.Z_MatrixTemporal[0].length != loader.Z_MatrixTemporal[1].length) {
                throw new ConfigException("The dimensions are not equals");
            } else {
                for (int i = 0; i < loader.Z_MatrixTemporal.length; i++) {
                    for (int j = 0; j < loader.Z_MatrixTemporal[0].length; j++) {
                        if (loader.Z_MatrixTemporal[i][j] < 0) {
                            throw new ConfigException("Only positive elements are allow");
                        }
                    }
                }
            }
        }
    }
}