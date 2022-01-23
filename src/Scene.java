import acm.graphics.*;
import javax.vecmath.Vector3f;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
/**
 * A class containing the components of a scene
 * (looking back on how I implemented this class, 
 * it isn't particularly useful, and probably could 
 * have simplified my code by actually defining the 
 * specific components of my scene in here or not 
 * using this class at all)
 * 
 * @author Alex Wloka 
 * @version 1/23/22
 */
public class Scene
{
    public Camera cam;
    public Object[] objs;
    public Light[] lightSources;
    /**
     * Constructor for objects of class Scene
     * 
     * @param camera The camera of a given scene
     * @param objects The objects within a scene
     */
    public Scene(Camera camera, Object[] objects, Light[] lights)
    {
        cam = camera;
        
        objs = new Object[objects.length];
        for (int i = 0; i < objs.length; i++)
        {
            objs[i] = objects[i];
        }
        
        lightSources = new Light[lights.length];
        for (int i = 0; i < lights.length; i++)
        {
            lightSources[i] = lights[i];
        }
    }

}
