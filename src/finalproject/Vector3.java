/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalproject;

/**
 *
 * @author PixelPioneers
 */
public class Vector3<T extends Number> {
    public T x, y, z;
    
    // method: constructor
    // purpose: Creates a new Vector with the given 3 values
    public Vector3(T x, T y, T z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
