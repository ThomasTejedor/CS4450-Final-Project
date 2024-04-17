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
    
    // method: render
    // purpose: Draws the chunk on the screen, displaying 900 cubes at once
    public void render() {
        glPushMatrix();
            glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
            glVertexPointer(3, GL_FLOAT, 0, 0L);
            glBindBuffer(GL_ARRAY_BUFFER, vboColorHandle);
            glColorPointer(3, GL_FLOAT, 0, 0L);
            glDrawArrays(GL_QUADS, 0, CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE * 24);
        glPopMatrix();
    }
    
    // method: rebuildMesh
    // purpose: Recreates the Vertex Buffer Objects so that they are ready to render
    public void rebuildMesh(float startX, float startY, float startZ) {
        vboColorHandle = glGenBuffers();
        vboVertexHandle = glGenBuffers();
        
        FloatBuffer vertexPositionData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        FloatBuffer vertexColorData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int z = 0; z < CHUNK_SIZE; z++) {
                for (int y = 0; y < CHUNK_SIZE; y++) {
                    vertexPositionData.put(createCube(
                        (float)(startX + x * CUBE_LENGTH), 
                        (float)(y * CUBE_LENGTH + (int)(CHUNK_SIZE * .8)), 
                        (float)(startZ + z * CUBE_LENGTH)));
                    vertexColorData.put(createCubeVertexCol(
                        getCubeColor(blocks[x][y][z])));
                }
            }
        }
        
        vertexColorData.flip();
        vertexPositionData.flip();
        
        glBindBuffer(GL_ARRAY_BUFFER,vboVertexHandle);
        glBufferData(GL_ARRAY_BUFFER, vertexPositionData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
     
        glBindBuffer(GL_ARRAY_BUFFER, vboColorHandle);
        glBufferData(GL_ARRAY_BUFFER, vertexColorData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
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
        switch (block.getID()) {
            case 1 -> {
                return new float[] {0, 1, 0};
            }
            case 2 -> {
                return new float[] {1, 0.5f, 0};
            }
            case 3 -> {            
                return new float[] {0, 0, 1};
            }
        }
        
        return new float[] {1, 1, 1};
    }
    
    // method: Constructor
    // purpose: Creates a new Chunk at the given coordinates
    public Chunk(int startX, int startY, int startZ) {
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
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        
        rebuildMesh(startX, startY, startZ);
    }
}
