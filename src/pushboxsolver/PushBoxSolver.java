package pushboxsolver;

import java.util.LinkedList;
import java.util.Stack;

/**
 * Started on 7 Nov
 * @author derekworth
 */
public final class PushBoxSolver {
    
    // Game pieces
    final int WALL    = 0;
    final int SPACE   = 1; // free (unoccupied) space
    final int BOX     = 2;
    final int DEST    = 3; // destination
    final int PLAYER  = 4;
    final int DBOX    = 5; // box at destination
    final int DPLAYER = 6; // player at destination
    
    private int[][] gameBoard = {
                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                {0, 1, 1, 1, 3, 0, 0, 0, 0, 0},
                                {0, 1, 3, 1, 4, 0, 0, 0, 0, 0},
                                {0, 1, 0, 1, 0, 0, 0, 0, 0, 0},
                                {0, 1, 0, 1, 2, 1, 0, 0, 0, 0},
                                {0, 1, 2, 3, 1, 1, 1, 0, 0, 0},
                                {0, 0, 0, 0, 1, 2, 1, 1, 0, 0},
                                {0, 0, 0, 0, 0, 1, 1, 1, 1, 0},
                                {0, 0, 0, 0, 0, 0, 1, 1, 1, 0},
                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
                            };
    
    private Location player;
    private LinkedList<Location> boxes;
    private LinkedList<Location> destinations;
    private final LinkedList<Configuration> configHistory;
    private final Stack<Move> moveHistory;
    private int boxesAtDest = 0;
    
    // Move directions
    final int RIGHT = 1;
    final int UP    = 2;
    final int LEFT  = 3;
    final int DOWN  = 4;

    public static void main(String[] args) {
        PushBoxSolver pbs = new PushBoxSolver();
        pbs.printGame("Starting Configuration:");
    }
    
    public PushBoxSolver() {
        int playerCount = 0;
        int boxCount = 0;
        int destCount = 0;
        
        // Initialize variables
        moveHistory   = new Stack<>();
        configHistory = new LinkedList<>();
        boxes         = new LinkedList<>();
        destinations  = new LinkedList<>();
        
        // Set player, boxes, and destinations (for quick lookups and updates)
        for (int y = 0; y < gameBoard.length; y++) {
            for (int x = 0; x < gameBoard[y].length; x++) {
                switch(gameBoard[y][x]) {
                    case PLAYER : player =         new Location(x, y) ; playerCount++; break;
                    case BOX    : boxes.add(       new Location(x, y)); boxCount++;    break;
                    case DEST   : destinations.add(new Location(x, y)); destCount++;
                }
            }
        }
        
        // Validate game board requirements
        if(playerCount != 1 || boxCount < 1 || boxCount != destCount) {
            System.out.println("ERROR: invalid game board, check for only 1 player and that there is 1 destination for every box.");
            System.exit(0);
        }
        
        // Add staring configuration to history
        Configuration startingConfig = new Configuration(player);
        boxes.forEach((box) -> { startingConfig.addBox(box); });
        configHistory.add(startingConfig);
        
        // Initiate recursive solution
        solveGame();
    }
    
    public void solveGame() {
        // recursively try all four directions for each move
    }
    
    public void solveGameHelper(Location curr) {
        Location up, rt, dn, lt;
        up = new Location(curr.getX()  , curr.getY()-1);
        rt = new Location(curr.getX()+1, curr.getY()  );
        dn = new Location(curr.getX()  , curr.getY()+1);
        lt = new Location(curr.getX()-1, curr.getY()  );
        
    }
    
    public boolean movePlayer(Location loc1) {
        // establish direction
        int xDiff = loc1.getX() - player.getX();
        int yDiff = loc1.getY() - player.getY();
        
        Location loc2, loc3, s1, s2;
        
        if(        xDiff == -1 && yDiff ==  0) { // move left
            loc2 = new Location(loc1.getX()-1, loc1.getY()  );
            loc3 = new Location(loc2.getX()-1, loc2.getY()  );
            s1   = new Location(loc2.getX()  , loc2.getY()+1);
            s2   = new Location(loc2.getX()  , loc2.getY()-1);
        } else if (xDiff ==  1 && yDiff ==  0) { // move right
            loc2 = new Location(loc1.getX()+1, loc1.getY()  );
            loc3 = new Location(loc2.getX()+1, loc2.getY()  );
            s1   = new Location(loc2.getX()  , loc2.getY()-1);
            s2   = new Location(loc2.getX()  , loc2.getY()+1);
        } else if (xDiff ==  0 && yDiff == -1) { // move up
            loc2 = new Location(loc1.getX()  , loc1.getY()-1);
            loc3 = new Location(loc2.getX()  , loc2.getY()-1);
            s1   = new Location(loc2.getX()-1, loc2.getY()  );
            s2   = new Location(loc2.getX()+1, loc2.getY()  );
        } else if (xDiff ==  0 && yDiff ==  1) { // move down
            loc2 = new Location(loc1.getX()  , loc1.getY()+1);
            loc3 = new Location(loc2.getX()  , loc2.getY()+1);
            s1   = new Location(loc2.getX()+1, loc2.getY()  );
            s2   = new Location(loc2.getX()-1, loc2.getY()  );
        } else {                                 // invalid move
            return false;
        }
        
        if(isType(loc1, SPACE) || isType(loc1, DEST)) { // move player to empty space
            
        } else if(isType(loc1, BOX) && 
                  isType(loc2, DEST)) {  // push box to destination
            
        } else if(isType(loc1, BOX) && 
                  isType(loc2, SPACE) && 
                 (isType(loc3, SPACE) || isType(loc3, DEST) || isType(loc3, BOX))) {  // push box to empty space
            
        } else if(isType(loc1, BOX) && isType(loc2, SPACE) && isType(s1, SPACE) && isType(s2, SPACE)) { 
            
        }
        // check if move is valid (no wall, no trap, no loop)
        // add new configuration to history
        // add new move to history
        // move player (and block if applicable) on the gameboard
        // check if game won (print move history if so)
        return true;
    }
    
    public boolean isType(Location loc, int type) {
        return gameBoard[loc.getY()][loc.getX()]==type;
    }
    
    public boolean isDestination(Location loc) {
        for(Location dest : destinations) {
            if(dest.equals(loc)) {
                return true;
            }
        }
        return false;
    }
    
    public void addPrevConfig(Configuration config) {
        for(Configuration c : configHistory) {
            if(c.equals(config)) {
                return;
            }
        }
        configHistory.add(config);
    }
    
    public boolean isPrevConfig(Configuration config) {
        for(Configuration c : configHistory) {
            if(c.equals(config)) {
                return true;
            }
        }
        return false;
    }
    
    public Configuration getCurrConfig() {
        Configuration curr = new Configuration(player);
        for(Location loc : boxes) {
            curr.addBox(loc);
        }
        return null;
    }
    
    public Location getBox(int x, int y) {
        for(Location loc : boxes) {
            if(loc.getX() == x && loc.getY() == y) {
                return loc;
            }
        }
        return null;
    }
    
    public boolean isGameOver() {
        return boxesAtDest == boxes.size();
    }
    
    public void printGame(String title) {
        System.out.println(title);
        String pieces = "# XOP";
        for (int y = 0; y < gameBoard.length; y++) {
            for (int x = 0; x < gameBoard[y].length; x++) {
                System.out.print(pieces.charAt(gameBoard[y][x]) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
    
    public class Configuration {
        Location player;
        LinkedList<Location> boxes;
        
        public Configuration(Location player) {
            this.player = player;
            boxes = new LinkedList<>();
        }
        
        public void addBox(Location box) {
            for(Location b : boxes) {
                if(b.equals(box)) {
                    return; // already added
                }
            }
            boxes.add(box);
        }
        
        public Location getPlayer() {
            return player;
        }
        
        public LinkedList<Location> getBoxes() {
            return boxes;
        }
        
        public boolean contains(Location box) {
            return getBoxes().stream().anyMatch((b) -> (box.equals(b)));
        }
        
        public boolean equals(Configuration config) {
            if(!config.getPlayer().equals(this.player)) {
                return false;
            }
            
            return config.getBoxes().stream().noneMatch((box) -> (!this.contains(box)));
        }
        
        @Override
        public String toString() {
            int boxNum = 1;
            String output = "==================\nPlayer: " + player + "\n==================\nBoxes: ";
            for(Location c : boxes) {
                output += "\n  " + boxNum++ + " - " + c;
            }
            return output;
        }
    }
    
    public class Location {
        int x, y;
        
        public Location(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        public int getX() {
            return x;
        }
        
        public int getY() {
            return y;
        }
        
        public void setX(int x) {
            this.x = x;
        }
        
        public void setY(int y) {
            this.y = y;
        }
        
        public boolean equals(Location loc) {
            return loc.getX() == x && loc.getY() == y;
        }
        
        @Override
        public String toString() {
            return "(" + (x+1) + ", " + (y+1) + ")";
        }
    }
    
    public class Move {
        
        int direction;
        boolean boxPushed;
        
        public Move(int dir, boolean push) {
            direction = dir;
            boxPushed = push;
        }
        
        public int getDirection() {
            return direction;
        }
        
        public boolean wasBoxPushed() {
            return boxPushed;
        }
    }
}
