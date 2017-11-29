package flingball;

import java.io.IOException;

import edu.mit.eecs.parserlib.UnableToParseException;

// import org.apache.commons.cli.*;

/**
 * Runs the Flingball program.
 */
public class Main {
        
    /**
     * Generate a Flingball game given the input board. If no input board is given,
     * the default.fb board is loaded
     * 
     * @param args command line argument to a .fb file which provides
     *             the board layout as defined in the Board grammar
     */
    public static void main(String[] args) throws UnableToParseException {
        // Default boardFile 
        String boardFile = "test/flingball/absorber2.fb";
        // parse the command line argument
        
        if (args.length > 0) {
            boardFile = args[0];
        }

        
        // load the Flingball object
        try {
            FlingballBoard flingball = BoardParser.parse(boardFile);
            flingball.redraw();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
