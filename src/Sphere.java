import javax.vecmath.Vector3f;
import java.awt.*;
import javax.swing.*;
/**
 * A sphere with a center as defined by a vector, a radius, and a color
 * (center location is defined as a vector by keeping all positional vectors
 * relative to each other using a single origin, in this case the camera position
 * 
 * @author Alex Wloka
 * @version 1/23/22
 */
public class Sphere
{
    private Vector3f pos;
    private float radius;
    private Color col;
    private float roughness;
    private float density;
    /**
     * Constructor for objects of class Sphere
     * 
     * @param position The center, defined by a vector which will stem from the camera 
     *                 position
     * @param r The radius of the sphere
     * @param sphereCol The color of the sphere
     * @param rough The value determining how rough the sphere will appear
     * @param dense The value determining how hard the sphere will appear
     *                 
     * @precondition rough and dense are initialized to a value between 0 and 1
     */
    public Sphere(Vector3f position, float r, Color sphereCol, float rough, float dense)
    {
        pos = position;
        radius = r;
        col = sphereCol;
        roughness = rough;
        density = dense;
    }
    
    public Vector3f getNormalSurfacePoint(Vector3f surfacePoint)
    {
        Vector3f surfaceNormal = new Vector3f();
        surfaceNormal.scaleAdd(-1f, pos, surfacePoint);
        surfaceNormal.normalize();
        return surfaceNormal;
    }
    public float getRadius()
    {
        return radius;
    }
    public Vector3f getCenter()
    {
        return pos;
    }
    public Color getCol()
    {
        return col;
    }
    public float getRoughness()
    {
        return roughness;
    }
    public float getDensity()
    {
        return density;
    }
}
