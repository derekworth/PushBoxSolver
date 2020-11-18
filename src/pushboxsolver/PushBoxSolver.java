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
    //private final LinkedList<Configuration> configHistory;
    private final Stack<Move> moveHistory;
    private int boxesAtDest = 0;
    
    // Move directions
    final int RIGHT = 1;
    final int UP    = 2;
    final int LEFT  = 3;
    final int DOWN  = 4;
    
    // Maximum depth of move history
    final int MAX_DEPTH = 85;

    public static void main(String[] args) {
        PushBoxSolver pbs = new PushBoxSolver();
    }
    
    public PushBoxSolver() {
        int playerCount = 0;
        int boxCount = 0;
        int destCount = 0;
        
        // Initialize variables
        moveHistory   = new Stack<>();
        //configHistory = new LinkedList<>();
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
        Configuration startingConfig = new Configuration(player, 0);
        boxes.forEach((box) -> { startingConfig.addBox(box); });
        //configHistory.add(startingConfig);
        
        // Initiate recursive solution
        solveGame();
    }
    
    public void solveGame() {
        printGame("Starting Configuration");
        
        System.out.println("Please wait... generating solution.");
        // recursively try all four directions for each move
        solveGameHelper(new Location(player.getX()+1, player.getY()  )); // send player to RIGHT
        solveGameHelper(new Location(player.getX()-1, player.getY()  )); // send player to LEFT
        solveGameHelper(new Location(player.getX()  , player.getY()+1)); // send player to DOWN
        solveGameHelper(new Location(player.getX()  , player.getY()-1)); // send player to UP
//        System.out.println("Total configurations explored: " + configHistory.size());
    }
    
    public void pauseBreak() {
        System.out.println("STOP!");
    }
    
    /**
     * This method recursively moves the player around the board until the game is solved
     * @param l1 next location of the player
     */
    public void solveGameHelper(Location l1) {
        if(moveHistory.size() > MAX_DEPTH) return;
        
        //printMoveHistory();
//        if(!currConfig.equals(this.getCurrConfig())) {
//            //printGame("" + this.configHistory.size());
//            printMoveHistory();
//            currConfig = this.getCurrConfig();
//        }
        
//        if(configHistory.size()==5931) {
//            pauseBreak();
//            printGame("Config 5931");
//        }
        // establish direction
        int xDiff = l1.getX() - player.getX();
        int yDiff = l1.getY() - player.getY();
        int direction;
        
        // l = location in direction of travel; s = sides of l2; d = next directions of travel
        Location pl, l2, l3, s1, s2, d1, d2;
        pl = new Location(player.getX(), player.getY());
        
        if(        xDiff == -1 && yDiff ==  0) { // move left
            direction = LEFT;
            l2 = new Location(l1.getX()-1, l1.getY()  );
            l3 = new Location(l2.getX()-1, l2.getY()  );
            s1 = new Location(l2.getX()  , l2.getY()+1);
            s2 = new Location(l2.getX()  , l2.getY()-1);
            d1 = new Location(l1.getX()  , l1.getY()+1);
            d2 = new Location(l1.getX()  , l1.getY()-1);
        } else if (xDiff ==  1 && yDiff ==  0) { // move right
            direction = RIGHT;
            l2 = new Location(l1.getX()+1, l1.getY()  );
            l3 = new Location(l2.getX()+1, l2.getY()  );
            s1 = new Location(l2.getX()  , l2.getY()-1);
            s2 = new Location(l2.getX()  , l2.getY()+1);
            d1 = new Location(l1.getX()  , l1.getY()-1);
            d2 = new Location(l1.getX()  , l1.getY()+1);
        } else if (xDiff ==  0 && yDiff == -1) { // move up
            direction = UP;
            l2 = new Location(l1.getX()  , l1.getY()-1);
            l3 = new Location(l2.getX()  , l2.getY()-1);
            s1 = new Location(l2.getX()-1, l2.getY()  );
            s2 = new Location(l2.getX()+1, l2.getY()  );
            d1 = new Location(l1.getX()-1, l1.getY()  );
            d2 = new Location(l1.getX()+1, l1.getY()  );
        } else if (xDiff ==  0 && yDiff ==  1) { // move down
            direction = DOWN;
            l2 = new Location(l1.getX()  , l1.getY()+1);
            l3 = new Location(l2.getX()  , l2.getY()+1);
            s1 = new Location(l2.getX()+1, l2.getY()  );
            s2 = new Location(l2.getX()-1, l2.getY()  );
            d1 = new Location(l1.getX()+1, l1.getY()  );
            d2 = new Location(l1.getX()-1, l1.getY()  );
        } else {                                 // invalid move
            return;
        }
        
        /**
         * _____________________
         * |    | d1 | s1 |    |
         * _____________________
         * | pl | l1 | l2 | l3 |
         * _____________________
         * |    | d2 | s2 |    |
         * _____________________
         */
        // check if move is valid (no wall, no trap, no loop)
        if(isType(l1, SPACE) || isType(l1, DEST)) {                                                        // SITUATION 1: move player to empty space
            // create new config
            Configuration config = getNewConfig(l1, direction);
            
            // check if new config exists already
//            if(!isPrevConfig(config)) {
                
                // add config to history  (linked list)
//                addConfigToHistory(config);
                
                // add move to history (stack)
                moveHistory.add(new Move(direction, false));
                
                // update new player location on gameboard
                if(isType(l1, DEST)) {
                    gameBoard[l1.getY()][l1.getX()] = DPLAYER;
                } else {
                    gameBoard[l1.getY()][l1.getX()] = PLAYER;
                }
                // update old player location on gameboard
                if(isType(pl, DPLAYER)) {
                    gameBoard[pl.getY()][pl.getX()] = DEST;
                } else {
                    gameBoard[pl.getY()][pl.getX()] = SPACE;
                }
                
                // update player location
                player.setXY(l1.getX(), l1.getY());
                
                // recursively call the next three directions (searching for a solution)
                solveGameHelper(d1);
                solveGameHelper(d2);
                solveGameHelper(l2);
                
                // back out of this direction (i.e. one of the above paths did not result in a solution)
                
                // pop move off history (stack)
                popMoveFromHistory();
                
                // revert new player location on gameboard
                if(isType(l1, DPLAYER)) {
                    gameBoard[l1.getY()][l1.getX()] = DEST;
                } else {
                    gameBoard[l1.getY()][l1.getX()] = SPACE;
                }
                // revert old player location on gameboard
                if(isType(pl, DEST)) {
                    gameBoard[pl.getY()][pl.getX()] = DPLAYER;
                } else {
                    gameBoard[pl.getY()][pl.getX()] = PLAYER;
                }
                
                // revert player location
                player.setXY(pl.getX(), pl.getY());
//            }
        } else if((isType(l1, DBOX) || isType(l1, BOX)) && 
                   isType(l2, DEST)) {                                                                    // SITUATION 2: push box to destination
            // create new config
            Configuration config = getNewConfig(l1, l2, direction);
            
            // check if new config exists already
//            if(!isPrevConfig(config)) {
                
                // add config to history  (linked list)
//                addConfigToHistory(config);
                
                // add move to history (stack)
                moveHistory.add(new Move(direction, true));
                
                // update new box location on gameboard
                gameBoard[l2.getY()][l2.getX()] = DBOX;
                boxesAtDest++;
                // update new player location on gameboard
                if(isType(l1, DBOX)) {
                    gameBoard[l1.getY()][l1.getX()] = DPLAYER;
                    boxesAtDest--;
                } else {
                    gameBoard[l1.getY()][l1.getX()] = PLAYER;
                }
                // update old player location on gameboard
                if(isType(pl, DPLAYER)) {
                    gameBoard[pl.getY()][pl.getX()] = DEST;
                } else {
                    gameBoard[pl.getY()][pl.getX()] = SPACE;
                }
                
                // update player location
                player.setXY(l1.getX(), l1.getY());
                // update box location
                for(Location box : boxes) {
                    if(box.equals(l1)) {
                        box.setXY(l2.getX(), l2.getY());
                        break;
                    }
                }
                
                if(this.isGameOver()) {
                    printMoveHistory();
                    System.exit(0);
                }
                
                // recursively call the next three directions (searching for a solution)
                solveGameHelper(d1);
                solveGameHelper(d2);
                solveGameHelper(l2);
                
                // back out of this direction (i.e. one of the above paths did not result in a solution)
                
                // pop move off history (stack)
                popMoveFromHistory();
                
                // revert new box location on gameboard
                gameBoard[l2.getY()][l2.getX()] = DEST;
                boxesAtDest--;
                // revert new player location on gameboard
                if(isType(l1, DPLAYER)) {
                    gameBoard[l1.getY()][l1.getX()] = DBOX;
                    boxesAtDest++;
                } else {
                    gameBoard[l1.getY()][l1.getX()] = BOX;
                }
                // revert old player location on gameboard
                if(isType(pl, DEST)) {
                    gameBoard[pl.getY()][pl.getX()] = DPLAYER;
                } else {
                    gameBoard[pl.getY()][pl.getX()] = PLAYER;
                }
                
                // revert player location
                player.setXY(pl.getX(), pl.getY());
                // update box location
                for(Location box : boxes) {
                    if(box.equals(l2)) {
                        box.setXY(l1.getX(), l1.getY());
                        break;
                    }
                }
//            }
        } else if((isType(l1, DBOX) || isType(l1, BOX)) && 
                   isType(l2, SPACE) && 
                 ((isType(l3, SPACE) || isType(l3, DEST)) ||                                              // SITUATION 3: push box to empty space (with parallel outlet)
                 ((isType(s1, SPACE) || isType(s1, DEST)) && (isType(s2, SPACE) || isType(s2, DEST))))) { // SITUATION 4: push box to empty space (with perpendicular outlet)
            // create new config
            Configuration config = getNewConfig(l1, l2, direction);
            
            // check if new config exists already
//            if(!isPrevConfig(config)) {
                
                // add config to history  (linked list)
//                addConfigToHistory(config);
                
                // add move to history (stack)
                moveHistory.add(new Move(direction, true));
                
                // update new box location on gameboard
                gameBoard[l2.getY()][l2.getX()] = BOX;
                // update new player location on gameboard
                if(isType(l1, DBOX)) {
                    gameBoard[l1.getY()][l1.getX()] = DPLAYER;
                    boxesAtDest--;
                } else {
                    gameBoard[l1.getY()][l1.getX()] = PLAYER;
                }
                // update old player location on gameboard
                if(isType(pl, DPLAYER)) {
                    gameBoard[pl.getY()][pl.getX()] = DEST;
                } else {
                    gameBoard[pl.getY()][pl.getX()] = SPACE;
                }
                
                // update player location
                player.setXY(l1.getX(), l1.getY());
                // update box location
                for(Location box : boxes) {
                    if(box.equals(l1)) {
                        box.setXY(l2.getX(), l2.getY());
                        break;
                    }
                }
                
                if(this.isGameOver()) {
                    printMoveHistory();
                    System.exit(0);
                }
                
                // recursively call the next three directions (searching for a solution)
                solveGameHelper(d1);
                solveGameHelper(d2);
                solveGameHelper(l2);
                
                // back out of this direction (i.e. one of the above paths did not result in a solution)
                
                // pop move off history (stack)
                popMoveFromHistory();
                
                // revert new box location on gameboard
                gameBoard[l2.getY()][l2.getX()] = SPACE;
                // revert new player location on gameboard
                if(isType(l1, DPLAYER)) {
                    gameBoard[l1.getY()][l1.getX()] = DBOX;
                    boxesAtDest++;
                } else {
                    gameBoard[l1.getY()][l1.getX()] = BOX;
                }
                // revert old player location on gameboard
                if(isType(pl, DEST)) {
                    gameBoard[pl.getY()][pl.getX()] = DPLAYER;
                } else {
                    gameBoard[pl.getY()][pl.getX()] = PLAYER;
                }
                
                // revert player location
                player.setXY(pl.getX(), pl.getY());
                // update box location
                for(Location box : boxes) {
                    if(box.equals(l2)) {
                        box.setXY(l1.getX(), l1.getY());
                        break;
                    }
                }
//            }
        }
    }
    
    public void printMoveHistory() {
        moveHistory.forEach((m) -> {
            System.out.print(m);
        });
        System.out.println();
    }
    
    /**
     * Removes the last added move from the history stack
     * @return True if a box was pushed for the removed move, False otherwise
     */
    public boolean popMoveFromHistory() {
        Move m = moveHistory.pop();
        return m.wasBoxPushed();
    }
    
    public Configuration getNewConfig(Location newPlayerLoc, int dir) {
        Configuration newConfig = new Configuration(newPlayerLoc, dir);
        // add unmoved boxes
        boxes.forEach((box) -> {
            newConfig.addBox(box);
        });
        return newConfig;
    }
    
    public Configuration getNewConfig(Location newPlayerLoc, Location newBoxLoc, int dir) {
        Configuration newConfig = new Configuration(newPlayerLoc, dir);
        // add unmoved boxes
        boxes.stream().filter((box) -> (!newPlayerLoc.equals(box))).forEachOrdered((box) -> {
            newConfig.addBox(box);
        });
        // add moved box
        newConfig.addBox(newBoxLoc);
        return newConfig;
    }
    
    public boolean isType(Location loc, int type) {
        // location off the game board
        if(loc.getX() >= gameBoard[0].length ||
           loc.getY() >= gameBoard.length    ||
           loc.getX() <= -1                  ||
           loc.getY() <= -1) {
            return false;
        }
        
        // location on the game board
        return gameBoard[loc.getY()][loc.getX()]==type;
        
    }
    
    public boolean isDestination(Location loc) {
        return destinations.stream().anyMatch((dest) -> (dest.equals(loc)));
    }
    
//    public void addConfigToHistory(Configuration config) {
//        for(Configuration c : configHistory) {
//            if(c.equals(config)) {
//                return;
//            }
//        }
//        configHistory.add(config);
//    }
    
//    public boolean isPrevConfig(Configuration config) {
//        return configHistory.stream().anyMatch((c) -> (c.equals(config)));
//    }
    
    public Configuration getCurrConfig(int dir) {
        Configuration curr = new Configuration(player, dir);
        boxes.forEach((loc) -> {
            curr.addBox(loc);
        });
        return curr;
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
    
//    final int WALL    = 0;
//    final int SPACE   = 1; // free (unoccupied) space
//    final int BOX     = 2;
//    final int DEST    = 3; // destination
//    final int PLAYER  = 4;
//    final int DBOX    = 5; // box at destination
//    final int DPLAYER = 6; // player at destination
    public void printGame(String title) {
        System.out.println(title);
        String pieces = "# XOP@&";
        for (int[] gameBoard1 : gameBoard) {
            for (int x = 0; x < gameBoard1.length; x++) {
                System.out.print(pieces.charAt(gameBoard1[x]) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
    
    public class Configuration {
        private final int direction;
        private final Location player;
        private final LinkedList<Location> boxes;
        
        public Configuration(Location player, int dir) {
            this.direction = dir;
            this.player = player.makeCopy();
            boxes = new LinkedList<>();
        }
        
        public void addBox(Location box) {
            for(Location b : boxes) {
                if(b.equals(box)) {
                    return; // already added
                }
            }
            boxes.add(box.makeCopy());
        }
        
        public Location getPlayer() {
            return player;
        }
        
        public LinkedList<Location> getBoxes() {
            return boxes;
        }
        
        public int getDirection() {
            return direction;
        }
        
        public boolean contains(Location box) {
            return getBoxes().stream().anyMatch((b) -> (box.equals(b)));
        }
        
        public boolean equals(Configuration config) {
            if(!config.getPlayer().equals(this.player)) {
                return false;
            }
            if(config.getDirection() != this.direction) {
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
        private int x, y;
        
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
        
        public void setXY(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        public boolean equals(Location loc) {
            return loc.getX() == x && loc.getY() == y;
        }
        
        public Location makeCopy() {
            return new Location(x, y);
        }
        
        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }
    
    public class Move {
        
        private final int direction;
        private final boolean boxPushed;
        
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
        
        @Override
        public String toString() {
            switch(direction) {
                case UP:
                    return "U";
                case DOWN:
                    return "D";
                case LEFT:
                    return "L";
                default:
                    return "R";
            }
        }
    }
}
