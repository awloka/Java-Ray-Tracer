
import acm.graphics.*;
import javax.vecmath.Vector3f;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * Creates a display and the image within
 * 
 * @author Alex Wloka
 * @version 1/23/22
 */
public class RGBDisplay 
{
    private Scene scene;
    public static final int resHeight = 720;
    public static final int resWidth = 1280;
    /**
     * Constructor for objects of class RGBDisplay
     */
    public RGBDisplay()
    {
        //Constructs a camera
        Vector3f camPos = new Vector3f(0f,2f,20f);
        Vector3f lookAtPoint = new Vector3f(0f,2f,0f);
        double horizontalFOV = Math.toRadians(120);
        Camera cam = new Camera(camPos, lookAtPoint, horizontalFOV);

        //Constructs an array of spheres
        Sphere[] spheres = new Sphere[5];
        spheres[0] = new Sphere(new Vector3f(0f,-10000f,0f), 10000f, new Color(155,0,255), 
            1f, 0f);
        spheres[1] = new Sphere(new Vector3f(-12f,2f,2f), 2f, Color.RED, 0.5f, 0.1f);
        spheres[2] = new Sphere(new Vector3f(0f, 2f,3f), 2f, Color.GREEN, 0.8f, 0.1f);
        spheres[3] = new Sphere(new Vector3f(12f,2f,2f), 2f, Color.BLUE, 0.9f, 0.1f);
        spheres[4] = new Sphere(new Vector3f(0f,3f,-10f), 9f, Color.ORANGE, 0.1f, 0.1f);

        //Constructs an array of light sources
        Light[] lightSources = new Light[2];

        lightSources[0] = new Light(new Vector3f (10,30,-10));
        lightSources[0].setColor(new Color(255, 127, 0));

        lightSources[1] = new Light(new Vector3f (-10,30,-10));
        lightSources[1].setColor(new Color(0, 127, 255));
        
        //Constructs the scene
        scene = new Scene(cam, spheres, lightSources);

        //Creates the frame
        GCanvas gc = new GCanvas();
        JFrame frame = new JFrame();
        frame.getContentPane().add(BorderLayout.CENTER, gc);
        frame.setSize(1408, 792);
        setGrid(gc);
        frame.show(); 

    }

    /**
     * Draws an image
     * 
     * @param canvas The object containing the full image
     */
    public void setGrid(GCanvas canvas)
    {
        Camera cam = scene.cam;
        //creates a grid
        int[][] grid = new int[resHeight][resWidth];

        //Determines the width and height of the visible scene using
        //the field of view angles with trigonometry
        float sceneWidth = (float)(Math.tan(cam.getHorizFOV()/2))*2;
        float sceneHeight = (float)(Math.tan(cam.getVertFOV()/2))*2;

        //Creates a vector from the camera position to the top left corner
        //of the scene being displayed
        Vector3f topLeft = new Vector3f();

        topLeft.scaleAdd(-sceneWidth/2, cam.getRightVector(), cam.getViewDirection());
        topLeft.scaleAdd(sceneHeight/2, cam.getUpVector(), topLeft);

        //Draws the scene by shooting rays from the camera position in the 
        //direction of every pixel on screen. 

        //If no objects were intersected, the pixel is black.
        for (int rows = 0; rows < resHeight; rows ++)
        {
            for (int cols = 0; cols < resWidth; cols++)
            {
                //Gets the direction from the camera's location 
                //to a specific pixel using the top left corner as a 
                //reference
                Vector3f direction = new Vector3f();

                direction.scaleAdd((cols*sceneWidth)/(resWidth-1), 
                    cam.getRightVector(),
                    topLeft);
                direction.scaleAdd((-rows*sceneHeight)/(resHeight-1),
                    cam.getUpVector(),
                    direction);

                //Creates a ray starting from the camera with the 
                //direction defined above
                Ray ray = new Ray(cam.getPosition(), direction);

                //By checking for intersections between the ray and each
                //object (currently only spheres) in the scene, the closest
                //sphere to the camera in that direction will be stored, along
                //with the distance from the camera to the intersection.
                //Using this information, the color of the pixel can be
                //determined
                for(int i = 0; i < scene.objs.length; i++)
                {
                    if (scene.objs[i] instanceof Sphere)
                    {
                        Sphere sp = (Sphere)scene.objs[i];
                        ray.intersectSphere(sp);
                    }
                }
                //where lighting gets applied

                //Calculates and sets surfacePoint
                Vector3f surfacePoint = new Vector3f();
                surfacePoint.scaleAdd(ray.minT, ray.direction, ray.startPos);

                for (int i = 0; i < scene.lightSources.length; i++)
                {
                    scene.lightSources[i].setDirection(surfacePoint);
                }

                if (ray.minT == -1f) 
                //if no objects were intersected, the pixel is black
                {
                    grid[rows][cols] = GImage.createRGBPixel(0,0,0,255);
                }
                else 
                {
                    //Initializes the variable that will store my overall color of the 
                    //pixel
                    Vector3f color = new Vector3f();

                    //Loops over all light sources to determine the color
                    //of the pixel
                    for (int i = 0; i < scene.lightSources.length; i++)
                    {
                        //The part that determines Shadows
                        Vector3f direction2 = new Vector3f();
                        
                        //A vector in the direction from a surface point to a light source 
                        //Not normalized here because it will get normalized in the Ray method 
                        //anyways, and I need to have the magnitude stored for a calculation done
                        //in the if statement that encompasses light calculation
                        direction2.set(surfacePoint);
                        direction2.add(scene.lightSources[i].getLightPos());

                        //A ray starting from the surface point extending in the direction
                        //towards the lightSource
                        Ray surfaceToLight = new Ray(surfacePoint, direction2);
                        
                        //Loops through all objects in the scene to get the closest intersection
                        //(if any at all) between surfaceToLight and the closest object
                        for (int objIndex = 0; objIndex < scene.objs.length; objIndex++)
                        {
                            Sphere sp = (Sphere)scene.objs[objIndex];
                            if (!ray.sphere.equals(sp))
                            {
                                surfaceToLight.intersectSphere(sp);
                            }
                        }
                        //the if statement is what implements shadows; if either of these conditions are met
                        //there are no shadows and lighting is calculated
                        //Otherwise, a shadow exists so light does not get calculated
                        //(second condition is there in the case that a light exists between two objects, because
                        //if that is the case, minT isn't -1 because it intersected the other object, and if the 
                        //second condition isn't there, lighting wouldn't get applied for that reason)
                        if (surfaceToLight.minT < 0 || surfaceToLight.minT > direction2.length())
                        {
                            //Calculate diffuse
                            Vector3f objCol = new Vector3f();
                            objCol.set(convertColorToVector3f(ray.sphere.getCol()));

                            Vector3f lightCol = new Vector3f();
                            lightCol.set(convertColorToVector3f(
                                    scene.lightSources[i].getColor()));

                            Vector3f surfaceNormal = new Vector3f();
                            surfaceNormal.set(ray.sphere.getNormalSurfacePoint(
                                    surfacePoint));

                            Vector3f diffuse = new Vector3f();
                            diffuse.set(calculateDiffuse(objCol, 
                                    lightCol,
                                    scene.lightSources[i].getDirection(),
                                    surfaceNormal
                                ));
                            //Calculate specular
                            Vector3f specular = new Vector3f();
                            specular.set(calculateSpecular(surfacePoint,
                                    cam.getPosition(),
                                    scene.lightSources[i], 
                                    ray.sphere));

                            //Add combined lighting to the overall color
                            color.add(calculateCombinedLighting(ray.sphere, 
                                    diffuse,                
                                    specular));
                        }
                    }
                    //Convert the Vector3f color to a Color
                    Color col = convertVector3fToColor(color);

                    //Change the Color into hash code to store in the array that 
                    //represents my display
                    int r = col.getRed();
                    int g = col.getGreen();
                    int b = col.getBlue();
                    int a = col.getAlpha();
                    int pixelCol = GImage.createRGBPixel(r, g, b, a);
                    grid[rows][cols] = pixelCol;
                }
            }
        }

        //Adds the image stored in my 2d int array to the canvas
        GImage image = new GImage(grid);
        image.sendToFront();
        canvas.add(image);
    }

    /**
     * Converts a Color object to a Vector3f object
     * 
     * @param col The Color object being converted
     */
    public Vector3f convertColorToVector3f(Color col)
    {
        float newRed = (float)col.getRed() / 255f;
        float newGreen = (float)col.getGreen() / 255f;
        float newBlue = (float)col.getBlue() / 255f;
        Vector3f colVector = new Vector3f(newRed, newGreen, newBlue);
        return colVector;
    }

    /**
     * Converts a Vector3f object to a Color object
     * 
     * @param vect The Color object being converted
     * @precondition vect is a vector that represents color whose 
     *               components are within the range 0-1
     */
    public Color convertVector3fToColor(Vector3f vect)
    {
        float newRed = Math.min(vect.x, 1f);
        float newGreen = Math.min(vect.y, 1f);
        float newBlue = Math.min(vect.z, 1f);
        Color col = new Color(newRed, newGreen, newBlue);
        return col;
    }

    /**
     * Calculates the diffuse component of the lighting for a pixel (Lambertian Diffuse)
     * 
     * @param objColor      The color of the object stored by the ray that was shot from the camera
     * @param lightColor    The color of the light
     * @param dirToLight    The direction of the surface point
     * @param surfaceNormal A normalized surfacePoint on the object diffuse is being calculated 
     *                      for
     */
    public Vector3f calculateDiffuse(Vector3f objColor,
    Vector3f lightColor,
    Vector3f dirToLight,
    Vector3f surfaceNormal)
    {
        float newRed = objColor.getX() * lightColor.getX();
        float newGreen = objColor.getY() * lightColor.getY();
        float newBlue = objColor.getZ() * lightColor.getZ();
        Vector3f colorVector = new Vector3f(newRed, newGreen, newBlue);
        float dotProd = dirToLight.dot(surfaceNormal);
        float maxVal = Math.max(0, dotProd);
        colorVector.scale(maxVal);
        return colorVector;
    }

    /**
     * Calculates the specular component of the lighting (highlights) for a pixel
     * using an exponential algorithm (Blinn-Phong)
     * 
     * @param surfacePoint   The surface point of the object that lighting is being 
     *                       calculated for
     * @param cameraPosition The position of the camera
     * @param lightSource    The current light source in the for-loop that calculates lighting
     *                       in the big method above
     * @param sp             The sphere closest to the camera that intersected with the ray shot 
     *                       from the camera
     */
    public Vector3f calculateSpecular(Vector3f surfacePoint,
    Vector3f cameraPosition,
    Light lightSource, 
    Sphere sp
    )
    {
        //Gets a vector from the surface point to the camera
        Vector3f toCamera = new Vector3f();
        toCamera.set(surfacePoint);
        toCamera.scaleAdd(-1f, cameraPosition);
        toCamera.normalize();

        //Calculates a half-way point between the lightSource position
        //and camera position
        Vector3f half = new Vector3f();
        half.add(lightSource.getDirection(), toCamera);
        half.normalize();

        //The exponential algorithm (part that generates a scalar based on roughness)
        //This scalar's value has a direct impact on the intensity of the highlight
        float maxExponent = 15.0f;
        float specularPower = (float)(Math.pow(2.0, 
                    (double)(maxExponent - sp.getRoughness() * maxExponent)));
        float t = (specularPower - 1) / specularPower;

        //Gets the intensity of the specular component
        float dotVal = sp.getNormalSurfacePoint(surfacePoint).dot(half);
        float maxVal = Math.max(0, dotVal);
        float intensity = (float)(Math.pow(maxVal, specularPower));
        intensity *= Math.max(0f, 
            sp.getNormalSurfacePoint(surfacePoint).dot(lightSource.getDirection()));
        intensity *= t;
        Vector3f specular = new Vector3f();
        specular.set(convertColorToVector3f(lightSource.getColor()));
        specular.scale(intensity);

        return specular;
    }

    /**
     * Calculates the combined lighting (incorperates the sphere's density,
     * as well as the diffuse and specular calculations done for a surface point
     * of the sphere
     */
    public Vector3f calculateCombinedLighting(Sphere sp,
    Vector3f diffuse,
    Vector3f specular)
    {
        float energyConservation = 1 - sp.getDensity();
        Vector3f totalLighting = new Vector3f();
        diffuse.scaleAdd(energyConservation, specular);
        totalLighting.set(diffuse);
        return totalLighting;
    }
}
