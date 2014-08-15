import java.util.Set;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.08.10
 */

public class Room 
{
    private String description;                 //essentially the room name, not its
                                                //description
    private String roomDescription;             //Actual description
    private HashMap<String, Room> exits;        // stores exits of this room.

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description) 
    {
        this.description = description;
        exits = new HashMap<String, Room>();
    }
    
    public Room( ) 
    {
        this.description = "";
        exits = new HashMap<String, Room>();
    }
    
    public void setDescription( String description )
    {
        this.description = description;
    }
    
    /**
     * Set the flowery description of the room
     *
     * @param description The words to set for this room
     */
    public void setRoomDescription( String description )
    {
        this.roomDescription = description;
    }
    
    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }
    
    //==============================================
    /**
     * Remove an exit going to given direction
     * @param direction the door to remove as 
     *                  indicated by the thr direction to goes to
     */
    public void removeExit( String direction )
    {
        exits.remove( direction );
    }
    
    /**
    * Return hashmap of current exits
    **/
    public HashMap<String, Room> getAllExits( )
    {
        return exits;
    }
    //=============================================
    
    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        return "You see a nameplate on the wall: " + description + ".\n" + getExitString();
    }
    
    /**
     * return this room's decription
     */
    public String getRoomDescription( )
    {
        return roomDescription  + "\n\n" + getLongDescription();
    }
    
    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }
    
    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    private String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }

}

