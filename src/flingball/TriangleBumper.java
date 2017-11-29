package flingball;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An immutable implementation of the Gadget datatype
 * representing an triangle bumper in the game of
 * Flingball as described in the course website.
 */
public class TriangleBumper implements Gadget {

    /**
     * Abstraction function:
     * AF(name, origin, orientation) = represents a triangle shaped bumper
     * in the game of flingball. The name, origin, and orientation are all
     * the respective name, origin, and orientation of the triangle bumper.
     *
     * Rep Invariant: 
     *  -origin coordinates must be within the board
     *  -orientation must be 0, 90, 180, or 270 degrees
     *  
     * Safety from rep exposure:
     *     all fields are private, final and immutable
     *     no function returns any pointers that could any fields
     *     observer functions return defensive copies of objects so that they are immutable
     */
    
    private final double BOARD_MIN_BOUND = 0;
    private final double BOARD_MAX_BOUND = 19;
    
    private final Set<Integer> ORIENTATIONS = getOrientations();
    private final int SIDELENGTH = 1;
    private final int TOP_LEFT_ORIENTATION = 0;
    private final int TOP_RIGHT_ORIENTATION = 90;
    private final int BOTTOM_RIGHT_ORIENTATION = 180;
    private final int BOTTOM_LEFT_ORIENTATION = 270;
    private final int NUM_SIDES = 3;
    private final int ZERO = 0;
    
    
    private final String name;
    private final physics.Vect origin;
    private final int orientation;
    private List<Gadget> actions = new ArrayList<Gadget>();
    private Polygon triangle;
    
    
    /**
     * Creates a triangle bumper with the given origin, name and orientation.
     * @param name : the name of the unique triangle bumper object. Can not be
     * named 'TriangleBumper'.
     * @param origin : the origin (top left) coords of the triangle bumper  
     * @param orientation : the orientation of the triangle bumper, must be
     * 0, 90, 180, or 270. 
     */
    public TriangleBumper(String name, physics.Vect origin, int orientation) {
        this.name = name;
        this.origin = origin;
        this.orientation = orientation;
        this.triangle = getTriangle();
        checkRep();
    }
    
    /**
     * Confirms that the rep invariant of the triangle bumper
     * class is still intact.
     */
    public void checkRep() {
        assert(ORIENTATIONS.contains(orientation));
        assert(origin.x() >= BOARD_MIN_BOUND && origin.x() <= BOARD_MAX_BOUND) :
               "x coordinate of triangle must be within in the board";
        assert(origin.y() >= BOARD_MIN_BOUND && origin.y() <= BOARD_MAX_BOUND) :
               "y coordinate of triangle must be within in the board";
    }
    
    public Set<Integer> getOrientations() {
        Set<Integer> orientationsPossible = new HashSet<>();
        orientationsPossible.add(TOP_LEFT_ORIENTATION);
        orientationsPossible.add(TOP_RIGHT_ORIENTATION);
        orientationsPossible.add(BOTTOM_RIGHT_ORIENTATION);
        orientationsPossible.add(BOTTOM_LEFT_ORIENTATION);
        return orientationsPossible;
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
    
    /**
     * Create a Polygon object connecting the points in the order received scaled by Pixels per unit (L)
     * @param points an ordered list of points for the edge of a polygon
     * @return the Polygon object consisting of the given points
     */
    public Polygon createPolygon(List<physics.Vect> points) {
        int[] xValues = new int[points.size()]; // initialize list of length of NUM_SIDES
        int[] yValues = new int[points.size()];
        
        for (int i = ZERO; i < points.size(); i++) {
            xValues[i] = (int) points.get(i).x() * PIXELS_PER_L;
            yValues[i] = (int) points.get(i).y() * PIXELS_PER_L;
        }
        return new Polygon(xValues, yValues, NUM_SIDES);
    }
    
    /**
     * Get the relevant triangle bumper object 
     * @return a polygon representing the triangle bumper
     */
    public Polygon getTriangle() {
        physics.Vect topLeft = origin;
        physics.Vect topRight = new physics.Vect(origin.x() + SIDELENGTH, origin.y());
        physics.Vect bottomRight = new physics.Vect(origin.x() + SIDELENGTH, origin.y() + SIDELENGTH);
        physics.Vect bottomLeft = new physics.Vect(origin.x(), origin.y() + SIDELENGTH);
        
        List<physics.Vect> points = new ArrayList<>();
        
        if (orientation == TOP_LEFT_ORIENTATION) {
            points.add(topLeft);
            points.add(topRight);
            points.add(bottomLeft);
            return createPolygon(points);
        }
        else if (orientation == TOP_RIGHT_ORIENTATION) {
            points.add(topLeft);
            points.add(topRight);
            points.add(bottomRight);
            return createPolygon(points);
        }
        else if (orientation == BOTTOM_RIGHT_ORIENTATION) {
            points.add(bottomRight);
            points.add(topRight);
            points.add(bottomLeft);
            return createPolygon(points);
        } else { // orientation == 270, bottom left
            points.add(topLeft);
            points.add(bottomRight);
            points.add(bottomLeft);
            return createPolygon(points);
        }
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
        physics.LineSegment diagTopLeftBottomRight = new physics.LineSegment(bottomRight, topLeft);
        physics.LineSegment diagBottomLeftTopRight = new physics.LineSegment(bottomLeft, topRight);
        
        List<physics.LineSegment> bounds = new ArrayList<>();
        
        if (orientation == TOP_LEFT_ORIENTATION) {
            bounds.add(topLine);
            bounds.add(diagBottomLeftTopRight);
            bounds.add(leftLine);
        }
        else if (orientation == TOP_RIGHT_ORIENTATION) {
            bounds.add(topLine);
            bounds.add(rightLine);
            bounds.add(diagTopLeftBottomRight);
        }
        else if (orientation == BOTTOM_RIGHT_ORIENTATION) {
            bounds.add(bottomLine);
            bounds.add(rightLine);
            bounds.add(diagBottomLeftTopRight);
        } else { // orientation == 270
            bounds.add(bottomLine);
            bounds.add(leftLine);
            bounds.add(diagTopLeftBottomRight);
        }

        return new CollisionBoundary(GadgetShape.TRIANGLE, bounds);
    }
    
    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Color.green);
        g2.fill(triangle);
    }
}
