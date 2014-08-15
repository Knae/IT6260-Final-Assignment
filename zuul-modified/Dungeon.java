
/**
 * Class dungeon will be responsibel for generating the rooms and keeping track of where the player is.
 * It will contain public functions to return the text that will contain the description of the room and anything else in it.
 * The dungeon can be envisioned as a grid of rooms. The maximum size of this grid will be hard-coded to 10*10 for a maximum of 100 rooms.
 * 
 * @author Chuang Sing 
 * @version 20/5/2014
 */
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Set;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public class Dungeon
{
    // instance variables
    private Room dungeonFloor[][] = new Room[ 10 ][ 10 ];
    
    private HashMap< String, ArrayList< String > > roomDescriptions;    //Desriptions are determined by room type
    private ArrayList< String > roomTypes;
    private ArrayList< String > bedroomDescrip;
    private ArrayList< String > armoryDescrip;
    private ArrayList< String > farmDescrip;
    private ArrayList< String > workshopDescrip;
    private ArrayList< String > mineDescrip;
    private ArrayList< String > officeDescrip;
    private ArrayList< String > messHallDescrip;
    private ArrayList< String > emptyDescrip;
   
    private int gridHeight = 5;
    private int gridWidth = 5;
   
    //========================================================================
    //Probabilities for doors appearing, randomised everytime a floor is built
    //=========================================================================
    double northP = 0.5;
    double southP = 0.5;
    double eastP =  0.5;
    double westP =  0.5;
    
    //Other Constants
    final int maxPasses = 15; //maximum passes to repeat when generating doors
    
    Random randBox = new Random( System.currentTimeMillis() );
    
    /**
     * Basic contruction will be a 5*5 rooms. Size of the grid can be set by adding the height and width in integers
     */
    public Dungeon( int height, int width)
    {   
        initialiseDungeon( height, width );
        
        generateFloor( );
    }
    
    public Dungeon()
    {
       initialiseDungeon( 5, 5);
       
       generateFloor( );
    }
    
    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y   a sample parameter for a method  
     * @return     the sum of x and y 
     */
    private void generateFloor( )
    {
        String currentRoomType = null;
        String roomName = new String( );
        
        northP = randBox.nextDouble( );
        southP = randBox.nextDouble( );
        eastP =  randBox.nextDouble( );
        westP =  randBox.nextDouble( );
        
        //generate all the rooms
        for( int i = 0; i < gridWidth; i++ )
        {
            for( int j = 0; j < gridHeight; j++)
            {
                currentRoomType = roomTypes.get( randBox.nextInt( roomTypes.size( ) ) );
                roomName = currentRoomType; // + " [" + j + "][" + i + "]";
                //roomName = "Room [" + j + "][" + i + "]";
                dungeonFloor[ i ][ j ] = new Room( roomName );
            }
        }
        
        generateRooms( );
    }
    
    /**
     * Create the rooms on this floor. Do this by intializing all the rooms, randomising the doors
     * in them. Then check for doors that are not connected to each other, removing these doors from
     * the room. Finally, generate the room descriptions for rooms that still have a door(s)
     */
    private void generateRooms( )
    {
        int passes = randBox.nextInt( maxPasses );
         //generate doors
         for( int p = 0; p < passes; p++ )
         {
            for( int i = 0; i < gridWidth; i++ )
            {
                for( int j = 0; j < gridHeight; j++)
                {
                    if( ( randBox.nextDouble( ) < northP ) && ( ( j+1 ) < gridHeight ) )
                    {
                        dungeonFloor[ i ][ j ].setExit( "North", dungeonFloor[ i ][ j+1 ] );
                    }
                    if( ( randBox.nextDouble( ) < southP ) && ( ( j-1 ) >= 0 ) )
                    {
                        dungeonFloor[ i ][ j ].setExit( "South", dungeonFloor[ i ][ j-1 ] );
                    }
                    if( ( randBox.nextDouble( ) < eastP ) && ( ( i+1 ) < gridWidth ) )
                    {
                        dungeonFloor[ i ][ j ].setExit( "East", dungeonFloor[ i+1 ][ j ] );
                    }
                    if( ( randBox.nextDouble( ) < westP ) && ( ( i-1 ) >= 0 ) )
                    {
                        dungeonFloor[ i ][ j ].setExit( "West", dungeonFloor[ i-1 ][ j ] );
                    }
                }
            }
        }
        
        /**
        *check that the doors connect to another room. If not, remove doors. Generate
        *room descriptions here.
        */
        for( int i = 0; i < gridWidth; i++ )
        {
            for( int j = 0; j < gridHeight; j++ )
            {
                if( (j+1 ) <gridHeight )
                {
                    if( !( compareRooms( dungeonFloor[ i ][ j ], dungeonFloor[ i ][ j+1 ] ) ) )
                        dungeonFloor[ i ][ j ].removeExit( "North" );                    
                }
                if( (j-1 ) >= 0)
                {
                    if( !( compareRooms( dungeonFloor[ i ][ j ], dungeonFloor[ i ][ j-1 ] ) ) )
                        dungeonFloor[ i ][ j ].removeExit( "South" );  
                }
                if( ( i+1 ) < gridWidth )
                {
                  if( !( compareRooms( dungeonFloor[ i ][ j ], dungeonFloor[ i+1 ][ j ] ) ) )
                        dungeonFloor[ i ][ j ].removeExit( "East" );  
                }
                if( ( i-1 ) >= 0 )
                {
                   if( !( compareRooms( dungeonFloor[ i ][ j ], dungeonFloor[ i-1 ][ j ] ) ) )
                        dungeonFloor[ i ][ j ].removeExit( "West" );  
                }
                
                if( dungeonFloor[ i ][ j ].getAllExits( ).size() > 0 )
                {
                    ArrayList< String > possibleDescrip = roomDescriptions.get( dungeonFloor[ i ][ j ].getShortDescription( ) );
                    dungeonFloor[ i ][ j ].setRoomDescription( possibleDescrip.get( randBox.nextInt( possibleDescrip.size( ) ) ) );
                    int x = 1;
                }   
            }
        }
    }
    
    /**
     * Compare the doors in the 2 rooms.If ANY of the doors in the next room are tied to teh current room, return
     * true
     */
    private Boolean compareRooms( Room currentRoom, Room nextRoom )
    {   
        HashMap<String, Room> nextDoors =  new HashMap<String, Room>( );
        nextDoors = nextRoom.getAllExits( );
        
        if( nextDoors.containsValue( currentRoom ) )
            return true;
        else
            return false;
    }
    
    /**
     * Find the first room with doors and set that as the starting room
     */
    public Room findEntrance( )
    {
         for( int i = 0; i < gridWidth; i++ )
        {
            for( int j = 0; j < gridHeight; j++)
            {
                if( dungeonFloor[ i ][ j ].getAllExits( ).size( ) > 0 )
                    return dungeonFloor[ i ][ j ];
            }
        }
        
        return null;
    }
    
    /**
     * Remove all the doors in every room, then recreate all the rooms
     */
    public void regenerateRooms( )
    {
        //generate all the rooms
        for( int i = 0; i < gridWidth; i++ )
        {
            for( int j = 0; j < gridHeight; j++)
            {
                dungeonFloor[ i ][ j ].getAllExits( ).clear( );
            }
        }
        generateFloor( );
    }
    
    /**
     * Initialise all teh arrays that contain room names and descriptions. Also the size of the floor
     */
    private void initialiseDungeon( int height, int width )
    {
        gridHeight = height;
        gridWidth = width;
        
        roomDescriptions = new HashMap< String, ArrayList< String > > ( );
        roomTypes = new ArrayList< String > ( );
        bedroomDescrip = new ArrayList< String > ( );
        armoryDescrip  = new ArrayList< String > ( );
        farmDescrip = new ArrayList< String > ( );
        workshopDescrip = new ArrayList< String > ( );
        mineDescrip = new ArrayList< String > ( );
        officeDescrip = new ArrayList< String > ( );
        messHallDescrip = new ArrayList< String > ( );
        emptyDescrip = new ArrayList< String > ( );
        
        defineRoomTypes( );
        defineDescriptions( );        
    }
    
    /**
     * Set the room names and connect the room descriptions to the names.
     * That is, the possible room types( the name ) and the possible descriptions for each type
     */
    private void defineRoomTypes( )
    {
        roomTypes.add( "Bedroom" );
        roomTypes.add( "Armory" );
        roomTypes.add( "Farm" );
        roomTypes.add( "Workshop" );
        roomTypes.add( "Mine" );
        roomTypes.add( "Office" );
        //roomTypes.add( "Mess Hall" );
        roomTypes.add( "Empty Room" );
        
        roomDescriptions.put( "Bedroom" , bedroomDescrip );
        roomDescriptions.put( "Armory" , armoryDescrip );
        roomDescriptions.put( "Farm" , farmDescrip );
        roomDescriptions.put( "Workshop" , workshopDescrip );
        roomDescriptions.put( "Mine" , mineDescrip );
        roomDescriptions.put( "Office" , officeDescrip );
        //roomDescriptions.put( "Mess Hall" , messHallDescrip );
        roomDescriptions.put( "Empty Room" , emptyDescrip );
    }
    
    /**
     *  Set all the possible room descriptions here. Placed here so as not to clutter the
     *  top
     */
    private void defineDescriptions( )
    {
        bedroomDescrip.add( "The room seems to have been trashed. The stone bed has been split \n" +
                            "in two, whatever cushion that was placed on it long ripped to shreds.\n" +
                            "In one corner lies the smashed pieces of what may have \n" +
                            "been a stone table, a large dent on the wall above it marking where\n" +
                            " stone met stone.") ;
        bedroomDescrip.add( "This bedroom has been remarkably well-preserved, if you ignore the dust \n" +
                            "gathering all over the floor. There is a wooden bed at the north of the \n" +
                            "room, on it a mattress of feathers of some creature. In the middle,\n" +
                            "sunlight flows  into the room through a lightwell. A stone\n" +
                            "bookcase stands tall at the east wall, beside a stone desk and\n" +
                            "chair. The books in it look like it would crumple if you touch it" );
        bedroomDescrip.add( "The room is quite bare. Only the stone bed hints on its purpose. \n" +
                            "On the bed lies the skeletal remains of short humanoid creature\n" +
                            " with wide shoulder bones. On a east wall lies a faded portrait,\n" +
                            " of whom it is no longer recognisable. Underneath it is a small\n" + 
                            " gold bowl, possibly used to hold offerings." );
       armoryDescrip.add(   "This room is filled to the ceiling with wooden boxes. Some of these\n" +
                            "boxes were left open, revealing the various metal weapons within them.\n" +
                            "Adorning the walls are more weapons of numerous shapes and sizes." );
       armoryDescrip.add(   "Along the walls hang a multitude of plagues displaying various weapons.\n" +
                            "Sadly a majority of these are now empty, whatever its contents already\n" +
                            "looted long ago."  );
       armoryDescrip.add(   "This room contains several crates, as well as a number of stone\n" +
                            "mannequins (perhaps?). Within the crates as well as adorning the\n" +
                            "mannequins are numerous sets of armors of various shapes, though\n" +
                            "all appear to be made of some sort of dark red metal." );
       farmDescrip.add(     "You see trees all around you, the bright sky overhead. Strange fruits\n" +
                            " hange from their branches. You hear a gentle whisper: \"If a Bob\n" +
                            "screams in the forest, and there is no one there to hear it...\"" );
       farmDescrip.add(     "You come into a copse of trees with a large meadow in the \n" +
                            "distance, golden with ripe wheat. As you look above you, the\n" +
                            "clouds pass by lazily." );
       farmDescrip.add(     "You hear water running in the room, and quickly spot a narrow river\n" +
                            "that runs along one of the walls. Fluorescent fungi flourish all\n " +
                            "over, iluminating several skeletons in the room. Judging by the name\n" +
                            "at the entrance, these could have been the livestock kept by the previous\n" +
                            "inhabitants. Their disappearance would have mark the slow death of hunger\n" +
                            "for these poor animals, whatever they may have been." );
       workshopDescrip.add( "Numerous metal tools of strange shapes and sizes lay on the stone table in\n" +
                            "the middle of the room. Nearby the table is an anvil and a forge, though the\n" +
                            "forge seems to have been dormant and lifeless for a long time." );
       workshopDescrip.add( "A number of wooden planks lay dumped on a corner, moss and fungi growing\n" +
                            "abundantly on it. Rusted saw blades lay scattered around the room, together\n" +
                            "with hammers and other tools. A few stone cupboards can been found in the\n" +
                            "room as well," );
       workshopDescrip.add( "Stone blocks can be found all over the room. Beside each lay a number of strange\n" +
                             "metal tools. Beside each also lays a parchment of some sort. Whatever that was\n" +
                             "on most of these have faded with time. However one which was written stuck on the\n" +
                             "wall with a dagger, was realtively large and had the words \"NO TIME\" sprawled all\n" +
                             "over it" );
       mineDescrip.add(     "In the middle is a huge borehole with two large contraptions opposite each\n" +
                            "other each clinging to the edge. A minecart hangs from one of them, full to\n" +
                            "the brim with what can only be assumed as valuable minerals. There is no way\n" +
                            "this, as the contraption seems like it could collapse from age any moment." );
       mineDescrip.add(     "In the middle are some stairs carved into the stone, leading downwards. From\n" +
                            "above, you can see it doesn't very far down, and you can make out the minetracks\n" +
                            "below. However the tunnel it leads to is has collapsed. Around the room ,above\n" +
                            "and below,are a number of pickaxe-like tools scattered aound." );
       mineDescrip.add(     "In one of the far corners of the room, you can make out the entrance to a large\n" +
                            "large tunnel. However a set of stone doors blocked the way, blocked by metal\n" +
                            "bars bolted into it. Deep claw marks trail all along the walls and floor, even\n" +
                            "a few black scorch marks. If you step closer, you recognise the smell of sulphur in\n" +
                            "air. A sense of dread grows as you get closer to the door.");
       officeDescrip.add(   "The room is completely bare, cept for a stone desk and some parchment and books on\n" +
                            "top of it. A bookcase sits in a corner, empty." );
       officeDescrip.add(   "This small room contains a stone table and a wooden chair. Parchments and scrolls lay\n" +
                            "scattered all over the table." );
       officeDescrip.add(   "The room is in ruins. The table is split in half, the chair in pieces. Books and\n" +
                            "parchments lay scattered all over the table." ); 
       //Ran out of ideas for this room
       /*
        messHallDescrip.add( "Long stone tables fill the room, with wooden benches on both sides. A\n" +
                            "number of empty stone bowls lay scattered on this tables, its contents\n" +
                            "long evaporated" );
       messHallDescrip.add( "messhall2" );
       messHallDescrip.add( "messhall3" );
       */
       emptyDescrip.add(    "The walls are grey with the weight of ages and layers of dust. A\n" +
                            "lone chair sits forlornly at the corner, cobwebs dangling from\n" +
                            "its rotted limbs." );
       emptyDescrip.add(    "You feel life in the pulsating beat of the walls, which move\n" +
                            "rhythmically to the sound it gives off. You know you are alone here,\n" +
                            "and yet you feel prying eyes observing your every move..." );
       emptyDescrip.add(    "Splinters and scraps lie in a scattered pile underneath where the\n" +
                            "ceiling beam had given way. A faint light creeps in from the hole\n" +
                            "in the ceiling, illuminating the vermin who stare upon you, judging\n" +
                            "your every move." );
       emptyDescrip.add(    "The hall stretches on as far as your eyes can see in the darkness.\n" +
                            "The shadows seem to dance about ahead of you, mimicking the movements\n" +
                            "of long gone souls." );
       emptyDescrip.add(    "You squeeze through the collapsed doorway, coughing as you clear the\n" +
                            "dust from your eyes. The rest of the room is in equal disrepair, the\n" +
                            "walls rotten and scarred and the floor panels peeling and cracked." );
       emptyDescrip.add(    "You squeeze through the collapsed doorway, coughing as you clear\n" +
                            "the dust from your eyes. The rest of the room is in equal disrepair,\n" +
                            "the walls rotten and scarred and the floor panels peeling and cracked." );
    }
    /**
     * Detect the room type by extracting the first word from the room description (its name)
     */
    private String getRoomType( String inputLine )
    {
        Scanner nameToken = new Scanner( inputLine );
        
        String word = null;
        
        if( nameToken.hasNext() )       //We're only concerned with the first word
        {
            word = nameToken.next( );
        }
        
        return word;
    }
}
