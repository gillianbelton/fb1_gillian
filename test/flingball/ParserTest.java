package flingball;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import edu.mit.eecs.parserlib.UnableToParseException;

/**
 * Tests for BoardParser.
 */
public class ParserTest {
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    /*
     * Testing strategy for Parser
     * 
     * Partition the inputs:
     *     Test the following properties on the FlingballBoard:
     *         board:
     *             gravity: default, 0, > 0 non-default
     *             friction1: default, 0, > 0 non-default
     *             friction2: default, 0, > 0 non-default
     *         balls: 0, 1, >1
     *         gadgets (overall): 0, 1, >1
     *             square: 0, 1, >1
     *             circle: 0, 1, >1
     *             triangle: 0, 1, >1
     *                 orientations: 0, 90, 180, 270, and not given (defaults 0)
     *             absorber: 0, 1, >1
     *                 k, m = 1,
     *                 k, m > 1 and < 20
     *                 k, m = 20
     *                 k = m
     *                 k != m
     *             **include placing these on the wall edges and in the center of the board
     *         triggers: 0, 1, >1
     *             **including a self-trigger
     *
     * Cover all parts of the partition
     */
    
    
    private final String test1 = "test/flingball/test1.fb";
    private final String test2 = "test/flingball/test2.fb";
    private final String absorber = "test/flingball/absorber.fb";
    private final String absorber2 = "test/flingball/absorber2.fb";
    private final double ERROR = .01;
    
    
    // This test covers a board with: default gravity, default friction1, and default friction2,
    //                                1 ball, 0 gadgets (0 squares, circles, triangles and absorbers),
    //                                0 triggers
    @Test
    public void test1() throws IOException {
        System.out.println("------------------test1---------------------");
        try {
            FlingballBoard board = BoardParser.parse(test1);
            assertEquals("Expected one ball", 1, board.getBalls().size());
            assertEquals("Expected no gadgets", 4, board.getGadgets().size());
            assertEquals("Expected default gravity", 25.0, board.getGravity(), ERROR);
            assertEquals("Expected default friction1", 0.025, board.getFriction1(), ERROR);
            assertEquals("Expected default friction2", 0.025, board.getFriction2(), ERROR);
        } catch (UnableToParseException e) {
            e.printStackTrace();
        }
    }
    
    // This test covers a board with: non-default gravity=30, non-default friction1=0.035,
    //                                non-default friction2=0.045, 0 balls, >1 gadgets, 1 trigger
    //                                1 square, 1 circle, 1 triangle (default orientation)
    //                                1 self-triggering absorber where k=m and k&m = 1
    //                                
    @Test
    public void test2() throws IOException {
        System.out.println("-----------------test2-------------------");
        try {
            FlingballBoard board = BoardParser.parse(test2);
            assertEquals("Expected no balls", 0, board.getBalls().size());
            assertEquals("Expected 4 gadgets", 8, board.getGadgets().size());
            assertEquals("Expected gravity = 30.0", 30.0, board.getGravity(), ERROR);
            assertEquals("Expected friction1 = 0.035", 0.035, board.getFriction1(), ERROR);
            assertEquals("Expected friction2 = 0.045", 0.045, board.getFriction2(), ERROR);
        } catch (UnableToParseException e) {
            e.printStackTrace();
        }
    }
    
    
    // This test covers a board with: gravity=25, >1 ball, >1 trigger
    //                                0 squares, >1 circle, 1 triangle (default orientation)
    //                                >1 absorbers where k != m and k&m > 1
    //                                >1 trigger
    //                                
    @Test
    public void testAbsorber() throws IOException {
        System.out.println("----------------absorber--------------------");
        try {
            FlingballBoard board = BoardParser.parse(absorber);
            assertEquals("Expected 3 balls", 3, board.getBalls().size());
            assertEquals("Expected 10 gadgets", 14, board.getGadgets().size());
            assertEquals("Expected default gravity", 25.0, board.getGravity(), ERROR);
            assertEquals("Expected default friction1", 0.025, board.getFriction1(), ERROR);
            assertEquals("Expected default friction2", 0.025, board.getFriction2(), ERROR);
        } catch (UnableToParseException e) {
            e.printStackTrace();
        }
    }
    
    
    // This test covers a board with: gravity=25, >1 ball, >1 trigger
    //                                >1 square, >1 circle, >1 triangle (covers all orientations)
    //                                >1 absorbers where k&m = 20
    //                                
    @Test
    public void testAbsorber2() throws IOException {
        System.out.println("-----------------absorber2-------------------");
        try {
            FlingballBoard board = BoardParser.parse(absorber2);
            assertEquals("Expected 2 balls", 2, board.getBalls().size());
            assertEquals("Expected 7 gadgets", 11, board.getGadgets().size());
            assertEquals("Expected default gravity", 25.0, board.getGravity(), ERROR);
            assertEquals("Expected default friction1", 0.025, board.getFriction1(), ERROR);
            assertEquals("Expected default friction2", 0.025, board.getFriction2(), ERROR);
        } catch (UnableToParseException e) {
            e.printStackTrace();
        }
    }
    
    
}
