public class MyPoint
{
    final double x;
    final double y;
    final Shape shape;

    public MyPoint(double xx, double yy, Shape sshape)
    {
        x = xx;
        y = yy;
        shape = sshape;
    }

    public double[] getPreviousPoint()
    {
        //incase its the first, loops back to the last
        if(shape.getCoordinate(0)[0] == x && shape.getCoordinate(0)[1] == y)
        {
            return shape.getCoordinate(shape.getCoordinates().length -1);
        }

        for(int i = 1; i < shape.getCoordinates().length; i++)
        {
            if(shape.getCoordinate(i)[0] == x && shape.getCoordinate(i)[1] == y)
            {
                return shape.getCoordinate(i -1);
            }
        }

        //if given coordinate isnt part of the shape
        return null;
    }

    public double[] toCoord() { return new double[] {x, y}; }

    public boolean equalsCoord(double[] coord) { return x == coord[0] && y == coord[1]; }

    public String toString()
    {
        return x+", "+y;
    }
}
