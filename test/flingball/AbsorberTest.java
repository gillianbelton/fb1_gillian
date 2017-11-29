package flingball;

import static org.junit.Assert.*;

import org.junit.Test;

public class AbsorberTest {
    
    private static final int NUM_SIDES = 4;
    private static final double ERROR = .01;
    private static final double XCOOR = 5;
    private static final double YCOOR = 10;
    private static final int WIDTH = 4;
    private static final int HEIGHT = 6;
    
    // Testing strategy
    //      getName()
    //      getOrigin()
    //      getBoundaries()
    //          number of sides
    //          GadgetShape
    //          Edges should be connected, orthogonal and equal in length to opposite side
    //      addBall()
    //          Uses getBalls to ensure a ball is added
    //
    // Cover all parts of the partition
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    /**
     * @return a sample absorber object for testing
     */
    public Absorber getAbsorber() {
        return new Absorber("absorber", new physics.Vect(XCOOR, YCOOR), WIDTH, HEIGHT);
    }
    
    @Test
    public void testGetName() {
        Absorber absorber = getAbsorber();
        assertEquals("Got wrong name", absorber.getName(), "absorber");
    }
    
    @Test
    public void testGetOrigin() {
        Absorber absorber = getAbsorber();
        assertEquals("Got wrong center", absorber.getOrigin(), new physics.Vect(XCOOR, YCOOR));
    }
    
    @Test
    public void testAddBall() {
        Absorber absorber = getAbsorber();
        Ball ball = new Ball("test", new physics.Vect(5, 5), new physics.Vect(0, 0));
        absorber.addBall(ball);
        assertEquals("Got wrong name", absorber.getBalls().size(), 1);
        absorber.addBall(ball);
        assertEquals("Got wrong name", absorber.getBalls().size(), 2);
    }
    
    @Test
    public void testGetBoundaries() {
        Absorber absorber = getAbsorber();
        assertEquals("Got wrong boundaries", absorber.getCollisionBoundaries().getShape(),
                GadgetShape.RECTANGLE);
        assertEquals("Not 4 sides", absorber.getCollisionBoundaries().getBoundaries().size(),
                NUM_SIDES);
        for (int i = 0; i < NUM_SIDES; i++) {
            assertEquals("Sides not equal to opposite side", absorber.getCollisionBoundaries().getBoundaries().get(i).length(),
                    absorber.getCollisionBoundaries().getBoundaries().get((i+NUM_SIDES/2) % NUM_SIDES).length(), ERROR);
            assertEquals("Sides not connected",
                    absorber.getCollisionBoundaries().getBoundaries().get(i).p2(),
                    absorber.getCollisionBoundaries().getBoundaries().get((i + 1) % NUM_SIDES).p1());
            assertEquals("Sides not orthogonal",
                    absorber.getCollisionBoundaries().getBoundaries().get(i).p2().minus(
                            absorber.getCollisionBoundaries().getBoundaries().get(i).p1()
                    ).dot(absorber.getCollisionBoundaries().getBoundaries().get((i+1)%NUM_SIDES).p2().minus(
                            absorber.getCollisionBoundaries().getBoundaries().get((i+1)%NUM_SIDES).p1()
                    )),
                    0, ERROR);
        }
    }
    
}