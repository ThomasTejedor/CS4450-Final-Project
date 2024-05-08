/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalproject;

/**
 *
 * @author PixelPioneers
 */
public class Block {
    private boolean isActive;
    private BlockType type;
    private float x, y, z;
    
    public enum BlockType {
        Block_Grass(0),
        Block_Sand(1),
        Block_Water(2),
        Block_Dirt(3),
        Block_Stone(4),
        Block_Bedrock(5),
        Block_Skybox(6);
        
        private int blockID ;
        
        // method: Constructor
        // purpose: Sets the id of the BlockType
        BlockType(int id) {
            blockID = id;
        }
        
        // method: getID
        // purpose: Returns the ID of the BlockType
        public int getID() {
            return blockID;
        }
        
        // method: setID
        // purpose: Sets the ID of the BlockType
        public void setID(int id) {
            blockID = id;
        }
    }
    
    // method: Constructor
    // purpose: Creates a new Block of the specified type
    public Block(BlockType type) {
        this.type = type;
    }
    
    // method: setCoords
    // purpose: Sets the coordinates of the block to the specified value
    public void setCoords(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    // method: isActive
    // purpose: Determines if the block is active or not
    public boolean isActive() {
        return isActive;
    }
    
    // method: setActive
    // purpose: Set whether or not the block is active
    public void setActive(boolean active) {
        isActive = active;
    }
    
    // method: getID
    // purpose: Gets the ID of the block
    public int getID() {
        return type.getID();
    }
}
