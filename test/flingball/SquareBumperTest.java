package flingball;

import static org.junit.Assert.*;

import org.junit.Test;

public class SquareBumperTest {
    
    private static final int NUM_SIDES = 4;
    private static final double ERROR = .01;
    private static final double XCOOR = 5;
    private static final double YCOOR = 10;
    
    // Testing strategy
    //      getName()
    //      getOrigin()
    //      getBoundaries()
    //          number of sides
    //          GadgetShape
    //      Edges: connected, orthogonal and equal in length to next side
    //
    // Cover all parts of the partition
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    public SquareBumper getBumper() {
        return new SquareBumper("bumper", new physics.Vect(XCOOR, YCOOR));
    }
    
    @Test
    public void testGetName() {
        SquareBumper bumper = getBumper();
        assertEquals("Got wrong name", bumper.getName(), "bumper");
    }
    
    @Test
    public void testGetOrigin() {
        SquareBumper bumper = getBumper();
        assertEquals("Got wrong center", bumper.getOrigin(), new physics.Vect(XCOOR, YCOOR));
    }
    
    @Test
    public void testGetBoundaries() {
        SquareBumper bumper = getBumper();
        assertEquals("Got wrong boundaries", bumper.getCollisionBoundaries().getShape(),
                GadgetShape.RECTANGLE);
        assertEquals("Not 4 sides", bumper.getCollisionBoundaries().getBoundaries().size(),
                NUM_SIDES);
        for (int i = 0; i < NUM_SIDES; i++) {
            assertEquals("Sides not equal", bumper.getCollisionBoundaries().getBoundaries().get(0).length(),
                    bumper.getCollisionBoundaries().getBoundaries().get(i).length(), ERROR);
            assertEquals("Sides not connected",
                    bumper.getCollisionBoundaries().getBoundaries().get(i).p1(),
                    bumper.getCollisionBoundaries().getBoundaries().get((i + 1) % NUM_SIDES).p2());
            assertEquals("Sides not orthogonal",
                    bumper.getCollisionBoundaries().getBoundaries().get(i).p2().minus(
                            bumper.getCollisionBoundaries().getBoundaries().get(i).p1()
                    ).dot(bumper.getCollisionBoundaries().getBoundaries().get((i+1)%NUM_SIDES).p2().minus(
                            bumper.getCollisionBoundaries().getBoundaries().get((i+1)%NUM_SIDES).p1()
                    )),
                    0, ERROR);
        }
    }
}