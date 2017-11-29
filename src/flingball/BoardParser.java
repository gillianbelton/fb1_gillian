package flingball;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.mit.eecs.parserlib.ParseTree;
import edu.mit.eecs.parserlib.Parser;
import edu.mit.eecs.parserlib.UnableToParseException;


public class BoardParser {
    
    private static Parser<BoardGrammar> parser = makeParser();
    private static final int TIMESTEP = 50;
    private static final double FRICTION_DEFAULT = 0.025;
    private static final double GRAVITY_DEFAULT = 25.0;
    
    private static final int ORIENTATION_DEFAULT = 0;
    
    private static final int CHILD_ZERO = 0;
    private static final int CHILD_ONE = 1;
    private static final int CHILD_TWO = 2;
    private static final int CHILD_THREE = 3;
    private static final int CHILD_FOUR = 4;
    
    // the nonterminals of the grammar
    private enum BoardGrammar {
        EXPRESSION, SQUARE_BUMPER, TRIANGLE_BUMPER, CIRCLE_BUMPER, ABSORBER, FIRE, BALL, DEFINITION,
        INTEGER, FLOAT, NAME, COMMENT, BOARD, WHITESPACE, ORIENTATION, G, F1, F2
    }
    

    /**
     * Compile the grammar into a parser.
     * 
     * @param grammarFilename <b>Must be in this class's Java package.</b>
     * @return parser for the grammar
     * @throws RuntimeException if grammar file can't be read or has syntax errors
     */
    private static Parser<BoardGrammar> makeParser() {
        try {
            // read the grammar as a file, relative to the project root.
            final File grammarFile = new File("src/flingball/FlingballGrammar.g");
            return Parser.compile(grammarFile, BoardGrammar.EXPRESSION);
        } catch (IOException e) {
            throw new RuntimeException("can't read the grammar file", e);
        } catch (UnableToParseException e) {
            throw new RuntimeException("the grammar has a syntax error", e);
        }
    }
    

    
    /**
     * Parse a file, line by line, to construct a board with the specified gadgets and actions
     * @param file filepath of file to parse
     * @return FlingballBoard parsed from the file
     * @throws UnableToParseException if the file doesn't match the BoardExpression grammar
     * @throws IOException if the file has an I/O error.
     */
    public static FlingballBoard parse(final String file) throws UnableToParseException, IOException {
        // parse the example into a parse tree
        File boardFile = new File(file);
        final ParseTree<BoardGrammar> parseTree = parser.parse(boardFile);

        // make an AST from the parse tree
        final FlingballBoard board = generateBoard(parseTree);
        System.out.println("AST " + board);       
        return board;
    }
    
    private static FlingballBoard generateBoard(final ParseTree<BoardGrammar> parseTree) {
        Map<String, List<String>> actions = new HashMap<String, List<String>>();
        List<Gadget> gadgets = new ArrayList<Gadget>();
        List<Ball> balls = new ArrayList<Ball>();
        BoardValue friction1 = new BoardValue(FRICTION_DEFAULT);
        BoardValue friction2 = new BoardValue(FRICTION_DEFAULT);
        BoardValue gravity = new BoardValue(GRAVITY_DEFAULT);
        
        makeAbstractSyntaxTree(parseTree, actions, gadgets, balls, friction1, friction2, gravity);
        addActions(gadgets, actions);
        return new FlingballBoard(gadgets, balls, TIMESTEP, friction1.getValue(), friction2.getValue(), gravity.getValue());
    }
    
    
    /**
     * Convert a parse tree into an abstract syntax tree.
     * 
     * @param parseTree constructed according to the grammar in Board.g
     */
    private static void makeAbstractSyntaxTree(
            final ParseTree<BoardGrammar> parseTree,
            Map<String, List<String>> actions,
            List<Gadget> gadgets,
            List<Ball> balls,
            BoardValue friction1,
            BoardValue friction2,
            BoardValue gravity) {        

                        
        switch (parseTree.name()) {
        case EXPRESSION: // EXPRESSION ::= DEFINITION *;
            {
                final List<ParseTree<BoardGrammar>> children = parseTree.children();
                for (ParseTree<BoardGrammar> child : children) {
                    makeAbstractSyntaxTree(child, actions, gadgets, balls, friction1, friction2, gravity);
                }
                break;
            }
        
        case DEFINITION: // DEFINITION ::= BOARD | BALL | SQUAREBUMPER | CIRCLEBUMPER | TRIANGLEBUMPER | ABSORBER | FIRE;
            {
                final ParseTree<BoardGrammar> child = parseTree.children().get(CHILD_ZERO);
                makeAbstractSyntaxTree(child, actions, gadgets, balls, friction1, friction2, gravity);
                break;
            }
        case BOARD: //BOARD ::= 'board' 'name' '=' NAME (G)? (F1)? (F2)?; 
            {
                System.out.println("parsing board");
                final List<ParseTree<BoardGrammar>> children = parseTree.children();
                if (children.size() > 1) {
                    for (ParseTree<BoardGrammar> child : children) {
                        switch(child.name()) {
                            case G: {                        
                                gravity.setValue(Double.parseDouble(child.children().get(CHILD_ZERO).text()));
                                break; 
                            }                   
                            case F1: {
                                friction1.setValue(Double.parseDouble(child.children().get(CHILD_ZERO).text()));
                                break; 
                            }
                            case F2: {
                                friction2.setValue(Double.parseDouble(child.children().get(CHILD_ZERO).text()));
                                break; 
                            }
                            case NAME:
                                break;
                            default:
                                System.out.println("child name: " + child.name());
                                throw new AssertionError("should never get here");
                        }
                    }
                    
                }
                
                break;
            }
    
        case BALL: //BALL ::= name=NAME x=FLOAT y=FLOAT xVelocity=FLOAT yVelocity=FLOAT;           
            {
                System.out.println("parsing ball");
                final List<ParseTree<BoardGrammar>> children = parseTree.children();
                String name = children.get(CHILD_ZERO).text();
                physics.Vect position = new physics.Vect(Float.parseFloat(children.get(CHILD_ONE).text()),
                                                         Float.parseFloat(children.get(CHILD_TWO).text()));
                physics.Vect velocity = new physics.Vect(Float.parseFloat(children.get(CHILD_THREE).text()),
                                                         Float.parseFloat(children.get(CHILD_FOUR).text()));
                Ball newBall = new Ball(name, position, velocity);
                balls.add(newBall);
                break;
            }
        
        case ABSORBER: //ABSORBER ::= absorber name=NAME x=INTEGER y=INTEGER width=INTEGER height=INTEGER;
            
            {
                System.out.println("parsing absorber");
                final List<ParseTree<BoardGrammar>> children = parseTree.children();
                String name = children.get(CHILD_ZERO).text();
                physics.Vect position = new physics.Vect(Integer.parseInt(children.get(CHILD_ONE).text()),
                                                         Integer.parseInt(children.get(CHILD_TWO).text()));
                int width = Integer.parseInt(children.get(CHILD_THREE).text());
                int height = Integer.parseInt(children.get(CHILD_FOUR).text());                      
                Absorber newAbsorber = new Absorber(name, position, width, height);
                gadgets.add(newAbsorber);
                break;
            }
        case FIRE: // FIRE ::= fire trigger=NAME action=NAME;
            {
                System.out.println("parsing fire");
                final List<ParseTree<BoardGrammar>> children = parseTree.children();
                String triggerName = children.get(CHILD_ZERO).text();
                String actionName = children.get(CHILD_ONE).text();
                if (actions.containsKey(triggerName))
                    actions.get(triggerName).add(actionName);
                else
                    actions.put(triggerName, new ArrayList<String>(Arrays.asList(actionName)));
                break;
            }
        
        case CIRCLE_BUMPER: // CIRCLE_BUMPER ::= 'circleBumper' 'name' '=' NAME 'x' '=' INTEGER 'y' '=' INTEGER;
            {
                System.out.println("parsing circle bumper");
                final List<ParseTree<BoardGrammar>> children = parseTree.children();
                String name = children.get(CHILD_ZERO).text();
                physics.Vect position = new physics.Vect(Integer.parseInt(children.get(CHILD_ONE).text()),
                                                         Integer.parseInt(children.get(CHILD_TWO).text()));                     
                CircleBumper newCB = new CircleBumper(name, position);
                gadgets.add(newCB);
                break;
                
            }
        case SQUARE_BUMPER: // SQUARE_BUMPER ::= 'squareBumper' 'name' '=' NAME 'x' '=' INTEGER 'y' '=' INTEGER;
            {
                System.out.println("parsing square bumper");
                final List<ParseTree<BoardGrammar>> children = parseTree.children();
                String name = children.get(CHILD_ZERO).text();
                physics.Vect position = new physics.Vect(Integer.parseInt(children.get(CHILD_ONE).text()),
                                                         Integer.parseInt(children.get(CHILD_TWO).text()));                     
                SquareBumper newSB = new SquareBumper(name, position);
                gadgets.add(newSB);
                break;
            }
        case TRIANGLE_BUMPER: //TRIANGLEBUMPER ::= 'triangleBumper' 'name' '=' NAME 'x' '=' INTEGER 'y' '=' INTEGER ('orientation' '=' ORIENTATION)?;
            {
                System.out.println("parsing triangle bumper");
                final List<ParseTree<BoardGrammar>> children = parseTree.children();
                String name = children.get(CHILD_ZERO).text();
                physics.Vect position = new physics.Vect(Integer.parseInt(children.get(CHILD_ONE).text()),
                                                         Integer.parseInt(children.get(CHILD_TWO).text()));
                int orientation = children.size() == CHILD_FOUR ? Integer.parseInt(children.get(CHILD_THREE).text()) : ORIENTATION_DEFAULT;
                TriangleBumper newTB = new TriangleBumper(name, position, orientation);
                gadgets.add(newTB);  
                break;
            }        
        default:
            throw new AssertionError("should never get here");    
        }
    }

    private static void addActions(List<Gadget> gadgets, Map<String, List<String>> actions) {
        //loop through all gadgets
        for (Gadget gadget: gadgets) {
            if (actions.containsKey(gadget.getName())) {
                //for each other gadget that this gadget's trigger activates
                for (String actionTarget: actions.get(gadget.getName())) {
                    //loop through to find matching gadget object
                    for (Gadget targetGadget: gadgets) {
                        //if this gadget has the name we are looking for add the action link
                        if (targetGadget.getName().equals(actionTarget))
                            gadget.addAction(targetGadget);
                    }
                }
            }
        }
    }
}


final class BoardValue {
    
    private double value;
    
    public BoardValue(double value) {
        this.value = value;
    }
    
    public double getValue() {
        return this.value;
    }
    
    public void setValue(double newValue) {
        this.value = newValue;
    }
}
