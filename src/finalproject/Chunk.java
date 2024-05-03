/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalproject;

import java.nio.FloatBuffer;
import java.util.Random;
import java.util.Date;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
/**
 *
 * @author PixelPioneers
 */
public class Chunk {
    static final int CHUNK_SIZE = 30;
    static final int CUBE_LENGTH = 2;
    private Block[][][] blocks;
    private int vboVertexHandle;
    private int vboColorHandle;
    private int startX, startY, startZ;
    private Random r;
    private int VBOTextureHandle;
    private Texture texture;
    
    // method: render
    // purpose: Draws the chunk on the screen, displaying 900 cubes at once
    public void render() {
        glPushMatrix();
            glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
            glVertexPointer(3, GL_FLOAT, 0, 0L);
            glBindBuffer(GL_ARRAY_BUFFER, vboColorHandle);
            glColorPointer(3, GL_FLOAT, 0, 0L);
            glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
            glBindTexture(GL_TEXTURE_2D, 1);
            glTexCoordPointer(2,GL_FLOAT,0,0L);
            glDrawArrays(GL_QUADS, 0, CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE * 24);
        glPopMatrix();
    }
    
    // method: rebuildMesh
    // purpose: Recreates the Vertex Buffer Objects so that they are ready to render
    public void rebuildMesh(float startX, float startY, float startZ) {
        
        //Determines the random seed
        int i =(int)(new Date().getTime()/100000);
        Random r = new Random();
        i += r.nextInt();
        
        //Creates the noise to map terrain
        int minHeight = CHUNK_SIZE;
        SimplexNoise noise = new SimplexNoise(minHeight,.1,i);
        
        vboColorHandle = glGenBuffers();
        vboVertexHandle = glGenBuffers();
        VBOTextureHandle = glGenBuffers();
        
        FloatBuffer vertexPositionData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        FloatBuffer vertexColorData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        FloatBuffer VertexTextureData = BufferUtils.createFloatBuffer((CHUNK_SIZE*CHUNK_SIZE*CHUNK_SIZE)*6*12);
        
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int z = 0; z < CHUNK_SIZE; z++) {
                int height = ((int)20 + (int)(15*noise.getNoise((int)x,(int)startY,(int)z)*CUBE_LENGTH));
                for (int y = 0; y < height && y < CHUNK_SIZE; y++) {
                    blocks[x][y][z] = getBlockType(x, y, z, height);
                    
                    vertexPositionData.put(createCube(
                        (float)(startX + x * CUBE_LENGTH), 
                        (float)(startY + y * CUBE_LENGTH/* + (int)(CHUNK_SIZE * .8)*/), 
                        (float)(startZ + z * CUBE_LENGTH)));
                    vertexColorData.put(createCubeVertexCol(
                        getCubeColor(blocks[x][y][z])));
                    VertexTextureData.put(createTexCube(
                            (float)0,
                            (float)0,
                            blocks[(int)(x)][(int)(y)][(int)(z)]));
                }
                // blocks above the height should be null
            }
        }
        
        vertexColorData.flip();
        vertexPositionData.flip();
        VertexTextureData.flip();
        
        glBindBuffer(GL_ARRAY_BUFFER,vboVertexHandle);
        glBufferData(GL_ARRAY_BUFFER, vertexPositionData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
     
        glBindBuffer(GL_ARRAY_BUFFER, vboColorHandle);
        glBufferData(GL_ARRAY_BUFFER, vertexColorData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        
        glBindBuffer(GL_ARRAY_BUFFER,VBOTextureHandle);
        glBufferData(GL_ARRAY_BUFFER,VertexTextureData,
                GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER,0);
        
        
    }
    
    // method: createCubeVertexCol
    // purpose: Creates an array of cube colors to use
    private float[] createCubeVertexCol(float[] cubeColorArray) {
        float cubeColors[] = new float[cubeColorArray.length * 4 * 6];
        
        for (int i = 0; i < cubeColors.length; i++) {
            cubeColors[i] = cubeColorArray[i % cubeColorArray.length];
        }
        
        return cubeColors;
    }
    
    // method: createCube
    // purpose: Creates a new cube as an array of floats, ready to be added to a VBO
    public static float[] createCube(float x, float y, float z) {
        int offset = CUBE_LENGTH / 2;
        return new float[] {
            // TOP QUAD
            x + offset, y + offset, z,
            x - offset, y + offset, z,
            x - offset, y + offset, z - CUBE_LENGTH,
            x + offset, y + offset, z - CUBE_LENGTH,
            // BOTTOM QUAD
            x + offset, y - offset, z - CUBE_LENGTH,
            x - offset, y - offset, z - CUBE_LENGTH,
            x - offset, y - offset, z,
            x + offset, y - offset, z,
            // FRONT QUAD
            x + offset, y + offset, z - CUBE_LENGTH,
            x - offset, y + offset, z- CUBE_LENGTH,
            x - offset, y - offset, z - CUBE_LENGTH,
            x + offset, y - offset, z - CUBE_LENGTH,
            // BACK QUAD
            x + offset, y - offset, z,
            x - offset, y - offset, z,
            x - offset, y + offset, z,
            x + offset, y + offset, z,
            // LEFT QUAD
            x - offset, y + offset, z - CUBE_LENGTH,
            x - offset, y + offset, z,
            x - offset, y - offset, z,
            x - offset, y - offset, z - CUBE_LENGTH,
            // RIGHT QUAD
            x + offset, y + offset, z,
            x + offset, y + offset, z - CUBE_LENGTH,
            x + offset, y - offset, z - CUBE_LENGTH,
            x + offset, y - offset, z
        };
    }
    
    // method: getCubeColor
    // purpose: Gets the color of a Block based on its type
    private float[] getCubeColor(Block block) {
        
        return new float[] {1, 1, 1};
    }
    
    public boolean checkForCollision(float playerX, float playerY, float playerZ) {
        // Get the player's bounding box, assuming it is the same size as the cubes
        float playerMinX = playerX - 1;
        float playerMaxX = playerX + 1;
        float playerMinY = playerY - 1;
        float playerMaxY = playerY + 1;
        float playerMinZ = playerZ - 1;
        float playerMaxZ = playerZ + 1;               
        
        // See if there is a block at the current position
        int blockX = (int)((playerX - startX) / CUBE_LENGTH);
        int blockY = (int)((playerY - startY) / CUBE_LENGTH);
        int blockZ = (int)((playerZ - startZ) / CUBE_LENGTH);
        
        System.out.println("Player is at block: (" + blockX + ", " + blockY + ", " + blockZ + ")");
        
        if (blockX >= 0 && blockY >= 0 && blockZ >= 0) {
            return (blocks[blockX][blockY][blockZ] != null);           
        } else {
            return false;
        }
        /*
        // Check for collision with each block in the chunk
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < CHUNK_SIZE; y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    // If the block is not null, check for collision
                    if (blocks[x][y][z] != null) {
                        float blockMinX = startX + x * CUBE_LENGTH;
                        float blockMaxX = startX + (x + 1) * CUBE_LENGTH;
                        float blockMinY = y * CUBE_LENGTH;
                        float blockMaxY = (y + 1) * CUBE_LENGTH;
                        float blockMinZ = startZ + z * CUBE_LENGTH;
                        float blockMaxZ = startZ + (z + 1) * CUBE_LENGTH;

                        // Check if the player's bounding box intersects with the block's bounding box
                        if (playerMaxX > blockMinX && playerMinX < blockMaxX &&
                            playerMaxY > blockMinY && playerMinY < blockMaxY &&
                            playerMaxZ > blockMinZ && playerMinZ < blockMaxZ) {
                            
                            System.out.println("Collision!");
                            // Collision detected
                            return true;
                        }
                    }
                }
            }
        } */
        
        // No collision
//        return false;
    }
    
    // method: Constructor
    // purpose: Creates a new Chunk at the given coordinates
    public Chunk(int startX, int startY, int startZ) {
       
        try{texture = TextureLoader.getTexture("PNG", 
                ResourceLoader.getResourceAsStream("terrain.png"));
        } catch(Exception e) {
            System.out.println("Texture file not found");
        }
        r = new Random();
        blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
        
        vboColorHandle = glGenBuffers();
        vboVertexHandle = glGenBuffers();
        VBOTextureHandle = glGenBuffers();
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        
        rebuildMesh(startX, startY, startZ);
    }
    
    private Block getBlockType(int x, int y, int z, int height) {
        // Only place grass, sand, or water at the topmost level
        // Only place dirt or stone below
        // Only place bedrock at the very bottom        
        if (y == 0) { // Block is on the bottom-most level
            return new Block(Block.BlockType.Block_Bedrock);
        } else if (y == height - 1) { // block is on the top-most level
            float rand = r.nextFloat();
            if (rand > 2f / 3) {
                return new Block(Block.BlockType.Block_Grass);
            } else if (rand > 1f / 3) {
                return new Block(Block.BlockType.Block_Sand);
            } else {
                return new Block(Block.BlockType.Block_Water);
            }
        } else { // Block is between top-most and bottom-most levels
            float rand = r.nextFloat();
            if (rand > 0.5f) {
                return new Block(Block.BlockType.Block_Dirt);
            } else {
                return new Block(Block.BlockType.Block_Stone);
            }
        }
    }
    
    // method: createTexCube
    // purpose: Textures the cube based on its block type
    public static float[] createTexCube(float x, float y, Block block) {
        
        float offset = (1024f/16)/1024f;
        
        Vector3 topQuadCoord = new Vector3(0,0,0);
        Vector3 bottomQuadCoord = new Vector3(0,0,0);
        Vector3 rightQuadCoord = new Vector3(0,0,0);
        Vector3 leftQuadCoord = new Vector3(0,0,0);
        Vector3 frontQuadCoord = new Vector3(0,0,0);
        Vector3 backQuadCoord = new Vector3(0,0,0);
        
        //For each quad give bottom right pixel coord in texture grid
        switch(block.getID()) {
            
            //grass block
            case 0:
                topQuadCoord.x = 3;
                topQuadCoord.y = 10;
                bottomQuadCoord.x = 3; 
                bottomQuadCoord.y = 1;
                rightQuadCoord.x = 4;
                rightQuadCoord.y = 1;
                leftQuadCoord.x = 4;
                leftQuadCoord.y = 1;
                frontQuadCoord.x = 4;
                frontQuadCoord.y = 1;
                backQuadCoord.x = 4;
                backQuadCoord.y = 1;
                break;
            //sand
            case 1:
                topQuadCoord.x = 3;
                topQuadCoord.y = 2;
                bottomQuadCoord.x = 3; 
                bottomQuadCoord.y = 2;
                rightQuadCoord.x = 3;
                rightQuadCoord.y = 2;
                leftQuadCoord.x = 3;
                leftQuadCoord.y = 2;
                frontQuadCoord.x = 3;
                frontQuadCoord.y = 2;
                backQuadCoord.x = 3;
                backQuadCoord.y = 2;
                break;
            //water
            case 2:
                topQuadCoord.x = 14;
                topQuadCoord.y = 13;
                bottomQuadCoord.x = 14; 
                bottomQuadCoord.y = 13;
                rightQuadCoord.x = 14;
                rightQuadCoord.y = 13;
                leftQuadCoord.x = 14;
                leftQuadCoord.y = 13;
                frontQuadCoord.x = 14;
                frontQuadCoord.y = 13;
                backQuadCoord.x = 14;
                backQuadCoord.y = 13;
                break;
            //dirt
            case 3:
                topQuadCoord.x = 2;
                topQuadCoord.y = 1;
                bottomQuadCoord.x = 2; 
                bottomQuadCoord.y = 1;
                rightQuadCoord.x = 2;
                rightQuadCoord.y = 1;
                leftQuadCoord.x = 2;
                leftQuadCoord.y = 1;
                frontQuadCoord.x = 2;
                frontQuadCoord.y = 1;
                backQuadCoord.x = 2;
                backQuadCoord.y = 1;
                break;
            //stone
            case 4:
                topQuadCoord.x = 3;
                topQuadCoord.y = 1;
                bottomQuadCoord.x = 3; 
                bottomQuadCoord.y = 1;
                rightQuadCoord.x = 3;
                rightQuadCoord.y = 1;
                leftQuadCoord.x = 3;
                leftQuadCoord.y = 1;
                frontQuadCoord.x = 3;
                frontQuadCoord.y = 1;
                backQuadCoord.x = 3;
                backQuadCoord.y = 1;
                break;
            //bedrock
            case 5:
                topQuadCoord.x = 2;
                topQuadCoord.y = 2;
                bottomQuadCoord.x = 2; 
                bottomQuadCoord.y = 2;
                rightQuadCoord.x = 2;
                rightQuadCoord.y = 2;
                leftQuadCoord.x = 2;
                leftQuadCoord.y = 2;
                frontQuadCoord.x = 2;
                frontQuadCoord.y = 2;
                backQuadCoord.x = 2;
                backQuadCoord.y = 2;
                break;
            default:
            {
                topQuadCoord.x = 7;
                topQuadCoord.y = 14;
                bottomQuadCoord.x = 7; 
                bottomQuadCoord.y = 14;
                rightQuadCoord.x = 7;
                rightQuadCoord.y = 14;
                leftQuadCoord.x = 7;
                leftQuadCoord.y = 14;
                frontQuadCoord.x = 7;
                frontQuadCoord.y = 14;
                backQuadCoord.x = 7;
                backQuadCoord.y = 14;
                                          
            }
        }  
        return new float[] {
                // TOP QUAD(DOWN=+Y)
                x + offset*(int)topQuadCoord.x, y + offset*(int)topQuadCoord.y, //bottom right coord
                x + offset*((int)topQuadCoord.x-1), y + offset*(int)topQuadCoord.y, //bottom left coord
                x + offset*((int)topQuadCoord.x-1), y + offset*((int)topQuadCoord.y-1), //top left coord
                x + offset*(int)topQuadCoord.x, y + offset*((int)topQuadCoord.y-1), //top right coord
                //BOTTOM QUAD
                x + offset*(int)bottomQuadCoord.x, y + offset*(int)bottomQuadCoord.y, //bottom right coord
                x + offset*((int)bottomQuadCoord.x-1), y + offset*(int)bottomQuadCoord.y, //bottom left coord
                x + offset*((int)bottomQuadCoord.x-1), y + offset*((int)bottomQuadCoord.y-1), //top left coord
                x + offset*(int)bottomQuadCoord.x, y + offset*((int)bottomQuadCoord.y-1), //top right coord
                //FRONT QUAD
                x + offset*((int)frontQuadCoord.x-1), y + offset*((int)frontQuadCoord.y-1), //top left coord
                x + offset*(int)frontQuadCoord.x, y + offset*((int)frontQuadCoord.y-1), //top right coord
                x + offset*(int)frontQuadCoord.x, y + offset*(int)frontQuadCoord.y, //bottom right coord
                x + offset*((int)frontQuadCoord.x-1), y + offset*(int)frontQuadCoord.y, //bottom left coord
                //BACK QUAD(DOWN=+Y)
                x + offset*(int)backQuadCoord.x, y + offset*(int)backQuadCoord.y, //bottom right coord
                x + offset*((int)backQuadCoord.x-1), y + offset*(int)backQuadCoord.y, //bottom left coord
                x + offset*((int)backQuadCoord.x-1), y + offset*((int)backQuadCoord.y-1), //top left coord
                x + offset*(int)backQuadCoord.x, y + offset*((int)backQuadCoord.y-1), //top right coord
                //LEFT QUAD
                x + offset*((int)frontQuadCoord.x-1), y + offset*((int)frontQuadCoord.y-1), //top left coord
                x + offset*(int)frontQuadCoord.x, y + offset*((int)frontQuadCoord.y-1), //top right coord
                x + offset*(int)frontQuadCoord.x, y + offset*(int)frontQuadCoord.y, //bottom right coord
                x + offset*((int)frontQuadCoord.x-1), y + offset*(int)frontQuadCoord.y, //bottom left coord
                //RIGHT QUAD
                x + offset*((int)frontQuadCoord.x-1), y + offset*((int)frontQuadCoord.y-1), //top left coord
                x + offset*(int)frontQuadCoord.x, y + offset*((int)frontQuadCoord.y-1), //top right coord
                x + offset*(int)frontQuadCoord.x, y + offset*(int)frontQuadCoord.y, //bottom right coord
                x + offset*((int)frontQuadCoord.x-1), y + offset*(int)frontQuadCoord.y, //bottom left coord
        };       
    }
       
}
