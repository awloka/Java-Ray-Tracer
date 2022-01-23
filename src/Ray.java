import javax.vecmath.Vector3f;
/**
 * A ray with a starting point and a direction, which is given
 * by a normalized vector. This class also holds the algorithm for
 * sphere intersections, and can store the minimum distance an intersection 
 * was found at, as well as the object that was intersected.
 * 
 * @author Alex Wloka 
 * @version 1/23/22
 */
public class Ray
{
    // instance variables - replace the example below with your own
    public Vector3f startPos;
    public Vector3f direction;
    public Sphere sphere;
    public float minT;
    /**
     * Constructor for objects of class Ray
     * 
     * @param startingPos The starting position of the ray, usually the camera's position
     * @param rayDirection The direction the ray is being shot in
     */
    public Ray(Vector3f startingPos, Vector3f rayDirection)
    {
        startPos = startingPos;
        direction = new Vector3f();
        direction.set(rayDirection);
        direction.normalize();
        minT = -1f;
    }
    
    /**
     * Checks whether the ray intersects a given sphere, and stores the smallest distance
     * it has obtained thus far as well as the closest sphere to obtain its color
     * 
     * @param sp The given sphere
     */
    public void intersectSphere(Sphere sp)
    {
        //Intersection Math
        float r = sp.getRadius();
        Vector3f startToCent = new Vector3f();
        startToCent.scaleAdd(-1, sp.getCenter(), startPos);
        float startToCentSquared = startToCent.dot(startToCent);
        float a = 1f;
        float b = startToCent.dot(direction)*2;
        float c = startToCentSquared - r*r;
        float currDiscrim = b*b - 4*a*c;
        //Checking for a new minT
        if (currDiscrim >= 0) 
        //if there is at least 1 intersection with this sphere
        {
            float currT = -1f;
            float rootSect = (float)(Math.sqrt(currDiscrim));
            float posCurrT = (-b + rootSect)/ (2*a);
            if (posCurrT > 0) 
            //if the first distance is valid
            {
                currT = posCurrT;
            }
            if (currDiscrim > 0) 
            //if there are two intersections
            {
                float negCurrT = (-b - rootSect) / (2*a);
                if (negCurrT > 0)
                //if the second distance is valid
                {
                    if (currT == -1f || negCurrT < currT)
                    //if posCurrT wasn't valid or negCurrT is a shorter dist.
                    {
                        currT = negCurrT;
                    }
                }
            }
            if (currT != -1f)
            //if currT was set to a valid distance
            {
                if (minT == -1f || currT < minT)
                //if no value has been stored for minT or currT is a shorter dist.
                {
                    minT = currT;
                    sphere = sp;
                }
            }
        }
    }
}
