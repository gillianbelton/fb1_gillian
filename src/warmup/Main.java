package warmup;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        
        Ball ball = new Ball(.01, 0.01, 5, 10, .2); // vx, vy, coord_x, coord_y, radius

        
        physics.Vect topLeftVect = new physics.Vect(0, 0);
        physics.Vect topRightVect = new physics.Vect(20, 0);
        physics.Vect bottomRightVect = new physics.Vect(20, 20);
        physics.Vect bottomLeftVect = new physics.Vect(0, 20);
        
        physics.LineSegment top = new physics.LineSegment(topLeftVect, topRightVect);
        physics.LineSegment right = new physics.LineSegment(topRightVect, bottomRightVect);
        physics.LineSegment bottom = new physics.LineSegment(bottomLeftVect, bottomRightVect);
        physics.LineSegment left = new physics.LineSegment(bottomLeftVect, topLeftVect);
        
        List<physics.LineSegment> walls = Arrays.asList(top, right, bottom, left);
                
        Board board = new Board(ball, walls);
        
        System.out.println("start: " + board.printBoard());
        
        for (int i = 0; i < 40; i++) {
            board.nextMove();
            System.out.println("timestep: " + i + ": " + board.printBoard());
        }
        //board.draw();
                
    }
    
}
