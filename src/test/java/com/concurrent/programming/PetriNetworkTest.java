package com.concurrent.programming;

import com.concurrent.programming.customexceptions.FiringException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("all")
public class PetriNetworkTest {
    PetriNetwork petriTest = new PetriNetwork();

    @Before
    public void before() {

        petriTest.setTitle("Test");
        petriTest.setDescription("TestBaseExampleDavid&Alla");
        petriTest.setAbsstract("pg51");
        petriTest.setSigma_MarkVector(new int[]{2, 0, 0, 1});
        petriTest.setI_IncidenceMatrix(new int[][]{{1, 0, -2}, {-1, 0, 3}, {0, -2, 2}, {0, 1, -1}});
        petriTest.setTInvariants();
        petriTest.setPInvariants();
        petriTest.setBounderMarkVector(new int[]{5, 5, 5, 5});
        petriTest.setH_IncidenceInhibitorArcsMatrix(new int[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 1, 0}});
        petriTest.setR_IncidenceReaderArcsMatrix(new int[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 1}});
    }

    @Test
    public void getInvariantPlaceMatrix() {
        assertArrayEquals(new int[]{0, 0, 1, 2}, petriTest.getPlacesInvariants()[0]);
    }


    @Test
    public void makeShot() throws FiringException {
        assertTrue(!petriTest.makeShot(1));
        assertTrue(!petriTest.makeShot(2));
        assertTrue(petriTest.makeShot(3));
    }

    @Test
    public void sensitizedTransitions() {
        assertArrayEquals(new boolean[]{false, false, true}, petriTest.SensitizedTransitions());
    }

    @Test
    public void getTitle() {
        assertEquals("Test", petriTest.getTitle());
    }

    @Test
    public void getAbsstract() {
        assertEquals("pg51", petriTest.getAbsstract());
    }

    @Test
    public void getDescription() {
        assertEquals("TestBaseExampleDavid&Alla", petriTest.getDescription());
    }

    @Test
    public void getI_IncidenceMatrix() {
        assertArrayEquals(new int[][]{{1, 0, -2}, {-1, 0, 3}, {0, -2, 2}, {0, 1, -1}}, petriTest.getI_IncidenceMatrix());
    }

    @Test
    public void getH_IncidenceInhibitorArcsMatrix() {
        assertArrayEquals(new int[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 1, 0}}, petriTest.getH_IncidenceInhibitorArcsMatrix());
    }

    @Test
    public void getR_IncidenceReaderArcsMatrix() {
        assertArrayEquals(new int[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 1}}, petriTest.getR_IncidenceReaderArcsMatrix());
    }

    @Test
    public void getSigma_MarkVector() throws FiringException {
        assertArrayEquals(new int[]{2, 0, 0, 1}, petriTest.getSigma_MarkVector());
        this.makeShot();
        assertArrayEquals(new int[]{0, 3, 2, 0}, petriTest.getSigma_MarkVector());
    }

    @Test
    public void getBounderMarkVector() {

        assertArrayEquals(new int[]{5, 5, 5, 5}, petriTest.getBounderMarkVector());
    }

    @Test
    public void getPlacesInvariants() {
        assertTrue(petriTest.getInvariantTransitionMatrix() == null);
    }
}