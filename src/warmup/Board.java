package warmup;

import java.text.DecimalFormat;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Board {
    private final Ball ball;
    private final List<physics.LineSegment> walls;
    private final int timestep = 50; // milliseconds
    
    private static final int GAMEBOARD_SIZE = 20;
    private static final int PIXELS_PER_L = 20;
    private static final int DRAWING_AREA_SIZE_IN_PIXELS = GAMEBOARD_SIZE * PIXELS_PER_L;
    
    public Board(Ball ball, List<physics.LineSegment> walls) {
        this.ball = ball;
        this.walls = walls;
    }
    
    public void nextMove() { 
        double timeUntilCollision = timeUntilCollision();

        if (timeUntilCollision < timestep) { 
            
            physics.LineSegment nextWallCollision = walls.get(nextWallCollision());
            
            // move ball to the first wall
            double newX = ball.getVelocity().x() * timeUntilCollision + ball.getCenter().x();
            double newY = ball.getVelocity().y() * timeUntilCollision + ball.getCenter().y();
            ball.setCenter(new physics.Vect(newX, newY));
            
            // calculate new velocity after collision with first wall
            physics.Vect newVelocity = physics.Physics.reflectWall(nextWallCollision, ball.getVelocity());
            ball.setVelocity(newVelocity);
            
            // how much time left after first collision
            double timeLeft = timestep - timeUntilCollision;
            
            double timeUntilNextCollision = timeUntilCollision();
            // check for second wall collision
            if (timeUntilNextCollision < timeLeft) {
                physics.LineSegment secondWallCollision = walls.get(nextWallCollision());
                
                // move ball to the second wall
                double thirdX = ball.getVelocity().x() * timeUntilNextCollision + ball.getCenter().x();
                double thirdY = ball.getVelocity().y() * timeUntilNextCollision + ball.getCenter().y();
                ball.setCenter(new physics.Vect(thirdX, thirdY));
                
                // calculate new velocity after collision with second wall
                physics.Vect thirdVelocity = physics.Physics.reflectWall(secondWallCollision, ball.getVelocity());
                ball.setVelocity(thirdVelocity);
                
                timeLeft = timeLeft - timeUntilNextCollision;
            }
            
            // no more hitting walls
            double finalX = ball.getVelocity().x() * timeLeft + ball.getCenter().x();
            double finalY = ball.getVelocity().y() * timeLeft + ball.getCenter().y();
            ball.setCenter(new physics.Vect(finalX, finalY));
            

        } else {
            physics.Vect newCenter = new physics.Vect(ball.getVelocity().x() * timestep + ball.getCenter().x(),
                                                      ball.getVelocity().y() * timestep + ball.getCenter().y());
            ball.setCenter(newCenter);
        }
    }
    
    public double timeUntilCollision() {
        ArrayList<Double> times = new ArrayList<Double>();
        for (physics.LineSegment wall: walls) {
            times.add(physics.Physics.timeUntilWallCollision(wall, ball.getCircle(), ball.getVelocity()));
        }
        double minTime = times.get(0);
        for (int i = 1; i < times.size(); i++) {
            if (times.get(i) < minTime) {
                minTime = times.get(i);
            }
        }
        return minTime;
    }
    
    /**
     * @return the wall that is next up for collision
     */
    public int nextWallCollision() {
        ArrayList<Double> times = new ArrayList<Double>();
        for (physics.LineSegment wall: walls) {
            times.add(physics.Physics.timeUntilWallCollision(wall, ball.getCircle(), ball.getVelocity()));
        }
        int wallIndex = 0;
        
        for (int i = 1; i < times.size(); i++) {
            if (times.get(i) < times.get(wallIndex)) {
                wallIndex = i;
            }
        }
        return wallIndex;
    }
    
    public String printBoard() {
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        return "Ball Coords: (" + numberFormat.format(ball.getCenter().x())
                                + ", " + numberFormat.format(ball.getCenter().y()) + ")";
    }
    
    // I think we can just call draw() whenever we want to draw the board
    public void draw() {
        final JFrame window = new JFrame("Pls work");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        final JPanel drawingArea = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                drawBoard(g);
            }
        };
        
        drawingArea.setPreferredSize(new Dimension(DRAWING_AREA_SIZE_IN_PIXELS, DRAWING_AREA_SIZE_IN_PIXELS));
        window.add(drawingArea);
        window.pack();
        window.setVisible(true);
        // note: the time must be javax.swing.Timer, not java.util.Timer
        new Timer(timestep, (ActionEvent e) -> {
            drawingArea.repaint();
        }).start();
    }
    
    public void drawBoard(final Graphics g) {
        Graphics2D g2 = (Graphics2D) g;  // every Graphics object is also a Graphics2D, which is a stronger spec
        
        // fill the background to erase everything
        g2.setColor(Color.black);
        g2.fill(new Rectangle2D.Double(0, 0, DRAWING_AREA_SIZE_IN_PIXELS, DRAWING_AREA_SIZE_IN_PIXELS));

        // move the origin of drawing, so that we don't have to add smileX+ and smileY+ to every drawing call below. 
        g2.translate(ball.getCenter().x(), ball.getCenter().y());        
        System.out.println(ball.getCenter().x());
        // fill the face with yellow
        g2.setColor(Color.yellow);
        g2.fill(new Ellipse2D.Double(0, 0, ball.getRadius(), ball.getRadius()));
    }
    
}
