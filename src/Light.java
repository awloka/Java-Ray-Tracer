import javax.vecmath.Vector3f;
import java.awt.*;
/**
 * The point a light exists at
 * 
 * @author Alex Wloka 
 * @version 1/23/22
 */
public class Light
{
    private Vector3f lightPos;
    private Vector3f direction;
    private Color color;
    /**
     * Constructor for objects of class Light
     */
    public Light (Vector3f lightSource)
    {
        lightPos = lightSource;
        direction = new Vector3f();
        color = Color.WHITE;
    }
    /**
     * Constructor for objects of class Light
     */
    public Light (Vector3f lightSource, 
                  Vector3f surfacePoint, 
                  Color col)
    {
        lightPos = lightSource;
        setDirection(surfacePoint);
    }
    
    public Color getColor()
    {
        return color;
    }
    
    public Vector3f getLightPos()
    {
        return lightPos;
    }
    
    public Vector3f getDirection()
    {
        return direction;
    }
    public void setColor(Color newCol)
    {
        color = newCol;
    }
    /**
     * Sets the direction as the direction from a surface
     * point to a light position
     */
    public void setDirection(Vector3f surfacePoint)
    {
        Vector3f dir = new Vector3f();
        dir.scaleAdd(-1f, surfacePoint, this.lightPos);
        dir.normalize();
        direction = dir;
    }
}
