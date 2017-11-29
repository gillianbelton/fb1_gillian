package flingball;

import java.util.List;

/**
 * An immutable datatype that represents the collision boundaries of a gadget object.
 * Can represent either a ball, an absorber, a triangle bumper, a square bumper, or
 * a circle bumper- which translate to the shapes: triangle, rectangle, and circle.
 *
 */
public class CollisionBoundary {
    
    private final List<physics.LineSegment> bounds;
    private final GadgetShape shape;
    
    /**
     * Constructor for Collision Boundary object
     * @param shape the shape of the gadget which can be triangle, circle or rectangle
     * @param bounds the lines segments representing the boundaries of the gadget object
     */
    public CollisionBoundary(GadgetShape shape, List<physics.LineSegment> bounds) {
        this.shape = shape;
        this.bounds = bounds;
    }
    
    /**
     * Returns the shape of gadget.
     * @return a GadgetShape with the type of collision boundary the object
     * represents.
     */
    public GadgetShape getShape() {
        return shape;
    }
    
    /**
     * @return a list of LineSegments representing the boundaries of the 
     * gadget object. If the GadgetShape is a circle, then there should 
     * be only one line segment, of which the length is the gadget's radius,
     * and the first point in the segment is the circle's center.
     */
    public List<physics.LineSegment> getBoundaries() {
        return bounds;
    }
}

