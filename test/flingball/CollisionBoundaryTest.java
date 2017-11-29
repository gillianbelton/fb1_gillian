package flingball;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class CollisionBoundaryTest {
    
    private static final int TRIANGLE_SIDES = 3;
    private static final int SQUARE_SIDES = 4;
    private static final int FIVE = 5;
    private static final int TEN = 10;
    
    // Testing strategy
    //     getShape
    //         triangle
    //         rectangle
    //         circle
    //     boundaries: number of sides
    //
    // Cover all parts of the partition
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    /**
     * @return a sample CollisionBoundary in the shape of a circle for testing
     */
    public CollisionBoundary getCircle() {
        physics.Vect center = new physics.Vect(FIVE, TEN);
        physics.Vect edge = new physics.Vect(TEN, TEN);
        physics.LineSegment radius = new physics.LineSegment(center, edge);
        return new CollisionBoundary(GadgetShape.CIRCLE, Arrays.asList(radius));
    }
    
    /**
     * @return a sample CollisionBoundary in the shape of a triangle for testing
     */
    public CollisionBoundary getTriangle() {
        physics.Vect topLeft = new physics.Vect(FIVE, FIVE);
        physics.Vect topRight = new physics.Vect(FIVE, TEN);
        physics.Vect bottomLeft = new physics.Vect(TEN, FIVE);
        physics.LineSegment topLeg = new physics.LineSegment(topLeft, topRight);
        physics.LineSegment leftLeg = new physics.LineSegment(topLeft, bottomLeft);
        physics.LineSegment hypotenuse = new physics.LineSegment(bottomLeft, topRight);
        return new CollisionBoundary(GadgetShape.TRIANGLE, Arrays.asList(topLeg, leftLeg, hypotenuse));
    }
    
    /**
     * @return a sample CollisionBoundary in the shape of a square for testing
     */
    public CollisionBoundary getSquare() {
        physics.Vect topLeft = new physics.Vect(FIVE, FIVE);
        physics.Vect topRight = new physics.Vect(FIVE, TEN);
        physics.Vect bottomLeft = new physics.Vect(TEN, FIVE);
        physics.Vect bottomRight = new physics.Vect(TEN, TEN);
        physics.LineSegment topLeg = new physics.LineSegment(topLeft, topRight);
        physics.LineSegment leftLeg = new physics.LineSegment(topLeft, bottomLeft);
        physics.LineSegment bottomLeg = new physics.LineSegment(bottomLeft, bottomRight);
        physics.LineSegment rightLeg = new physics.LineSegment(topRight, bottomRight);
        return new CollisionBoundary(GadgetShape.RECTANGLE, Arrays.asList(topLeg, leftLeg, bottomLeg, rightLeg));
    }

    @Test
    public void testGetShape() {
        assertEquals("Circle bumper shape is right", getCircle().getShape(), GadgetShape.CIRCLE);
        assertEquals("Triangle bumper shape is right", getTriangle().getShape(), GadgetShape.TRIANGLE);
        assertEquals("Square bumper shape is right", getSquare().getShape(), GadgetShape.RECTANGLE);
    }
    
    @Test
    public void testGetBoundaries() {
        assertEquals("Circle bumper shape is right", getCircle().getBoundaries().size(), 1);
        assertEquals("Triangle bumper shape is right", getTriangle().getBoundaries().size(), TRIANGLE_SIDES);
        assertEquals("Square bumper shape is right", getSquare().getBoundaries().size(), SQUARE_SIDES);
    }
    
}