package flingball;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A mutable implementation of the Gadget datatype 
 * representing an absorber in the game of Flingball
 * as described in the course website.
 */
public class Absorber implements Gadget {

    /**
     * Abstraction function:
     * AF(name, origin, width, height) = represents an absorber
     * in the game of flingball. The name, origin, width, and height
     * are the respective name, origin, width, and height of the
     * absorber. Absorbers simulate the ball-return mechanism in many
     * games. Absorbers capture and hold balls when they collide.
     *
     * Rep Invariant: 
     *  -origin coordinates must be within the board
     *  -width and height must be > 0 and <= 20
     *  
     * Safety from rep exposure:
     *     all fields are private, final and immutable
     *     no function returns any pointers that could mutate any fields
     *     observer functions return defensive copies of objects so that they are immutable
     */
    
    private final int MIN_DIMENSION = 0;
    private final int MAX_DIMENSION = 20;
    private final double BALL_CORNER_OFFSET = 0.25;
    private final double BASE_VELOCITY = 0;
    private final double FIRE_VELOCITY = -50;
    
    private final int ZERO = 0;
    private final int ONE = 1;
    
    private final String name;
    private final physics.Vect origin;
    private final int width;
    private final int height;
    private final int orientation = 0;
    private List<Ball> balls = new ArrayList<Ball>();
    private List<Gadget> actions = new ArrayList<Gadget>();
    
    /**
     * Creates an absorber with the given center, name, width, and height.
     * @param name : the name of the unique absorber object
     * @param origin : the origin coords of the absorber
     * @param width : the width of the absorber, must be > 0 and <= 20
     * @param height : the height of the absorber, must be > 0 and <= 20
     */
    public Absorber(String name, physics.Vect origin, int width, int height) {
        this.name = name;
        this.origin = origin;
        this.width = width;
        this.height = height;
        checkRep();
    }
    
    /**
     * Confirms that the rep invariant of the absorber
     * class is still intact.
     */
    public void checkRep() {
        assert(width > MIN_DIMENSION && width <= MAX_DIMENSION) :
               "width must be greater than 0 and less than or equal to 20";
        assert(height > MIN_DIMENSION && height <= MAX_DIMENSION) :
               "height must be greater than 0 and less than or equal to 20";
    }
    
    
    /**
     * If a ball hits an absorber, stores the ball into the absorber bottom right corner
     */
    public void addBall(Ball ball) {
        physics.Vect ballOrigin = new physics.Vect(origin.x() + width - BALL_CORNER_OFFSET,
                                                   origin.y() + height - BALL_CORNER_OFFSET);
        physics.Vect velocity = new physics.Vect(BASE_VELOCITY, BASE_VELOCITY);
        ball.setOrigin(ballOrigin);
        ball.setVelocity(velocity);
        ball.makeInactive();
        balls.add(ball);
    }
    
    @Override
    public void action() {       
        for (Gadget gadget : actions) {
            if (!this.equals(gadget)) {
                gadget.action();
            }
        }
        if (!balls.isEmpty()) {
            Ball ball = balls.get(ZERO);
            balls = balls.subList(ONE, balls.size());
            ball.makeActive();
            ball.setOrigin(new physics.Vect(origin.x() + width - .25, origin.y() - .25));
            ball.setVelocity(new physics.Vect(BASE_VELOCITY, FIRE_VELOCITY));
        }
    }
    
    public List<Ball> getBalls() {
        return Collections.unmodifiableList(balls);
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
        physics.Vect topRight = new physics.Vect(origin.x() + width, origin.y());
        physics.Vect bottomRight = new physics.Vect(origin.x() + width, origin.y() + height);
        physics.Vect bottomLeft = new physics.Vect(origin.x(), origin.y() + height);
        
        physics.LineSegment topLine = new physics.LineSegment(topLeft, topRight);
        physics.LineSegment rightLine = new physics.LineSegment(topRight, bottomRight);
        physics.LineSegment bottomLine = new physics.LineSegment(bottomRight, bottomLeft);
        physics.LineSegment leftLine = new physics.LineSegment(bottomLeft, topLeft);
        
        List<physics.LineSegment> bounds = new ArrayList<>();
        
        bounds.add(topLine);
        bounds.add(rightLine);
        bounds.add(bottomLine);
        bounds.add(leftLine);

        return new CollisionBoundary(GadgetShape.RECTANGLE, bounds);
    }
    
    @Override
    public void draw(Graphics2D g2) {
        double x = getOrigin().x();
        double y = getOrigin().y();
        
        g2.setColor(Color.magenta);
        Rectangle2D.Double rect = new Rectangle2D.Double(x * PIXELS_PER_L,
                                                         y * PIXELS_PER_L,
                                                         width * PIXELS_PER_L,
                                                         height * PIXELS_PER_L);
        g2.fill(rect);
    }
}
