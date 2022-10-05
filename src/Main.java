import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Main
{
    public final int width, height, hZ;
    private final JFrame window;
    private final JPanel canvas;
    public static Shape shape;
    public int translation =3;

    public Main(int wwidth, int hheight, int frames, Color lightColour)
    {
        width = wwidth;
        height = hheight;
        hZ = frames;

        Light light = new Light(new double[] {50, 350});

        //graphical stuff
        {
            window = new JFrame("window");

            canvas = new JPanel()
            {
                protected void paintComponent(Graphics g)
                {
                    g.setColor(new Color(45, 45, 45));
                    g.fillRect(0, 0, width, height);

                    g.setColor(new Color(0x000000));
                    shape.paint(g, true);

                    ArrayList<Shape> shapes = new ArrayList<>();
                    shapes.add(shape);
                    g.setColor(lightColour);
                    light.getLightShape(shapes, width, height).paint(g, true);

                    g.setColor(new Color(0xFFC400));
                    g.fillOval((int)light.getCoordinates()[0], (int)light.getCoordinates()[1], 5, 5);

                }
            };

            window.add(canvas);
            window.setPreferredSize(new Dimension(width, height));
            window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            window.pack();
            window.setVisible(true);
        }


        //the loop
        {

            Thread loop = new Thread()
            {
                public void run()
                {
                    super.run();

                    while (true)
                    {
                        canvas.repaint();

                        light.translate(0, translation);
                        if(light.getCoordinates()[1] < 10) { translation = 3; }
                        if(light.getCoordinates()[1] > height - 10) { translation = -3; }

                        try
                        {
                            Thread.sleep(1000 / frames);
                        } catch (InterruptedException e) {
                            System.out.println("that sucks");
                        }
                    }
                }
            };

            loop.start();
        }


    }

    public static void main(String[] args)
    {
        shape = new Shape(new double[][] {{255, 60}, {302, 24}, {288, 250}, {500, 302}, {405, 376}, {354, 503}, {244, 522}, {100, 370}});
        Main m = new Main(800, 600, 30, new Color(255, 234, 0, 128));
    }
}

