package flingball;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * An immutable implementation of the Gadget datatype
 * representing an square bumper in the game of Flingball
 * as described in the course website.
 */
public class SquareBumper implements Gadget {

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
    private final double BOARD_MAX_BOUND = 19;
    private final int SIDELENGTH = 1;
    
    private final String name;
    private final physics.Vect origin;
    private final int orientation = 0;
    private List<Gadget> actions = new ArrayList<Gadget>();
    
    /**
     * Creates a square bumper with the given origin and name.
     * @param name : the name of the unique square bumper object
     * @param origin : the origin (top left) coords of the square bumper  
     */
    public SquareBumper(String name, physics.Vect origin) {
        this.name = name;
        this.origin = origin;
        checkRep();
    }
    
    /**
     * Confirms that the rep invariant of the square bumper
     * class is still intact.
     */
    public void checkRep() {
        assert(origin.x() >= BOARD_MIN_BOUND && origin.x() <= BOARD_MAX_BOUND) :
               "x coordinate of square must be within in the board";
        assert(origin.y() >= BOARD_MIN_BOUND && origin.y() <= BOARD_MAX_BOUND) :
               "y coordinate of square must be within in the board";
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
        checkRep();
        physics.Vect topLeft = origin;
        physics.Vect topRight = new physics.Vect(origin.x() + SIDELENGTH, origin.y());
        physics.Vect bottomRight = new physics.Vect(origin.x() + SIDELENGTH, origin.y() + SIDELENGTH);
        physics.Vect bottomLeft = new physics.Vect(origin.x(), origin.y() + SIDELENGTH);
        
        physics.LineSegment topLine = new physics.LineSegment(topLeft, topRight);
        physics.LineSegment rightLine = new physics.LineSegment(topRight, bottomRight);
        physics.LineSegment bottomLine = new physics.LineSegment(bottomRight, bottomLeft);
        physics.LineSegment leftLine = new physics.LineSegment(bottomLeft, topLeft);

        
        List<physics.LineSegment> bounds = new ArrayList<>();
        
        bounds.add(bottomLine);
        bounds.add(rightLine);
        bounds.add(topLine);
        bounds.add(leftLine);

        return new CollisionBoundary(GadgetShape.RECTANGLE, bounds);
        
    }
    
    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Color.yellow);
        double x = getOrigin().x();
        double y = getOrigin().y();
        Rectangle2D.Double square = new Rectangle2D.Double(x * PIXELS_PER_L,
                                                           y * PIXELS_PER_L,
                                                           SIDELENGTH * PIXELS_PER_L,
                                                           SIDELENGTH * PIXELS_PER_L);
        g2.fill(square);
    }
}
