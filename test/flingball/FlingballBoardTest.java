package flingball;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import edu.mit.eecs.parserlib.UnableToParseException;

public class FlingballBoardTest {
    
    private final Ball ball = new Ball("testBall", new physics.Vect(10,10), new physics.Vect(1,1));
    private final Gadget circleBumper = new CircleBumper("testCircleBumper", new physics.Vect(10, 15));
    private final int timestep = 50;
    private final double friction1 = 0.025;
    private final double friction2 = 0.025;
    private final double gravity = 25.0;
    private final String test1 = "test/flingball/test1.fb";
    
    private final double ERROR = .01;

    
    /*
     * Testing strategy for FlingballBoard
     * 
     * Constructor:
     *  -contains ball = T, F
     *  -number of gadets = 0, 1, 2+
     *  -timestep is custom = T, F 
     *  -friction1 is custom = T, F 
     *  -friction2 is custom = T, F 
     *  -gravity is custom = T, F 
     *  
     *  NextStep:
     *  -contains ball
     *  -number of gadets = 0, 1, 2+
     *  -timestep is custom = T, F 
     *  -friction1 is custom = T, F 
     *  -friction2 is custom = T, F 
     *  -gravity is custom = T, F 
     *  
     *  For redraw(), we will use visual testing
     *  Note: by default the tests do not include this since
     *        redraw includes an infinite while loop for visualization
     *        --> Add redraw in order to see the visual testing
     *        
     *      -look for appropriate collisions with gadgets
     *      -look for expected collision angles
     *      -observe gravity affects when gravity tested at:
     *          -5, 25, and 50. 
     *      -observe different frictions when tested at:
     *          -0, 0.025, 0.5
     *          -both for friction1 and friction2
     *       
     *  -The other functions are getters which are trivial and
     *  private methods which dont need to be tested. 
     * 
     */
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    /*
     * Tests intialization of new Board with a manual creation
     * of a new board (tested as outlined above)
     */
    @Test
    public void testNewBoardManual() throws IOException {
        FlingballBoard board = new FlingballBoard(
                Arrays.asList(circleBumper),
                Arrays.asList(ball),
                timestep,
                friction1,
                friction2,
                gravity);
        assertEquals("Expected one ball", 1, board.getBalls().size());
        assertEquals("Expected one gadget", 1+4, board.getGadgets().size());
        assertEquals("Expected default gravity", gravity, board.getGravity(), ERROR);
        assertEquals("Expected default friction1", friction1, board.getFriction1(), ERROR);
        assertEquals("Expected default friction2", friction2, board.getFriction2(), ERROR);
    }
    
    /*
     * Tests intialization of new Board via the parser
     * (tested as outlined above)
     */
    @Test
    public void testNewBoardParse() throws IOException {
        try {
            FlingballBoard board = BoardParser.parse(test1);
            assertEquals("Expected one ball", 1, board.getBalls().size());
            assertEquals("Expected no gadgets", 4, board.getGadgets().size());
            assertEquals("Expected default gravity", gravity, board.getGravity(), ERROR);
            assertEquals("Expected default friction1", friction1, board.getFriction1(), ERROR);
            assertEquals("Expected default friction2", friction2, board.getFriction2(), ERROR);
        } catch (UnableToParseException e) {
            e.printStackTrace();
        }
    }

    /*
     * Tests nextStep() method (as outlined above)
     */
    @Test
    public void testNextStep() throws IOException {
        try {
            FlingballBoard board = BoardParser.parse(test1);
            board.nextStep();
            assertEquals("Expected one ball", 1, board.getBalls().size());
            assertEquals("Expected different position", 1.63, board.getBalls().get(0).getOrigin().x(), ERROR);
            assertEquals("Expected different position", 4.385, board.getBalls().get(0).getOrigin().y(), ERROR);
            
            board.nextStep();
            assertEquals("Expected one ball", 1, board.getBalls().size());
            assertEquals("Expected different position", 1.46097, board.getBalls().get(0).getOrigin().x(), ERROR);
            assertEquals("Expected different position", 4.3328, board.getBalls().get(0).getOrigin().y(), ERROR);
        } catch (UnableToParseException e) {
            e.printStackTrace();
        }
    }
    
}
