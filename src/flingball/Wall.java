package flingball;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * An immutable implementation of the Gadget datatype
 * representing an square bumper in the game of Flingball
 * as described in the course website.
 */
public class Wall implements Gadget {

    /**
     * Abstraction function:
     * AF(name, origin) = represents a square shaped bumper
     *     in the game of flingball. The name and origin are the
     *     respective name and origin of the square bumper.
     *
     * Rep Invariant: 
     *     origin coordinates must be within the board
     *     
     * Safety from Rep Exposure
     *     all fields are private, final and immutable
     *     no function returns any pointers that could mutate any fields
     *     observer functions return defensive copies of objects so that they are immutable
     */
    
    private final double BOARD_MIN_BOUND = 0;
    private final double BOARD_MAX_BOUND = Dimensions.DRAWING_AREA_SIZE_IN_PIXELS;
    private final double HALF = 0.5;
    
    private final String name;
    private final physics.LineSegment wall;
    private final int orientation = 0;
    private List<Gadget> actions = new ArrayList<Gadget>();
    
    /**
     * Creates a new wall
     * @param name of wall
     * @param p1 one endpoint of the wall
     * @param p2 other endpoint of the wall
     */
    public Wall(String name, physics.Vect p1, physics.Vect p2) {
        this.name = name;
        this.wall = new physics.LineSegment(p1, p2);
        checkRep();
    }
    
    /**
     * Confirms that the rep invariant of the square bumper
     * class is still intact.
     */
    public void checkRep() {
        assert(wall.p1().x() >= BOARD_MIN_BOUND && wall.p1().x() <= BOARD_MAX_BOUND) :
               "x coordinate of p1 must be within in the board";
        assert(wall.p2().x() >= BOARD_MIN_BOUND && wall.p2().x() <= BOARD_MAX_BOUND) :
            "x coordinate of p2 must be within in the board";
        assert(wall.p1().y() >= BOARD_MIN_BOUND && wall.p1().y() <= BOARD_MAX_BOUND) :
            "y coordinate of p1 must be within in the board";
        assert(wall.p2().y() >= BOARD_MIN_BOUND && wall.p2().y() <= BOARD_MAX_BOUND) :
            "y coordinate of p2 must be within in the board";
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
        return wall.p1().plus(wall.p2()).times(HALF);
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public CollisionBoundary getCollisionBoundaries() {
        checkRep();
        
        List<physics.LineSegment> bounds = new ArrayList<>();
        
        bounds.add(wall);

        return new CollisionBoundary(GadgetShape.RECTANGLE, bounds);
        
    }
    
    @Override
    public void draw(Graphics2D g2) {
        // Nothing
    }
}
