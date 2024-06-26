/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package finalproject;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;
import org.lwjgl.util.glu.GLU;

/**
 * file: FinalProject.java
 * authors: Pixel Pioneers (J. Fabel, T. Tejedor, C. Kim)
 * class: CS 4450
 * 
 * assignment: Final Project - Checkpoint 1
 * date last modified: 4/08/2024
 * 
 * purpose: Creates a 640x480 window that renders a 3D cube, and allows the user to move
 *          around using WASD keys and the mouse.
 * 
 */
public class FinalProject {
    private FPCameraController fp;
    private DisplayMode displayMode;      
    private FloatBuffer lightPosition;
    private FloatBuffer whiteLight;
    
    // method: start
    // purpose: Creates and initializes the window, and then renders the primitives inside
    public void start() {        
        try {
            createWindow();
            initGL();
            fp = new FPCameraController(-20f, -50f, -20f);
            fp.gameLoop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }        
    
    private void initLightArrays() {
        lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(0).put(20.0f).put(0).put(1.0f).flip();
        
        whiteLight = BufferUtils.createFloatBuffer(4);
        whiteLight.put(1.0f).put(1.0f).put(1.0f).put(0.0f).flip();
    }
    
    // method: createWindow
    // purpose: Creates a 640x480 window and finds the best display mode for that window
    private void createWindow() throws Exception {
        Display.setFullscreen(false);
        DisplayMode d[] = Display.getAvailableDisplayModes();
        
        for (int i = 0; i < d.length; i++) {
            if (d[i].getWidth() == 640 && d[i].getHeight() == 480 && d[i].getBitsPerPixel() == 32) {
                displayMode = d[i];
                break;
            }
        }
        
        Display.setDisplayMode(displayMode);
        Display.setTitle("CS 4450 Final Project");
        Display.create();
    }
    
    // method: initGL
    // purpose: Initializes openGL so that we are ready to render in our window
    private void initGL() {
        glClearColor(0f, 0f, 0f, 0f);
        
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        
        GLU.gluPerspective(100f, (float)displayMode.getWidth() / (float)displayMode.getHeight(), 0.1f, 300f);
        
        glEnable(GL_TEXTURE_2D);
        glEnableClientState (GL_TEXTURE_COORD_ARRAY);

        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glEnable(GL_DEPTH_TEST);
        
        initLightArrays();
        glLight(GL_LIGHT0,GL_POSITION, lightPosition);
        glLight(GL_LIGHT0,GL_SPECULAR, whiteLight);
        glLight(GL_LIGHT0,GL_DIFFUSE, whiteLight);
        glLight(GL_LIGHT0,GL_AMBIENT, whiteLight);
        
        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
//        glEnable(GL_ALPHA_TEST);
//        glAlphaFunc(GL_GREATER, 0.1f);
        
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
    }
    
    // method: main
    // purpose: Creates a new instance of FinalProject and then starts it
    public static void main(String[] args) {
        FinalProject program = new FinalProject();
        program.start();
    }
    
}
