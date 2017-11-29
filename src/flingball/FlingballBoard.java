package flingball;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.Collections;

import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 * A Flingball board simulator.
 * 
 * <p> Flingball is initialized with a layout board grammar, which is used to
 * initialize the gadgets on the board, the outer walls, and define the playing
 * area for the balls.
 * 
 * The playing area is 20 L wide by 20 L high. The upper left corner is (0,0) and
 * the lower right corner is (20,20). The outer walls are line segments. The gadgets
 * on the board are defined by their ADTs. The flingball is a ball object.
 * 
 * When we say a gadget is at a particular location, that means that the gadget’s
 * origin (upper left-hand corner of its bounding box) is at that location.
 * The origin of a ball is at its center.
 * 
 * The board has gravity given by a constant defaulted to 25L/sec^2 and friction given
 * by Vnew = Vold × ( 1 - mu × deltat - mu2 × |Vold| × deltat ) where mu and mu2 are the
 * two friction constants. The frictions are both defaulted to 0.025 per L.
 * 
 */
public class FlingballBoard {
    
//    private static final int GAMEBOARD_SIZE = Dimensions.GAMEBOARD_SIZE;
//    private static final int PIXELS_PER_L = Dimensions.PIXELS_PER_L;
    private static final int DRAWING_AREA_SIZE_IN_PIXELS = Dimensions.DRAWING_AREA_SIZE_IN_PIXELS;
    private static final int GAMEBOARD_SIZE = Dimensions.GAMEBOARD_SIZE;
    private static final physics.Vect TOP_LEFT = new physics.Vect(0, 0);
    private static final physics.Vect TOP_RIGHT = new physics.Vect(0, GAMEBOARD_SIZE);
    private static final physics.Vect BOTTOM_LEFT = new physics.Vect(GAMEBOARD_SIZE, 0);
    private static final physics.Vect BOTTOM_RIGHT = new physics.Vect(GAMEBOARD_SIZE, GAMEBOARD_SIZE);
    private static final double TIMESTEP_SCALAR = 1000.;
    private static final int ZERO = 0;
    private static final int ONE = 1;
    private static final double MINUS_ONE = -1;
    
    private static final Wall TOP = new Wall("_Top", TOP_LEFT, TOP_RIGHT);
    private static final Wall LEFT = new Wall("_Left", TOP_LEFT, BOTTOM_LEFT);
    private static final Wall RIGHT = new Wall("_Right", BOTTOM_RIGHT, TOP_RIGHT);
    private static final Wall BOTTOM = new Wall("_Bottom", BOTTOM_LEFT, BOTTOM_RIGHT);
    
    private static final double TIME_ERROR = 0.0001;
    
    private final ArrayList<Gadget> gadgets;
    private List<Ball> balls;
    private final double gravity;
    private final double friction1;
    private final double friction2;
    private final int timestep;

    
    private final int NUM_GADGETS;
    private final int NUM_BALLS;
    
    // Abstraction function:
    //   AF(gadgets, ball, timestep        = a flingball game with borders defined by the
    //      gravity, friction1, friction2)   gadgets on the board, the balls at various locations,
    //                                       and a timestep through which the balls continue to
    //                                       move and progress throughout the board. The board is 
    //                                       affected by gravity and friction, which is calculated 
    //                                       from the formula specified in the spec.
    //
    // Representation invariant:
    //   there are four walls forming a 20L by 20L board
    //   We don't lose balls/gadgets
    //   timestep is positive
    //   gravity, friction1 and friction2 are nonnegative
    //
    // Safety from rep exposure:
    //   timestep is private, final and immutable
    //   no function returns any pointers that could mutate walls, gadgets, or balls
    //   observer functions return defensive copies of objects so that they are immutable
    //   the constructor takes in the information from the input grammar file, parses it, and
    ///  extracts the information leaving no mutable pointers.

    /**
     * Constructor for the Flingball initial board including window, 
     * drawing area, gadgets, friction, and gravity.
     * @param gadgets the list of gadgets on the board
     * @param balls the list of balls on the board
     * @param timestep in milliseconds (50 milliseconds if unspecified)
     * @param friction1 special value for 1st friction (0.025 if unspecified)
     * @param friction2 special value for 2nd friction (0.025 if unspecified)
     * @param gravity special value for gravity (25.0 if unspecified)
     */
    public FlingballBoard(List<Gadget> gadgets,
                              List<Ball> balls,
                              int timestep,
                              double friction1,
                              double friction2, 
                              double gravity) {
        this.timestep = timestep;
        this.gadgets = new ArrayList<Gadget>();
        for (Gadget gadget : gadgets) {
            this.gadgets.add(gadget);
        }
        // add walls to list of gadgets
        this.gadgets.add(TOP);
        this.gadgets.add(LEFT);
        this.gadgets.add(RIGHT);
        this.gadgets.add(BOTTOM);
        this.balls = balls;
        this.gravity = gravity;
        this.friction1 = friction1;
        this.friction2 = friction2;

        
        // Set initial count of balls and gadgets for later checkRep() calls
        NUM_GADGETS = this.gadgets.size();
        NUM_BALLS = this.balls.size();
        checkRep();
    }
    
    /**
     * Confirms that we don't lose/gain balls/gadgets, and that
     * Timestep is positive, other constants are nonnegative
     */
    private void checkRep() {
        assert balls.size() == NUM_BALLS;
        assert gadgets.size() == NUM_GADGETS;
        assert timestep > 0;
        assert gravity >= 0;
        assert friction1 >= 0;
        assert friction2 >= 0;
    }
    
    
    /**
     * Move each ball to account for their motion during one time step
     * This accounts for gravity and friction.
     */
    public void nextStep() {
        double remainingTime, timeUntilCollision;
        Map<Gadget, Double> collisionTimes;
        Map<Gadget, physics.LineSegment> collisionWalls;
        ArrayList<Ball> newBalls = new ArrayList<Ball>();
        for (Ball ball : balls) { // For each ball
            remainingTime = timestep/TIMESTEP_SCALAR; // initialize remaining time
            if (!ball.isActive()) {
                newBalls.add(ball); // Don't move ball if in absorber
                continue;
            }
            while (remainingTime > ZERO) {
                collisionTimes = collisionTimes(ball);
                collisionWalls = collisionWalls(ball);
                timeUntilCollision = minDouble(collectionToList(collisionTimes.values()));
                // Move ball as much as you can before collision or time left
                ball.setOrigin(moveBall(ball, Math.min(timeUntilCollision, remainingTime)));
                remainingTime -=  Math.min(timeUntilCollision, remainingTime);
                if (remainingTime > TIME_ERROR) { // Check if ball just collided with gadget
                    for (Gadget g: gadgets) {
                        if (collisionTimes.get(g) <= timeUntilCollision + TIME_ERROR) {
                            if (g instanceof Absorber) {
                                if (ball.getVelocity().y() < ZERO) {
                                    break; // Ignore ball if being launched
                                }
                                // Absorb ball and end time left
                                ((Absorber) g).addBall(ball);
                                remainingTime = 0;
                            } // Otherwise, collide with line or circle
                            else if (g.getCollisionBoundaries().getShape().equals(GadgetShape.CIRCLE)) {
                                ball.setVelocity(physics.Physics.reflectCircle(g.getOrigin(), ball.getOrigin(), ball.getVelocity()));
                            }
                            else {
                                ball.setVelocity(physics.Physics.reflectWall(collisionWalls.get(g), ball.getVelocity()));
                            }
                            g.action(); // Call action after collision
                            break;
                        }
                    }
                }
                remainingTime -=  Math.min(timeUntilCollision, remainingTime);
            }
            if (ball.isActive()) {
                // Using gravity, friction1, friction2, adjust velocity of non-absorbed balls
                ball.setVelocity(newVelocity(ball, timestep/TIMESTEP_SCALAR));
            }
            newBalls.add(ball);
        }
        balls = newBalls;
        checkRep();
    }
    
    /**
     * Acquire the times left for a ball to collide with each gadget
     * @param ball to analyze
     * @return mapping from gadgets to time until ball collides with it
     */
    private Map<Gadget, Double> collisionTimes(Ball ball) {
        HashMap<Gadget, Double> times = new HashMap<Gadget, Double>();
        double bestTime;
        physics.Circle ballCircle = new physics.Circle(ball.getOrigin(), ball.getCollisionBoundaries().getBoundaries().get(ZERO).length());
        for (Gadget g: gadgets) {
            switch (g.getCollisionBoundaries().getShape()) {
            case CIRCLE:
                {
                    physics.Circle bumperCircle = new physics.Circle(g.getOrigin(), g.getCollisionBoundaries().getBoundaries().get(ZERO).length());
                    times.put(g, physics.Physics.timeUntilCircleCollision(bumperCircle, ballCircle, ball.getVelocity()));
                    continue;
                }
            default:
                {
                    bestTime = physics.Physics.timeUntilWallCollision(
                            g.getCollisionBoundaries().getBoundaries().get(ZERO), ballCircle, ball.getVelocity()
                    );
                    for (physics.LineSegment wall : g.getCollisionBoundaries().getBoundaries()) {
                        bestTime = Math.min(bestTime, physics.Physics.timeUntilWallCollision(
                            wall, ballCircle, ball.getVelocity()));
                    }
                    times.put(g, bestTime);
                    continue;
                }
            }
        }
        checkRep();
        return times;
    }
    
    /**
     * Acquire the next wall for a ball to collide with each gadget
     * @param ball to analyze
     * @return mapping from gadgets to its first wall the ball will collide with
     */
    private Map<Gadget, physics.LineSegment> collisionWalls(Ball ball) {
        HashMap<Gadget, physics.LineSegment> walls = new HashMap<Gadget, physics.LineSegment>();
        double bestTime;
        physics.LineSegment bestWall;
        physics.Circle ballCircle = new physics.Circle(ball.getOrigin(), ball.getCollisionBoundaries().getBoundaries().get(ZERO).length());
        for (Gadget g: gadgets) {
            switch (g.getCollisionBoundaries().getShape()) {
            case CIRCLE:
                {
                    walls.put(g, new physics.LineSegment(g.getOrigin(), g.getOrigin()));
                    continue;
                }
            default:
                {
                    bestTime = physics.Physics.timeUntilWallCollision(
                            g.getCollisionBoundaries().getBoundaries().get(ZERO), ballCircle, ball.getVelocity()
                    );
                    bestWall = g.getCollisionBoundaries().getBoundaries().get(ZERO);
                    for (physics.LineSegment wall : g.getCollisionBoundaries().getBoundaries()) {
                        if (bestTime > physics.Physics.timeUntilWallCollision(
                                wall, ballCircle, ball.getVelocity())) {
                            bestTime = Math.min(bestTime, physics.Physics.timeUntilWallCollision(
                                    wall, ballCircle, ball.getVelocity()));
                                bestWall = wall;
                        }
                    }
                    walls.put(g, bestWall);
                    continue;
                }
            }
        }
        checkRep();
        return walls;
    }
    
    /**
     * Finds updated velocity of ball using physical constants
     */
    private physics.Vect newVelocity(Ball ball, double timedelta) {
        physics.Vect velocity = ball.getVelocity();
        velocity = velocity.minus(new physics.Vect(ZERO, -gravity).times(timedelta));
        physics.Vect newVelocity = velocity.times(
                ONE - friction1 * timedelta - friction2 * velocity.length() * timedelta);
        return newVelocity;
    }
    
    /**
     * Move ball over some amount of time
     */
    private static physics.Vect moveBall(Ball ball, double timestep) {
        physics.Vect velocity = ball.getVelocity();
        physics.Vect newCenter = ball.getOrigin().plus(velocity.times(timestep));
        return newCenter;
    }
    
    /**
     * Returns the minimum of some input list of doubles
     */
    private static double minDouble(List<Double> l) {
        double min = MINUS_ONE;
        for (double d: l) {
            if (min < ZERO)
                min = d;
            min = Math.min(min, d);
        }
        return min;
    }
    
    /**
     * Helper function to convert a collect to a list
     */
    private static List<Double> collectionToList(Collection<Double> c) {
        ArrayList<Double> list = new ArrayList<Double>();
        for (double d : c) {
            list.add(d);
        }
        return list;
    }
    
    /**
     * Helper function to isolate drawing functionality of the board
     * @param g2 the graphics2D object to draw on
     */
    public void draw(Graphics2D g2) {
        // fill the background to erase everything
        g2.setColor(Color.black);
        g2.fill(new Rectangle2D.Double(ZERO, ZERO, DRAWING_AREA_SIZE_IN_PIXELS, DRAWING_AREA_SIZE_IN_PIXELS));

        for (Gadget gadget : gadgets) {
            gadget.draw(g2);
        }
        
        for (Ball b: balls) {
            b.draw(g2);
        }
        checkRep();
    }
    
    /**
     * Update the board UI with the current state of the Flingball board
     */
    public void redraw() {
        JFrame window = new JFrame("Flingball!");
        JPanel drawingArea = new JPanel();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        drawingArea.setPreferredSize(new Dimension(DRAWING_AREA_SIZE_IN_PIXELS, DRAWING_AREA_SIZE_IN_PIXELS));
        window.add(drawingArea);
        window.pack();
        window.setVisible(true);
        
        long interval = (long) timestep;
        int count = ZERO;
        try {
            while (true) {
                count += 1;
                Graphics g = drawingArea.getGraphics();
                Graphics2D g2 = (Graphics2D) g;  // every Graphics object is also a Graphics2D, which is a stronger spec
                
                draw(g2);
                Thread.sleep(interval);
                nextStep();
                if (count > 10) assert Math.abs(interval - 50) <= 1; // TODO: delete this line (for debugging)
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * observer to see balls in board
     * @return list of balls
     */
    public List<Ball> getBalls() {
        return Collections.unmodifiableList(balls);
    }
    
    /**
     * An observer to see gadgets in board
     * @return list of gadgets
     */
    public List<Gadget> getGadgets() {
        return Collections.unmodifiableList(gadgets);
    }
    
    /**
     * An observer to see gravity in board
     * @return the gravity
     */
    public double getGravity() {
        return gravity;
    }
    
    /**
     * An observer to see friction1 in board
     * @return the friction1
     */
    public double getFriction1() {
        return friction1;
    }
    
    /**
     * An observer to see friction2 in board
     * @return the friction2
     */
    public double getFriction2() {
        return friction2;
    }

}
