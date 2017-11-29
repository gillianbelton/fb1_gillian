package flingball;

import java.awt.Graphics2D;

/**
 * An mutable datatype that represents a gadget, or an item on a Flingball board. 
 * All gadgets have an action and a trigger procedures, as well as an orientation,
 * a center, a size, and a shape.
 *
 */
public interface Gadget {
    
    public static final int PIXELS_PER_L = Dimensions.PIXELS_PER_L;

    
    /**
     * Initiates the action of a gadget object. Should execute
     * the Gadget's specified action behavior.
     */
    public void action();
    
    /**
     * Gadgets, when triggered, can initiate the actions of other
     * gadgets. This method adds a gadget for this to activate. 
     * @param gadget : gadget to be triggered when this is triggered
     */
    public void addAction(Gadget gadget);
    
    /**
     * Gets the rotational information about the gadget. Can be
     * rotated 0, 90, 180, or 270º degrees.
     * @return the orientation of the gadget in degrees
     */
    public int getOrientation();
    
    /**
     * Gets the coordinate information of the gadget, its origin
     * position on the Flingball board. For triangles, the origin 
     * is defined as the vertex (corner) opposite the hypotenuse. 
     * @return a vector of the x, y coords of the gadget
     */
    public physics.Vect getOrigin();
    
    /**
     * Gets the relevant information needed to calculate a potential
     * collision with this gadget.
     * @return a valid CollisionBoundary object with the correct geometric
     * information of the gadget. 
     */
    public CollisionBoundary getCollisionBoundaries();
    
    /**
     * @return the name of the Gadget object
     */
    public String getName();
    
    /**
     * Draws the gadget in a given graphics window
     * @param g2 graphics to draw the gadget in
     */
    public void draw(Graphics2D g2);
        
}
