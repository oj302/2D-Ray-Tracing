import java.util.ArrayList;
import java.util.Comparator;

public class Light
{
    private static int numberOfLights = 0;
    private final double[] coordinates;
    //used to indentify which light is which when looking at shapes edge angles (FOR OPTIMISATION)
    public final int index;

    public Light(double[] ccoordinates)
    {
        coordinates = ccoordinates;
        index = numberOfLights;
        numberOfLights++;
    }

    public double[] getCoordinates() { return coordinates; }
    public void translate(double x, double y) {coordinates[0] += x; coordinates[1] += y;}

    public Shape getLightShape(ArrayList<Shape> shapes, int width, int height) {
        //get list of points in order of angle with the shape they belong to
        ArrayList<MyPoint> points = new ArrayList<>();
        ArrayList<double[]> frontPoints = new ArrayList<>();

        //double closestDistance = Utility.toPolar(coordinates, shapes.get(0).getCoordinate(0))[0];
        //int closestIndex = 0;

        for (Shape s : shapes) {
            for (double[] c : s.getCoordinates()) {
                points.add(new MyPoint(c[0], c[1], s));
            }
        }

        points.add(new MyPoint(0, 0, null));
        points.add(new MyPoint(width, 0, null));
        points.add(new MyPoint(0, height, null));
        points.add(new MyPoint(width, height, null));

        //comparator to sort list by angle
        Comparator<MyPoint> thetaComparator = new Comparator<MyPoint>() {
            @Override
            public int compare(MyPoint p1, MyPoint p2) {
                double theta1 = Utility.toPolar(coordinates, p1.toCoord())[1];
                double theta2 = Utility.toPolar(coordinates, p2.toCoord())[1];
                return Double.compare(theta1, theta2);
            }
        };
        points.sort(thetaComparator);

        //DEBUGGING
        System.out.println("\n\npoints: ");
        for (MyPoint p : points)
        {
            System.out.println(p.x+", "+p.y+" theta: "+Utility.toPolar(coordinates, p.toCoord())[1]);
        }

        //go through points in order, check if theyre at the front

        //if the first point isnt at the front this will cause problems!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //dont know which point to start at

        MyPoint previousFront = points.get(0);
        boolean onCatchUp = false;
        for(MyPoint p : points)
        {
            if(frontPoints.isEmpty())
            {
                frontPoints.add(points.get(0).toCoord());
                System.out.println("\n\n\nadding first point: "+frontPoints.get(frontPoints.size() -1)[0]+", "+frontPoints.get(frontPoints.size() -1)[1]);
            }
            else if(onCatchUp)
            {
                System.out.println("on catchup????");
                //skip points until the point you are checking is the one most recently added to front points
                //continue as normal for next point
                if(p.equalsCoord(previousFront.toCoord()))
                {
                    onCatchUp = false;
                }
            }
            else
            {
                //previous point didnt belong to a shape, edge of screen
                //current point will always be used
                if(previousFront.shape == null)
                {
                    frontPoints.add(Utility.extendLine(new double[][] {coordinates, p.toCoord()}, width, height));
                    System.out.println("start of new shape edge of screen: "+frontPoints.get(frontPoints.size() -1)[0]+", "+frontPoints.get(frontPoints.size() -1)[1]);
                    frontPoints.add(p.toCoord());
                    System.out.println("start of new shape first point: "+frontPoints.get(frontPoints.size() -1)[0]+", "+frontPoints.get(frontPoints.size() -1)[1]);
                    previousFront = p;
                }
                //if the last point in the front and the point being currently looked at are from the same shape, the current point must be in front
                else if(p.equalsCoord(previousFront.getPreviousPoint()))
                {
                    frontPoints.add(p.toCoord());
                    System.out.println("same shape continues: "+frontPoints.get(frontPoints.size() -1)[0]+", "+frontPoints.get(frontPoints.size() -1)[1]);
                    previousFront = p;
                }
                //most recent point added in the front points, last point the light "hit"
                //if the light intersects the line between the previous front and the next point in the same shape whilst
                //trying to get to the point being looked at, the light does not reach the point being looked at
                else if (!Utility.intersects(
                        //front point and point in shape after it
                        new double[][]{previousFront.toCoord(), previousFront.getPreviousPoint()},
                        //coordinates of light source and point being checked
                        new double[][]{coordinates, p.toCoord()}))
                {
                    //compares theta values of the shape belonging to the last point on the front
                    //checks if we just hit the edge of the shape
                    //alternative is that another shape is blocking the light from hitting the shape
                    if(Utility.toPolar(coordinates, previousFront.toCoord())[1] > Utility.toPolar(coordinates, previousFront.getPreviousPoint())[1])
                    {
                        System.out.println("hit end edge of shape");
                        //more processing here, have to find where the light stops
                        //previously find "edge angles" of every shape, use to find where this light beam stops (FOR OPTIMISATION)
                        //add double to frontPoints
                        //REMEMBER CORNERS

                        //algorithm has just found the edge of the last shape
                        //light beam is extended to the edge of the screen
                        double[] edgePoint = Utility.extendLine(new double[][]{coordinates, previousFront.toCoord()}, width, height);
                        System.out.println("* line continues to "+edgePoint[0]+", "+edgePoint[1]);
                        System.out.println("* previous front was "+previousFront.toString());
                        MyPoint currentClosest = new MyPoint(edgePoint[0], edgePoint[1], null);
                        double shortestDistance = Math.abs(coordinates[0] - currentClosest.x);

                        //see if this light beam intersects any shapes edges, find the closest intersection
                        //PROBLEM: its hitting its self!!!!!!!!!!!!!!!!!!!!!!!!!!
                        for (MyPoint pp : points)
                        {
                            if(pp.shape != null && !previousFront.equalsCoord(pp.getPreviousPoint()) && !previousFront.equalsCoord(pp.toCoord()) &&
                                    Utility.intersects(new double[][]{coordinates, currentClosest.toCoord()}, new double[][]{pp.toCoord(), pp.getPreviousPoint()}))
                            {
                                double intersectX = Utility.intersectValue(new double[][]{coordinates, currentClosest.toCoord()}, new double[][]{pp.toCoord(), pp.getPreviousPoint()});

                                if(Math.abs(coordinates[0] - intersectX) < shortestDistance)
                                {
                                    System.out.println("* new closest intersect, in between "+pp.toString()+" and "+pp.getPreviousPoint()[0]+", "+pp.getPreviousPoint()[1]);
                                    shortestDistance = intersectX;
                                    currentClosest = pp;
                                }
                            }
                        }

                        //no intersect at all, hits side of window
                        if (currentClosest.shape == null)
                        {   //probab;y unecessary

                            System.out.println("* previous front: "+previousFront.toString());
                            frontPoints.add(edgePoint);
                            System.out.println("hits edge of screen from end of shape: "+frontPoints.get(frontPoints.size() -1)[0]+", "+frontPoints.get(frontPoints.size() -1)[1]);
                            frontPoints.add(p.toCoord());
                            System.out.println("add next point to front "+frontPoints.get(frontPoints.size() -1)[0]+", "+frontPoints.get(frontPoints.size() -1)[1]);
                            previousFront = p;
                        }
                        else
                        {
                            System.out.println("another shape hit behind last");
                            //finds coordinates of intersect and adds a point there
                            double xIntersect = Utility.intersectValue(new double[][]{coordinates, edgePoint}, new double[][]{currentClosest.toCoord(), currentClosest.getPreviousPoint()});
                            double yIntersect = ((edgePoint[1] - coordinates[1]) * (xIntersect - coordinates[0]) / (edgePoint[0] - coordinates[0])) + coordinates[1];
                            //intersect
                            frontPoints.add(new double[]{xIntersect, yIntersect});
                            System.out.println("hits another shape behind last intersect: "+frontPoints.get(frontPoints.size() -1)[0]+", "+frontPoints.get(frontPoints.size() -1)[1]);
                            frontPoints.add(currentClosest.toCoord());
                            System.out.println("First front point in new shape: "+frontPoints.get(frontPoints.size() -1)[0]+", "+frontPoints.get(frontPoints.size() -1)[1]);
                            previousFront = currentClosest;

                            onCatchUp = true;
                            //couldve missed some points whilst jumping from the intersect to the next point in the shape
                        }
                    }

                    //another shape is blocking the previous shape from getting light
                    else
                    {
                        System.out.println("shape in front of previous");
                        //finds where the light hitting the edge of the old shape hits the new shape
                        double xIntersect = Utility.intersectValue(new double[][] {coordinates, p.toCoord()}, new double[][] {previousFront.toCoord(), previousFront.getPreviousPoint()});
                        double yIntersect = ((p.toCoord()[1] - coordinates[1]) * (xIntersect - coordinates[0]) / (p.toCoord()[0] - coordinates[0])) - coordinates[1];

                        //intersect
                        frontPoints.add(new double[]{xIntersect, yIntersect});
                        System.out.println("shape in front, intersect of last shape: "+frontPoints.get(frontPoints.size() -1)[0]+", "+frontPoints.get(frontPoints.size() -1)[1]);
                        frontPoints.add(p.toCoord());
                        System.out.println("shape in front, first point of new shape: "+frontPoints.get(frontPoints.size() -1)[0]+", "+frontPoints.get(frontPoints.size() -1)[1]);
                        previousFront = p;

                    }
                }
            }
        }

        System.out.println("output: ");
        double[][] shape = new double[frontPoints.size()][];
        for(int i = 0; i < shape.length; i++)
        {
            shape[i] = frontPoints.get(i);
            System.out.println(frontPoints.get(i)[0]+", "+frontPoints.get(i)[1]);
        }
        return new Shape(shape);
    }

}
