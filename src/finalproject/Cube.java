/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalproject;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.Color;

/**
 *
 * @author PixelPioneers
 */
public class Cube {
    // Notes - this will be replaced during checkpoint 2
    private final Color CUBE_TOP_COLOR = (Color) Color.YELLOW;
    private final Color CUBE_BOTTOM_COLOR = (Color) Color.WHITE;
    private final Color CUBE_FRONT_COLOR = (Color) Color.GREEN;
    private final Color CUBE_BACK_COLOR = (Color) Color.BLUE;
    private final Color CUBE_LEFT_COLOR = (Color) Color.RED;
    private final Color CUBE_RIGHT_COLOR = (Color) Color.ORANGE;
    private final Color OUTLINE_COLOR;    
    private float width;
    
    // method: constructor
    // purpose: Creates a new Cube with the specified values
    public Cube(Color outlineColor, float width) {
        this.OUTLINE_COLOR = outlineColor;
        this.width = width;
    }
    
    // method: constructor
    // purpose: Create a new Cube with the specified size and default colors
    public Cube(float width) {
        this((Color)Color.WHITE, width);
    }
    
    // method: constructor
    // purpose: Create a new Cube with default values
    public Cube() {
        this(2f);
    }
    
    // method: draw
    // purpose: Draws a cube on the screen
    public void draw() {
        float unitSize = width / 2;
        
        // Draw the cube
        glBegin(GL_QUADS);
            
            // Top
            glColor3f(CUBE_TOP_COLOR.getRed(), CUBE_TOP_COLOR.getGreen(), CUBE_TOP_COLOR.getBlue());
            glVertex3f(unitSize, unitSize, -unitSize);
            glVertex3f(-unitSize, unitSize, -unitSize);
            glVertex3f(-unitSize, unitSize, unitSize);
            glVertex3f(unitSize, unitSize, unitSize);
            
            // Bottom
            glColor3f(CUBE_BOTTOM_COLOR.getRed(), CUBE_BOTTOM_COLOR.getGreen(), CUBE_BOTTOM_COLOR.getBlue());
            glVertex3f(unitSize, -unitSize, unitSize);
            glVertex3f(-unitSize, -unitSize, unitSize);
            glVertex3f(-unitSize, -unitSize, -unitSize);
            glVertex3f(unitSize, -unitSize, -unitSize);
            
            // Front
            glColor3f(CUBE_FRONT_COLOR.getRed(), CUBE_FRONT_COLOR.getGreen(), CUBE_FRONT_COLOR.getBlue());
            glVertex3f(unitSize, unitSize, unitSize);
            glVertex3f(-unitSize, unitSize, unitSize);
            glVertex3f(-unitSize, -unitSize, unitSize);
            glVertex3f(unitSize, -unitSize, unitSize);
            
            // Back
            glColor3f(CUBE_BACK_COLOR.getRed(), CUBE_BACK_COLOR.getGreen(), CUBE_BACK_COLOR.getBlue());
            glVertex3f(unitSize, -unitSize, -unitSize);
            glVertex3f(-unitSize, -unitSize, -unitSize);
            glVertex3f(-unitSize, unitSize, -unitSize);
            glVertex3f(unitSize, unitSize, -unitSize);
            
            // Left
            glColor3f(CUBE_LEFT_COLOR.getRed(), CUBE_LEFT_COLOR.getGreen(), CUBE_LEFT_COLOR.getBlue());
            glVertex3f(-unitSize, unitSize, unitSize);
            glVertex3f(-unitSize, unitSize, -unitSize);
            glVertex3f(-unitSize, -unitSize, -unitSize);
            glVertex3f(-unitSize, -unitSize, unitSize);
            
            // Right
            glColor3f(CUBE_RIGHT_COLOR.getRed(), CUBE_RIGHT_COLOR.getGreen(), CUBE_RIGHT_COLOR.getBlue());
            glVertex3f(unitSize, unitSize, -unitSize);
            glVertex3f(unitSize, unitSize, unitSize);
            glVertex3f(unitSize, -unitSize, unitSize);
            glVertex3f(unitSize, -unitSize, -unitSize);
        glEnd();
        
        // Draw the cube's outline
        glBegin(GL_LINE_LOOP);
            glColor3f(OUTLINE_COLOR.getRed(), OUTLINE_COLOR.getGreen(), OUTLINE_COLOR.getBlue());
            
            // Top
            glVertex3f(unitSize, unitSize, -unitSize);
            glVertex3f(-unitSize, unitSize, -unitSize);
            glVertex3f(-unitSize, unitSize, unitSize);
            glVertex3f(unitSize, unitSize, unitSize);
        glEnd();
        glBegin(GL_LINE_LOOP);
            // Bottom
            glVertex3f(unitSize, -unitSize, unitSize);
            glVertex3f(-unitSize, -unitSize, unitSize);
            glVertex3f(-unitSize, -unitSize, -unitSize);
            glVertex3f(unitSize, -unitSize, -unitSize);
        glEnd();
        glBegin(GL_LINE_LOOP);
            // Front
            glVertex3f(unitSize, unitSize, unitSize);
            glVertex3f(-unitSize, unitSize, unitSize);
            glVertex3f(-unitSize, -unitSize, unitSize);
            glVertex3f(unitSize, -unitSize, unitSize);
        glEnd();
        glBegin(GL_LINE_LOOP);
            // Back
            glVertex3f(unitSize, -unitSize, -unitSize);
            glVertex3f(-unitSize, -unitSize, -unitSize);
            glVertex3f(-unitSize, unitSize, -unitSize);
            glVertex3f(unitSize, unitSize, -unitSize);
        glEnd();
        glBegin(GL_LINE_LOOP);
            // Left
            glVertex3f(-unitSize, unitSize, unitSize);
            glVertex3f(-unitSize, unitSize, -unitSize);
            glVertex3f(-unitSize, -unitSize, -unitSize);
            glVertex3f(-unitSize, -unitSize, unitSize);
        glEnd();
        glBegin(GL_LINE_LOOP);
            // Right
            glVertex3f(unitSize, unitSize, -unitSize);
            glVertex3f(unitSize, unitSize, unitSize);
            glVertex3f(unitSize, -unitSize, unitSize);
            glVertex3f(unitSize, -unitSize, -unitSize);
        glEnd();
    }
}
