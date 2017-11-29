package flingball;

import static org.junit.Assert.*;

import org.junit.Test;

public class CircleBumperTest {
    
    private static final double ERROR = .01;
    private static final double RADIUS = 0.5;
    private static final double XCOOR = 5;
    private static final double YCOOR = 10;
    
    // Testing strategy
    //     getName()
    //     getOrigin()
    //     getBoundaries()
    //         number of sides
    //         GadgetShape
    //         radius length meets specs
    //
    // Cover all parts of the partition
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    /**
     * @return a sample CircleBumper object for testing
     */
    public CircleBumper getBumper() {
        return new CircleBumper("bumper", new physics.Vect(XCOOR, YCOOR));
    }
    
    @Test
    public void testGetName() {
        CircleBumper bumper = getBumper();
        assertEquals("Got wrong name", bumper.getName(), "bumper");
    }
    
    @Test
    public void testGetOrigin() {
        CircleBumper bumper = getBumper();
        assertEquals("Got wrong center", bumper.getOrigin(), new physics.Vect(XCOOR, YCOOR));
    }
    
    @Test
    public void testGetBoundaries() {
        CircleBumper bumper = getBumper();
        assertEquals("Not 1 radius", bumper.getCollisionBoundaries().getBoundaries().size(),
                1);
        assertEquals("Got wrong boundaries", bumper.getCollisionBoundaries().getShape(),
                GadgetShape.CIRCLE);
        assertEquals("Got wrong radius", bumper.getCollisionBoundaries().getBoundaries().get(0).length(),
                new physics.Vect(0, RADIUS).length(), ERROR);
    }
    
    
}