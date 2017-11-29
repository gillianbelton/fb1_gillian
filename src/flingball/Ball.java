package flingball;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.Arrays;

/**
 * An immutable datatype representing an ball in the game of
 * Flingball as described in the course website.
 */
public class Ball {

    /**
     * Abstraction function:
     * AF(name, center, velocity) = represents an ball in the game
     * of flingball. The name, center, and velocity are the 
     * respective name, center, width, and height of the ball
     * A ball can move around the board and collide with other
     * gadgets.
     */
    
    /**
     * Rep Invariant: 
     *  -center coordinates must be within the board
     */
    
    private final String name;
    private physics.Vect origin;
    private physics.Vect velocity;
    private boolean active;
    
    private static final double RADIUS = 0.25;
    private static final double BOARD_LENGTH = 20;
    private static final double ERROR = .01;
    
    /**
     * Creates a ball with the given center, name, and velocity.
     * @param name : the name of the unique square bumper object
     * @param origin : the origin (center) coords of the ball  
     * @param velocity : the width of the bumper object
     */
    public Ball(String name, physics.Vect origin, physics.Vect velocity) {
        this.name = name;
        this.origin = origin;
        this.velocity = velocity;
        this.active = true;
        checkRep();
    }
    
    /**
     * makes the ball active (meaning velocity can be updated by gravity and friction)
     */
    public void makeActive() {
        active = true;
    }
    
    /**
     * makes the ball inactive (meaning velocity cannot be updated by gravity and friction)
     */
    public void makeInactive() {
        active = false;
    }
    
    /**
     * @return true if the ball is active and under the influence of gravity and friction
     */
    public boolean isActive() {
        return (active == true);
    }
    
    /**
     * Confirms that the rep invariant of the ball
     * class is still intact.
     */
    public void checkRep() {
        assert (name != null);
        assert RADIUS <= origin.x() + ERROR;
        assert origin.x() <= BOARD_LENGTH - RADIUS + ERROR;
        assert RADIUS <= origin.y() + ERROR;
        assert origin.y() <= BOARD_LENGTH - RADIUS + ERROR;
    }


    /**
     * Gets the center of the ball
     * @return vector representing the center of the ball
     */
    public physics.Vect getOrigin() {
        return origin;
    }
    
    public void setOrigin(physics.Vect newOrigin) {
        origin = new physics.Vect(newOrigin.x(), newOrigin.y());
        checkRep();
    }
    
    /**
     * Gets the name of the ball
     * @return name of the ball (string)
     */
    public String getName() {
        return name;
    }

    
    /**
     * Gets the velocity of the ball
     * @return vector representing ball's velocity
     */
    public physics.Vect getVelocity() {
        return velocity;
    }
    
    /**
     * Sets the ball's new velocity
     * @param newVelocity
     */
    public void setVelocity(physics.Vect newVelocity) {
       velocity = new physics.Vect(newVelocity.x(), newVelocity.y());
       checkRep();
    }
    
    /**
     * Gets the collision boundary of the ball
     * @return representation of circle's edge
     */
    public CollisionBoundary getCollisionBoundaries() {
        return new CollisionBoundary(GadgetShape.CIRCLE,
                Arrays.asList(new physics.LineSegment(0, 0, 0, RADIUS))
                );
                
    }
    
    /**
     * Draws the ball in the given Graphics
     * @param g2 to draw the ball in
     */
    public void draw(Graphics2D g2) {
        g2.setColor(Color.white);
       
        g2.fill(new Ellipse2D.Double(origin.x() * Dimensions.PIXELS_PER_L,
                                     origin.y() * Dimensions.PIXELS_PER_L,
                                     RADIUS * 2 * Dimensions.PIXELS_PER_L,
                                     RADIUS * 2 * Dimensions.PIXELS_PER_L));
    }
}
