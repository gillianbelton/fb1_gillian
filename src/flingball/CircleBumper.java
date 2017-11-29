package flingball;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

/**
 * An immutable implementation of the Gadget datatype 
 * representing a circle bumper in the game of Flingball
 * as described in the course website.
 */
public class CircleBumper implements Gadget {

    /**
     * Abstraction function:
     *     AF(name, origin) = represents a circle shaped bumper
     *     in the game of flingball with a diameter of 1L. The 
     *     name and origin are the respective name and origin 
     *     (center) of the circle bumper.
     *
     * Rep Invariant: 
     *     origin coordinates must be within the board
     * 
     * Safety from rep exposure:
     *     all fields are private, final and immutable
     *     no function returns any pointers that could any fields
     *     observer functions return defensive copies of objects so that they are immutable
     */
    
    private final double DIAMETER = 1;
    private final double BOARD_MIN_BOUND = 0.5;
    private final double BOARD_MAX_BOUND = 19.5;
    
    private final int orientation = 0;
    private final String name;
    private final physics.Vect origin;
    private List<Gadget> actions = new ArrayList<Gadget>();
    
    /**
     * Creates a circle bumper with the given center, name and radius.
     * @param name : the name of the unique circle bumper object
     * @param origin : the origin (center) coords of the circle bumper
     */
    public CircleBumper(String name, physics.Vect origin) {
        this.name = name;
        this.origin = origin;
        checkRep();
    }
    
    /**
     * Confirms that the rep invariant of the circle bumper
     * class is still intact.
     */
    public void checkRep() {
        assert(origin.x() >= BOARD_MIN_BOUND && origin.x() <= BOARD_MAX_BOUND) :
               "x coordinate of circle must be within in the board";
        assert(origin.y() >= BOARD_MIN_BOUND && origin.y() <= BOARD_MAX_BOUND) :
               "y coordinate of circle must be within in the board";
    }

    
    @Override
    public void action() {
        for (Gadget gadget : actions) {
            gadget.action();
        }
    }
    
    @Override
    public void addAction(Gadget gadget) {
        actions.add(gadget);
    }
    
    @Override
    public int getOrientation() {
        return orientation;
    }
    
    @Override
    public physics.Vect getOrigin() {
        return origin;
    }
    
    @Override
    public String getName() {
        return name;
    }
    

    @Override
    public CollisionBoundary getCollisionBoundaries() {
        // calculate line segment representing center of circle to the outer boundary
        physics.Vect outerPoint = new physics.Vect(0, DIAMETER/2).plus(origin);
        physics.LineSegment line = new physics.LineSegment(origin, outerPoint);
        
        // return CollisionBoundary object with the line segment calculated as the bounds
        List<physics.LineSegment> bounds = new ArrayList<>();
        bounds.add(line);
        return new CollisionBoundary(GadgetShape.CIRCLE, bounds);
    }
    
    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Color.red);
        g2.fill(new Ellipse2D.Double(origin.x() * PIXELS_PER_L,
                                     origin.y() * PIXELS_PER_L,
                                     DIAMETER * PIXELS_PER_L,
                                     DIAMETER * PIXELS_PER_L));
    }
}
