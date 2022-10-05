import java.awt.*;
import java.util.ArrayList;

public class Shape
{
    private final double[][] coordinates; //points stored in clockwise order

    //edge point of shape from the perspective of each light source, stored as a theta value from the light source, clockwise order
    //FOR OPTIMISATION
    //private ArrayList<Double> firstEdges;
    //private ArrayList<Double> secondEdges;

    public Shape(double[][] ccoordinates)
    {
        coordinates = ccoordinates;

    }

    public boolean inside(double[] point)
    {
        int left = 0, right = 0; //number of lines of polygons either side of point

        for(int i = 0; i < coordinates.length - 1; i++) //cycles through all sides (final side is covered after)
        {
            if(Utility.inbetween(coordinates[i][1], coordinates[i + 1][1], point[1])) //if point is in between the y coordinates of the current side
            {
                //if the intersection of the horisontal line of the point and the current side has a higher x value than the point, line must be to the right of point
                if(Utility.intersectValue(new double[][] {coordinates[i], coordinates[i+1]}, new double[][] {point, {point[0] + 1, point[1]}}) >= point[0])
                { right++; }
                else { left++; } //otherwise to the left
            }
        }
        if(Utility.inbetween(coordinates[0][1], coordinates[coordinates.length -1][1], point[1]))
        {
            if(Utility.intersectValue(new double[][] {coordinates[0], coordinates[coordinates.length -1]}, new double[][] {point, {point[0] + 1, point[1]}}) >= point[0])
            { right++; }
            else { left++; }
        }

        return (right % 2) == 1;
    }

    public void paint(Graphics g, boolean fill)
    {
        int[] xPoints = new int[coordinates.length];
        int[] yPoints = new int[coordinates.length];

        for(int i =0; i < coordinates.length; i++)
        {
            xPoints[i] = (int)(coordinates[i][0]);
            yPoints[i] = (int)(coordinates[i][1]);
        }

        if(fill)
        { g.fillPolygon(xPoints, yPoints, coordinates.length); }
        else { g.drawPolygon(xPoints, yPoints, coordinates.length); }
    }

    public double[][] getCoordinates() { return coordinates; }

    /*
    public double[] getPreviousPoint(MyPoint p)
    {
        //incase its the first, loops back to the last
        if(coordinates[0][0] == p.x && coordinates[0][1] == p.y)
        {
            return coordinates[coordinates.length -1];
        }

        for(int i = 1; i < coordinates.length; i++)
        {
            if(coordinates[i][0] == p.x && coordinates[i][1] == p.y)
            {
                return coordinates[i -1];
            }
        }

        //if given coordinate isnt part of the shape
        return null;
    }
    //*/

    public double[] getCoordinate(int index) { return coordinates[index]; }
}
