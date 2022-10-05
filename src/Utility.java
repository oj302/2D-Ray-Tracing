public class Utility
{
    public static boolean above(double[][] line, double[] point)
    {
        //returns whether point is above line, if on line, false
        return ((point[1] - line[0][1]) * (line[1][0] - line[0][0]) > (line[1][1] - line[0][1]) * (point[0] - line[0][0])); //(yp - y1) * (x2 - x1) > (y2 - y1) * (xp - x1)
    }

    public static double gradient(double[][] line)
    {
        return (double)(line[1][1] - line[0][1]) / (double)(line[1][0] - line[0][0]);
    }

    public static boolean inbetween(double p1, double p2, double middle)
    { return ((p1 <= middle) && (middle <= p2)) || ((p2 <= middle) && (middle <= p1)); }


    public static double intersectValue(double[][] line1, double[][] line2) //returns x value of intersection of lines, doesnt have to be inbetween both points
    {
        double m1 = gradient(line1);
        double m2 = gradient(line2);

        return (line2[0][1] - line1[0][1] + (m1 * line1[0][0]) - (m2 * line2[0][0])) / (m1 - m2);
    }

    public static boolean intersects(double[][] line1, double[][] line2)
    {
        double x = intersectValue(line1, line2);
        return inbetween(line1[0][0], line1[1][0], x) && inbetween(line2[0][0], line2[1][0], x);
    }

    public static double[] toPolar(double[] origin, double[] point)
    {
        double[] cartesian = new double[] { point[0] - origin[0], point[1] - origin[1] };
        double[] polar = new double[2];
        polar[0] = Math.sqrt(Math.pow(cartesian[0], 2) + Math.pow(cartesian[1], 2));
        if(cartesian[0] == 0)
        {
            if(cartesian[1] >= 0) { polar[1] = Math.PI /2; }
            else { polar[1] = Math.PI * 3 /2; }

        }
        else { polar[1] = Math.atan(cartesian[1] / cartesian[0]); }

        if(point[0] < origin[0]) { polar[1] = - polar[1]; }

        return polar;
    }

    public static double[] toPolar(double[] origin, MyPoint point)
    {
        double[] cartesian = new double[] { point.x - origin[0], point.y - origin[1] };
        double[] polar = new double[2];
        polar[0] = Math.sqrt(Math.pow(cartesian[0], 2) + Math.pow(cartesian[1], 2));
        if(cartesian[0] == 0)
        {
            if(cartesian[1] >= 0) { polar[1] = Math.PI /2; }
            else { polar[1] = Math.PI * 3 /2; }

        }
        else { polar[1] = Math.atan(cartesian[1] / cartesian[0]); }

        //questionable
        if(point.x < origin[0]) { polar[1] = - polar[1]; }

        return polar;
    }

    //extends a given line from the first point to the edge of the screen
    //INCORRECT!!!!!!!!!!!!
    public static double[] extendLine(double[][] line, int width, int height)
    {
        //error checking for vertical lines (m = infinity)
        if(line[0][0] == line [1][0])
        {
            if(line[0][1] < line[1][1] ) { return new double[] {line[0][0], height}; }
            else  { return new double[] {line[0][0], 0}; }
        }
        /*
        if(line[0][1] == line [1][1])
        {
            if(line[0][0] < line[1][0] ) { return new double[] { width, line[0][1]}; }
            else  { return new double[] {0, line[0][1]}; }
        }

         */

        double m = (line[1][1] - line[0][1]) / (line[1][0] - line[0][0]);
        /*
        int y1 = (int)(((0 - line[0][0]) * m) + line[0][1]);
        int y2 = (int)(((width - line[0][0]) * m) + line[0][1]);
        int x1 = (int)(((0 - line[0][1]) / m) + line[0][0]);
        int x2 = (int)(((height - line[0][1]) / m) + line[0][0]);
         */

        double xValue;
        double yValue;
        double[] result = new double[2];

        if(line[0][1] < line[1][1]) { xValue = ((height - line[0][1]) / m) + line[0][0];    result[1] = height; }
        else                        { xValue = ((0 - line[0][1]) / m) + line[0][0];         result[1] = 0; }
        if(line[0][0] < line[1][0]) { yValue = ((width - line[0][0]) * m) + line[0][1];     result[0] = width; }
        else                        { yValue = ((0 - line[0][0]) * m) + line[0][1];         result[0] = 0; }

        if(xValue > 0 && xValue < width) { result[0] = xValue; }
        else { result[1] = yValue; }

        return result;
    }
}
