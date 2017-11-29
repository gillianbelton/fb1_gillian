
package flingball;

import static org.junit.Assert.*;

import org.junit.Test;

public class TriangleBumperTest {
    
    private static final int NUM_SIDES = 3;
    private static final double ERROR = .01;
    private static final double XCOOR = 5;
    private static final double YCOOR = 10;
    private static final int RIGHT_ANGLE = 90;
    
    // Testing strategy
    //      getName()
    //      getOrigin()
    //      getBoundaries()
    //          number of sides
    //          GadgetShape
    //          rotations: normal / rotation
    //      Edges:
    //          connected
    //          two should be both equal in length and orthogonal 
    //
    // Cover all parts of the partition

    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    public TriangleBumper getBumper1() {
        return new TriangleBumper("bumper1", new physics.Vect(XCOOR, YCOOR), 0);
    }
    
    public TriangleBumper getBumper2() {
        return new TriangleBumper("bumper2", new physics.Vect(XCOOR, YCOOR), RIGHT_ANGLE);
    }
    
    @Test
    public void testGetName() {
        TriangleBumper bumper = getBumper1();
        assertEquals("Got wrong name", bumper.getName(), "bumper1");
    }
    
    @Test
    public void testGetOrienation() {
        TriangleBumper bumper1 = getBumper1();
        assertEquals("Got wrong orientation", bumper1.getOrientation(), 0);
        TriangleBumper bumper2 = getBumper2();
        assertEquals("Got wrong orientation", bumper2.getOrientation(), RIGHT_ANGLE);
    }
    
    @Test
    public void testGetOrigin() {
        TriangleBumper bumper = getBumper1();
        assertEquals("Got wrong center", bumper.getOrigin(), new physics.Vect(XCOOR, YCOOR));
    }
    
    @Test
    public void testGetBoundaries() {
        TriangleBumper bumper = getBumper1();
        assertEquals("Got wrong boundaries", bumper.getCollisionBoundaries().getShape(),
                GadgetShape.TRIANGLE);
        assertEquals("Not 3 sides", bumper.getCollisionBoundaries().getBoundaries().size(),
                NUM_SIDES);
        boolean sharePoint;
        physics.Vect x1, x2, y1, y2;
        for (int i = 0; i < NUM_SIDES; i++) {
            x1 = bumper.getCollisionBoundaries().getBoundaries().get(i).p1();
            y1 = bumper.getCollisionBoundaries().getBoundaries().get(i).p2();
            x2 = bumper.getCollisionBoundaries().getBoundaries().get((i + 1) % NUM_SIDES).p1();
            y2 = bumper.getCollisionBoundaries().getBoundaries().get((i + 1) % NUM_SIDES).p2();
            sharePoint = (x1.equals(x2) || x1.equals(y2) || y1.equals(x2) || y1.equals(y2));
            assertTrue("Sides not connected", sharePoint);
        }
        boolean isRightIsosceles = false;
        for (int i = 0; i < NUM_SIDES; i++) {
            if (Math.abs(bumper.getCollisionBoundaries().getBoundaries().get(0).length() - 
                    bumper.getCollisionBoundaries().getBoundaries().get(i).length()) < ERROR) {
                if (Math.abs(bumper.getCollisionBoundaries().getBoundaries().get(i).p2().minus(
                            bumper.getCollisionBoundaries().getBoundaries().get(i).p1()
                    ).dot(bumper.getCollisionBoundaries().getBoundaries().get((i+1)%NUM_SIDES).p2().minus(
                            bumper.getCollisionBoundaries().getBoundaries().get((i+1)%NUM_SIDES).p1()
                    ))) < ERROR) {
                    isRightIsosceles = true;
                }
            }
        }
        assertTrue("Not right isosceles", isRightIsosceles);
    }
    
    @Test
    public void testGetBoundariesRotated() {
        TriangleBumper bumper = getBumper2();
        assertEquals("Got wrong boundaries", bumper.getCollisionBoundaries().getShape(),
                GadgetShape.TRIANGLE);
        assertEquals("Not 3 sides", bumper.getCollisionBoundaries().getBoundaries().size(),
                NUM_SIDES);
        boolean isRightIsosceles = false;
        for (int i = 0; i < NUM_SIDES; i++) {
            assertEquals("Sides not connected",
                    bumper.getCollisionBoundaries().getBoundaries().get(i).p2(),
                    bumper.getCollisionBoundaries().getBoundaries().get((i + 1) % NUM_SIDES).p1());
        }
        for (int i = 0; i < NUM_SIDES; i++) {
            if (Math.abs(bumper.getCollisionBoundaries().getBoundaries().get(0).length() - 
                    bumper.getCollisionBoundaries().getBoundaries().get(i).length()) < ERROR) {
                if (Math.abs(bumper.getCollisionBoundaries().getBoundaries().get(i).p2().minus(
                            bumper.getCollisionBoundaries().getBoundaries().get(i).p1()
                    ).dot(bumper.getCollisionBoundaries().getBoundaries().get((i+1)%NUM_SIDES).p2().minus(
                            bumper.getCollisionBoundaries().getBoundaries().get((i+1)%NUM_SIDES).p1()
                    ))) < ERROR) {
                    isRightIsosceles = true;
                }
            }
        }
        assertTrue("Not right isosceles", isRightIsosceles);
    }
    
}