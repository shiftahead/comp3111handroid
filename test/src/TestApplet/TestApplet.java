package TestApplet;
import javax.swing.*;
import java.awt.*;

public class TestApplet extends JApplet {
	private int x = 0;
    public void init() {
      JButton bt=new JButton("Click me");
      add(bt);
        System.out.println("Init");
    }
    public void start() {
        System.out.println("Start");
    }
    public void stop() {
        System.out.println("Stop");
        System.out.println(x++);
            }
    public void destroy() {
        System.out.println("Destroy");
        System.out.println(x++);
    }
}