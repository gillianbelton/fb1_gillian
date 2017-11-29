package flingball;

import static org.junit.Assert.*;

import org.junit.Test;

public class BallTest {
    
    private static final double ERROR = .01;
    private static final double RADIUS = 0.25;
    private static final double XCOOR = 2;
    private static final double YCOOR = 3;
    private static final double XVELOCITY = 0;
    private static final double YVELOCITY = 1;
    
    private static final physics.Vect FIVES_VECTOR = new physics.Vect(5, 5);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    /**
     * @return a sample ball object for testing purposes
     */
    public Ball newBall() {
        return new Ball("ball", new physics.Vect(XCOOR, YCOOR), new physics.Vect(XVELOCITY, YVELOCITY));
    }
    
    @Test
    public void testGetCircle() {
        Ball ball = newBall();
        assertEquals("This ball doesn't have the right radius", ball.getCollisionBoundaries().getBoundaries().get(0).length(),
                new physics.Vect(0, RADIUS).length(), ERROR);
        assertEquals("Got wrong shape", ball.getCollisionBoundaries().getShape(),
                GadgetShape.CIRCLE);
    }
    
    @Test
    public void testGetCenter() {
        Ball ball = newBall();
        assertEquals("This ball doesn't have the right center", ball.getOrigin(),
                new physics.Vect(XCOOR, YCOOR));
    }
    
    @Test
    public void testSetCenter() {
        Ball ball = newBall();
        physics.Vect newOrigin = FIVES_VECTOR;
        ball.setOrigin(newOrigin);
        assertEquals("This ball doesn't have the right center", ball.getOrigin(),
                FIVES_VECTOR);
        newOrigin = new physics.Vect(XCOOR, YCOOR);
        assertEquals("Defensive copying failed", ball.getOrigin(),
                FIVES_VECTOR);
    }

    
    @Test
    public void testGetVelocity() {
        Ball ball = newBall();
        assertEquals("This ball doesn't have the right velocity", ball.getVelocity(),
                new physics.Vect(XVELOCITY, YVELOCITY));
    }
    
    @Test
    public void testSetVelocity() {
        Ball ball = newBall();
        physics.Vect newVelocity = FIVES_VECTOR;
        ball.setVelocity(newVelocity);
        assertEquals("This ball doesn't have the right velocity", ball.getVelocity(),
                FIVES_VECTOR);
        newVelocity = new physics.Vect(XVELOCITY, YVELOCITY);
        assertEquals("Defensive copying failed", ball.getVelocity(),
                FIVES_VECTOR);
    }
    
    
}