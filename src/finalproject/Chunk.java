/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalproject;

import java.nio.FloatBuffer;
import java.util.Random;
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
        vboColorHandle = glGenBuffers();
        vboVertexHandle = glGenBuffers();
        VBOTextureHandle = glGenBuffers();
        
        FloatBuffer vertexPositionData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        FloatBuffer vertexColorData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        FloatBuffer VertexTextureData = BufferUtils.createFloatBuffer((CHUNK_SIZE*CHUNK_SIZE*CHUNK_SIZE)*6*12);
        
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int z = 0; z < CHUNK_SIZE; z++) {
                for (int y = 0; y < CHUNK_SIZE; y++) {
                    vertexPositionData.put(createCube(
                        (float)(startX + x * CUBE_LENGTH), 
                        (float)(y * CUBE_LENGTH + (int)(CHUNK_SIZE * .8)), 
                        (float)(startZ + z * CUBE_LENGTH)));
                    vertexColorData.put(createCubeVertexCol(
                        getCubeColor(blocks[x][y][z])));
                    VertexTextureData.put(createTexCube((float)0,(float)0,blocks[(int)(x)][(int)(y)][(int)(z)]));
                }
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
        
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < CHUNK_SIZE; y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    if (r.nextFloat() > 0.7f) {
                        blocks[x][y][z] = new Block(Block.BlockType.Block_Grass);                        
                    } else if (r.nextFloat() > 0.4f) {
                        blocks[x][y][z] = new Block(Block.BlockType.Block_Dirt);
                    } else if (r.nextFloat() > 0.2f) {
                        blocks[x][y][z] = new Block(Block.BlockType.Block_Water);
                    } else {
                        blocks[x][y][z] = new Block(Block.BlockType.Block_Stone);
                    }
                }
            }
        }
        
        vboColorHandle = glGenBuffers();
        vboVertexHandle = glGenBuffers();
        VBOTextureHandle = glGenBuffers();
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        
        rebuildMesh(startX, startY, startZ);
    }
    
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
            //dirt
                break;
            //stone
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
            //bedrock
            case 4:
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
