import java.util.Scanner;

public class Game {

    // Globals
    public static final boolean DEBUGGING = true;   // Debugging flag.
    public static final int MAX_LOCALES = 2;        // Total number of rooms/locations we have in the game.
    public static final int MAX_SCORE = 40;
    public static int currentLocale = 0;            // Player starts in locale 0.
    public static String command;                   // What the player types as he or she plays the game.
    public static boolean stillPlaying = true;      // Controls the game loop.
    public static Locale[] locations;               // An uninitialized array of type Locale. See init() for initialization.
    public static Items[] itemArray;
    public static int[][]  nav;                     // An uninitialized array of type int int.
    public static int moves = 0;                    // Counter of the player's moves.
    public static int score = 5;                    // Tracker of the player's score.
    public static int item1Collected = 0;
    public static int item2Collected = 0;
    public static int item3Collected = 0;
    public static int item4Collected = 0;
    public static boolean mapAcquired = false;
    
    public static void main(String[] args) {
    	System.out.println("Welcome to the Haunted Game");
        // Set starting locale, if it was provided as a command line parameter.
        if (args.length > 0) {
           try {
              int startLocation = Integer.parseInt(args[0]);
              // Check that the passed-in value for startLocation is within the range of actual locations.
              if ( startLocation >= 0 && startLocation <= MAX_LOCALES) {
                  currentLocale = startLocation;
              }
           } catch(NumberFormatException ex) {   // catch(Exception ex)
              System.out.println("Warning: invalid starting location parameter: " + args[0]);
              if (DEBUGGING) {
                 System.out.println(ex.toString());
              }
           }
        }

        // Get the game started.
        init();
        updateDisplay();

        // Game Loop
        while (stillPlaying) {
            getCommand();
            navigate();
            updateDisplay();
        }

        // We're done. Thank the player and exit.
        System.out.println("Thank you for playing.");
    }


    private static void init() {
        // Initialize any uninitialized globals.
        command = new String();

        // Set up the location instances of the Locale class.
        Locale loc0 = new Locale(0);
        loc0.setName("Abandoned Playground");
        loc0.setDesc("The swing set is barely holding together. You can go east or south from here.");

        Locale loc1 = new Locale(1);
        loc1.setName("Old Mansion");
        loc1.setDesc("There is a hole in the window. You can go north or east from here.");

        OuterDimension loc2 = new OuterDimension(2); //Locale(2);
        loc2.setName("Graveyard");
        loc2.setDesc("The tombstones are chipped on the sides. You can go east, west or south from here.");
        loc2.setDimensionLocation("Far Far Away");
        
        Locale loc3 = new Locale(3);
        loc3.setName("Sheriff's Office");
        loc3.setDesc("There seems to be no law here anymore. You can go east, west or north from here.");
        
        Locale loc4 = new Locale(4);
        loc4.setName("Haunted Bakery");
        loc4.setDesc("Stale bread is behind the glass window. You can go east, west or south from here.");
        
        OuterDimension loc5 = new OuterDimension(5); //Locale(5);
        loc5.setName("Broken Fountain");
        loc5.setDesc("There seems to be frogs living in it. You can go east, west or north from here.");
        loc5.setDimensionLocation("Stolen property of an Alien");
        
        Locale loc6 = new Locale(6);
        loc6.setName("Magick Shoppe");
        loc6.setDesc("Outside the ancient oak tree there is a sign that displays the words 'Magick For Sale'. In the shop there is a \n Magic Wand \n Old Staff \n Spellbook \n Love Potion\n You can go west or south from here.");
        
        Locale loc7 = new Locale(7);
        loc7.setName("Run-down School");
        loc7.setDesc("The front door's lock is completely rusted over. You can go north or west from here.");

        // Set up the location array.
        locations = new Locale[8];
        locations[2] = loc2; // Graveyard   
        locations[0] = loc0; // Abandoned Playground  
        locations[1] = loc1; // Old Mansion  
        locations[3] = loc3; // Sheriff's Office
        locations[4] = loc4; // Haunted Bakery
        locations[5] = loc5; // Broken Fountain
        locations[6] = loc6; // Magick Shoppe
        locations[7] = loc7; // Run-down School
        
        Items item1 = new Items(0);
        item1.setName("axe");
        item1.setDesc("Sharp and pointy, probably can cut through wood.");
        
        Items item2 = new Items(1);
        item2.setName("wrench");
        item2.setDesc("You can probably knock something unconcious with this.");
        
        Items item3 = new Items(2);
        item2.setName("hammer");
        item2.setDesc("Can make nails go through things.");
        
        Items item4 = new Items(3);
        item2.setName("crowbar");
        item2.setDesc("Makes prying open doors seem like childs play.");
        
        itemArray = new Items[4];
        itemArray[0] = item1;
        itemArray[1] = item2;
        itemArray[2] = item3;
        itemArray[3] = item4;
        
        if (DEBUGGING) {
           System.out.println("All game locations:");
           for (int i = 0; i < locations.length; ++i) {
              System.out.println(i + ":" + locations[i].toString());
           }
           System.out.println("When you open your eyes you awake in the playground. You see a piece of paper under the slide that looks like it could be a map.");
        }
        // Set up the navigation matrix.
        nav = new int[][] {
                                 /* N   S   E   W */
                                 /* 0   1   2   3 */
         /* nav[0] for loc 0 */  { -1,  1, 2, -1 },
         /* nav[1] for loc 1 */  {  0, -1, 3, -1 },
         /* nav[2] for loc 2 */  { -1,  3, 4,  0 },
         /* nav[3] for loc 3 */  {  2, -1, 5,  1 },
         /* nav[4] for loc 4 */  { -1,  5, 6,  2 },
         /* nav[5] for loc 5 */  {  4, -1, 7,  3 },
         /* nav[6] for loc 6 */  { -1,  7,-1,  4 },
         /* nav[7] for loc 7 */  {  6, -1,-1,  5 }
        };
    }

    private static void updateDisplay() {
        System.out.println(locations[currentLocale].getName());
        System.out.println(locations[currentLocale].getDesc());
    }

    private static void getCommand() {
        System.out.print("[" + moves + " moves, score " + score + ", " + ((score * 100) / MAX_SCORE) + "% completed] ");
        Scanner inputReader = new Scanner(System.in);
        command = inputReader.nextLine();  // command is global.
    }

    private static void navigate() {
        final int INVALID = -1;
        int dir = INVALID;  // This will get set to a value > 0 if a direction command was entered.
      
        if (        command.equalsIgnoreCase("north") || command.equalsIgnoreCase("n") ) {
            dir = 0;
        } else if ( command.equalsIgnoreCase("south") || command.equalsIgnoreCase("s") ) {
            dir = 1;
        } else if ( command.equalsIgnoreCase("east")  || command.equalsIgnoreCase("e") ) {
            dir = 2;
        } else if ( command.equalsIgnoreCase("west")  || command.equalsIgnoreCase("w") ) {
            dir = 3;

        } else if ( command.equalsIgnoreCase("quit")  || command.equalsIgnoreCase("q")) {
            quit();
        } else if ( command.equalsIgnoreCase("help")  || command.equalsIgnoreCase("h")) {
            help();   
        } else if ( command.equalsIgnoreCase("take axe")){
        	item1Collected = 1;
        	System.out.println("You put the axe in your inventory");
        } else if ( command.equalsIgnoreCase("take wrench")){
        	item2Collected = 1;
        	System.out.println("You put the wrench in your inventory");
        } else if ( command.equalsIgnoreCase("take hammer")){
        	item3Collected = 1;
        	System.out.println("You put the hammer in your inventory");
        } else if ( command.equalsIgnoreCase("take crowbar")){
        	item4Collected = 1;
        	System.out.println("You put the crowbar in your inventory");
        } else if ( command.equalsIgnoreCase("take map")){
        	mapAcquired = true;
        	System.out.println("You acquired the map. Type map or m.");
        }
        if (command.equalsIgnoreCase("map") || command.equalsIgnoreCase("m")){
        	if(mapAcquired == true){
        		System.out.println("Copy and paste this URL into a brower to see the map: http://imgur.com/QBY3nDK");
        	}
        	else {
        		System.out.println("You do not yet have the map");
        	}
        }
        
        if(!(command.equalsIgnoreCase("north") || command.equalsIgnoreCase("n") ||
        	 command.equalsIgnoreCase("east") || command.equalsIgnoreCase("e") || 
        	 command.equalsIgnoreCase("west") || command.equalsIgnoreCase("w") || 
        	 command.equalsIgnoreCase("south") || command.equalsIgnoreCase("s") ||
        	 command.equalsIgnoreCase("quit") || command.equalsIgnoreCase("q") ||
        	 command.equalsIgnoreCase("help") || command.equalsIgnoreCase("h") ||
        	 command.equalsIgnoreCase("take axe") ||
        	 command.equalsIgnoreCase("take wrench") ||
        	 command.equalsIgnoreCase("take hammer") ||
        	 command.equalsIgnoreCase("take crowbar") ||
        	 command.equalsIgnoreCase("map") || command.equalsIgnoreCase("m") ||
        	 command.equalsIgnoreCase("take map"))){
        		System.out.println("That is not a valid command, type help or h for valid commands.");
        }


        if (dir > -1) {   // This means a dir was set.
            int newLocation = nav[currentLocale][dir];
            if (newLocation == INVALID) {
                System.out.println("You cannot go that way. Type Help or H to see valid commands.");
            } 
            else {
                currentLocale = newLocation;
                moves = moves + 1;
                if(newLocation == 0 && mapAcquired == false){
                	System.out.println("You think you see a map under the slide.");
                }
                if(newLocation == 2 && item1Collected == 0){ 
                	System.out.println("There appears to be axe wedged in a tombstone.");
                	}
                
                if(currentLocale == 1 && item2Collected == 0){ 
                	System.out.println("There is a wrench hanging out of the broken window.");
                }
                
                if(currentLocale == 5 && item3Collected == 0){ 
                	System.out.println("A hammer appears to be plugging one of the holes in the fountain.");
                }
                
                if(currentLocale == 7 && item4Collected == 0){ 
                	System.out.println("There seems to be a crowbar wedged between the doors.");
                }
                
                if (locations[newLocation].getHasVisited() == false && newLocation != 0){
                	score = score + 5;
                	locations[newLocation].setHasVisited(true);
                }
            }
        }
       }
   

    private static void help() {
        System.out.println("The commands are as follows:");
        System.out.println("   n/north");
        System.out.println("   s/south");
        System.out.println("   q/quit");
        System.out.println("   m/map if it is unlocked");
        System.out.println("   h/help to repeat these instructions");
        System.out.println("   take *Insert item name here*");
    }

    private static void quit() {
        stillPlaying = false;
    }
}