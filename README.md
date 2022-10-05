# 2D-Ray-Tracing

## Description
Given a list of 2D shapes and coordinates for a light source, this algorithm will draw the rays of light from the source.

Shapes are given as 2D arrays of doubles in coordinate pairs (e.g [[x1, y1], [x2, y2] ... ]) in clockwise order
Light source is then created and can be moved around the window using its translation function and its beams will update in real time
In theory many light sources can be used at once but it has not been tested

The code comes ready to run with a demonstration of a moving light source (works in Java 9)

## Overview of Classes

### Light
Stores coordintes for the light object and comes with a function to translate its self
The main function of the whole program is getLightShape which takes all shapes as parameters as well as the width and height of the window.
This function returns the "shape" the light beam creates (a 2D array of doubles in coordinate pairs)

It does this by first ordering points by their angle using the light source as a refference. It then chooses a starting point (at the moment it is the first 
point in the list, this will be changed as it causes problems). It then continues looking at points in angle order. If the point is visible to the light beam it
is added to the frontPoints list which later makes up the light shape. If the edge of a shape is hit on either side the light beam is continued and the point it next
intersects must be found and added to the frontPoints list.

### Main
* Creates GUI and loop to refresh screen / update light position in constructor
* Shapes can be added / modified under psvm function
* Light initial coordinates can also be changed under psvm

### MyPoint
* Sometimes used to store a point
* Also stores the shape it belongs to
* Can find the previous point in the same shape

### Shape
* Stores a 2D array representing coordinates of the shape
* Points are stored in clockwise order

### Utility
* Utility class with various useful geometric functions
* Can convert cartesian points to polar using a refference point


## Future Improvements
### Bugs:
* First point added to frontPoints could be behind another shape, creating an incorrect light shape
* Light beams dont work when theyre off screen
* Light doesnt work when to the left side of shape
* Multiple shapes have not been tested yet
* Light source should "turn off" when placed in a shape
* Multiple light sources has not been tested

### Improvements:
* Make light source follow mouse
* Make shapes procedurally generated
