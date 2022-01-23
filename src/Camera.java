import javax.vecmath.Vector3f;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * The point from which one views the scene
 *
 * @author Alex Wloka
 * @version @version 1/23/22
 */
public class Camera
{
    private Vector3f pos; //Camera position
    private Vector3f lookAtPoint; //The point directly in front of the camera

    //Horizontal and vertical field of view,
    //as defined by an angle in radians
    private double horizFOV;
    private double vertFOV;

    private Vector3f viewDirection; //Direction the camera is looking in

    //Defines the upper and right-most boundaries of the display
    //in respect to the view direction
    private Vector3f upVector;
    private Vector3f rightVector;
    //The direction of the Y-Axis, which the rightVector will always be
    //orthogonal to
    public static final Vector3f YAXIS = new Vector3f(0f, 1f, 0f);
    /**
     * Constructor for objects of class Camera
     *
     * @param camPos Position of the camera
     * @param centerPoint Position of a point directly in front
     *                    of the camera
     * @param horizontalFOV The horizontal field of view
     */
    public Camera(Vector3f camPos, Vector3f centerPoint, double horizontalFOV)
    {
        pos = camPos;
        lookAtPoint = centerPoint;
        horizFOV = horizontalFOV;
        vertFOV =  (11.5f/16f) *horizFOV;

        viewDirection = new Vector3f();
        viewDirection.sub(lookAtPoint, pos);
        viewDirection.normalize();

        rightVector = new Vector3f();
        rightVector.cross(viewDirection,YAXIS);
        rightVector.normalize();

        upVector = new Vector3f();
        upVector.cross(rightVector, viewDirection);
        upVector.normalize();
    }
    //Getters for all instance variables
    public Vector3f getPosition()
    {
        return pos;
    }
    public Vector3f getlookAtPoint()
    {
        return lookAtPoint;
    }
    public double getHorizFOV()
    {
        return horizFOV;
    }
    public double getVertFOV()
    {
        return vertFOV;
    }
    public Vector3f getViewDirection()
    {
        return viewDirection;
    }
    public Vector3f getUpVector()
    {
        return upVector;
    }
    public Vector3f getRightVector()
    {
        return rightVector;
    }
}
