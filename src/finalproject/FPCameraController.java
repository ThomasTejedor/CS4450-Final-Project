/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalproject;

//import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.Sys;
import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;

/**
 *
 * @author PixelPioneers
 */
public class FPCameraController {
    private final float GRAVITY = 0.5f;

    // 3D vector to store the camera's position
    private Vector3<Float> position = null;
    private Vector3<Float> IPosition = null;
    
    // Rotation around the Y axis of the camera    
    private float yaw = 0.0f;
    // Rotation around the Y axis of the camera
    private float pitch = 0.0f;
        
    private Chunk myChunk;
    
    // method: constructor
    // purpose: Initializes a new FPCameraController so that it is ready to render on our screen
    public FPCameraController(float x, float y, float z) {
        position = new Vector3<>(x, y, z);
        IPosition = new Vector3<>(x, y, z);
        IPosition.x = 0f;
        IPosition.y = 15f;
        IPosition.z = 0f;              
        
        myChunk = new Chunk((int)x, (int)y, (int)z);
    }
    
    // method: yaw
    // purpose: Increment the camera's current yaw rotation
    public void yaw(float amount) {
        // increment the yaw by the amount param
        yaw += amount;
    }
    
    // method: pitch
    // purpose: Increment the camera's current pitch rotation
    public void pitch(float amount) {
        pitch -= amount;
    }
    
    // method: walkForwards
    // purpose: Moves the camera forwards relative to its current yaw
    public void walkForwards(float distance) {
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw));
        position.x -= xOffset;
        position.z += zOffset;
        
        
    }
    
    // method: walkBackwards
    // purpose: Moves the camera backwards relative to its current yaw
    public void walkBackwards(float distance) {
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw));
        position.x += xOffset;
        position.z -= zOffset;
    }
    
    // method: strafeLeft
    // purpose: Strafes the camera left relative to its current yaw
    public void strafeLeft(float distance) {
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw - 90));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw - 90));
        position.x -= xOffset;
        position.z += zOffset;                
    }
    
    // method: strafeRight
    // purpose: Strafes the camera right relative to its current location
    public void strafeRight(float distance) {
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw + 90));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw + 90));
        position.x -= xOffset;
        position.z += zOffset;                
    }
    
    // method: moveUp
    // purpose: Moves the camera up relative to its current yaw
    public void moveUp(float distance) {
        position.y -= distance;
    }
    
    // method: moveDown
    // purpose: Moves the camera down
    public void moveDown(float distance) {
        position.y += distance;
    }
    
    // method: lookThrough
    // purpose: Translates and rotates the matrix so that it looks through the camera
    public void lookThrough() {
     
        // rotate the pitch around the X axis
        glRotatef(pitch, 1f, 0f, 0f);
        // rotate the yaw around the Y axis
        glRotatef(yaw, 0f, 1f, 0f);
        // translate to the position vector's location
        glTranslatef(position.x, position.y, position.z);
        
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(40.0f).put(
                50.0f).put(40.0f).put(1.0f).flip();
        glLight(GL_LIGHT0,GL_POSITION,lightPosition);
    }
    
    // method: gameLoop
    // purpose: Handles the primary loop of our game, including player input and rendering
    public void gameLoop() {
        FPCameraController camera = new FPCameraController(0, 0, 0);
        float dx = 0f;
        float dy = 0f;
        float dt = 0f; // length of frame
        float lastTime = 0f; // when was the last frame
        long time = 0;
        float mouseSensitivity = 0.09f;
        float movementSpeed = .35f;
        
        dt = 1 / 60f;
        
        // hide the mouse
        Mouse.setGrabbed(true);
        
        // keep looping until the display window is closed or the ESC key is down
        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            time = Sys.getTime();
            dt = time - lastTime; // in ms
//            System.out.println(time - lastTime);
            lastTime = time;
            
            
            dx = Mouse.getDX();
            dy = Mouse.getDY();
            camera.yaw(dx * mouseSensitivity);
            camera.pitch(dy * mouseSensitivity);
            
            if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
                camera.walkForwards(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
                camera.walkBackwards(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
                camera.strafeLeft(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
                camera.strafeRight(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                camera.moveUp(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                camera.moveDown(movementSpeed);
            } 
            if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
                System.out.println("x: " + -camera.position.x + ", y: " + -camera.position.y + ", z: " + -camera.position.z);
                
                boolean collision = myChunk.checkForCollision(-camera.position.x, -camera.position.y, -camera.position.z);
                System.out.println("Collision: " + collision);
            }
                        
            glLoadIdentity();
            
            // Look through the camera before drawing anything
            camera.lookThrough();           
            
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            // Draw scene here
            render();
            
            // Update display in sync aat 60 fps
            Display.update();
            Display.sync(60);   
        }
        
        Display.destroy();
    }
    
    // method: render
    // purpose: Draws a cube in our 3D world
    private void render() {
        try {            
            myChunk.render();
        } catch (Exception e) {
            
        }
    }
}
