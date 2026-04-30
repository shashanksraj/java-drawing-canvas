import javafx.geometry.Point2D;
import java.io.Serializable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * PaintObject is an abstract class defining a base class that allows all
 * subclassess to be drawn with two points: from and and to 
 * 
 * @author Gabriel Kishi and Rick Mercer
 */
public abstract class PaintObject implements Serializable {

  /** 
   * Needed this for Serializable Objects that change between writeObject and readObject.
   * We can also save a list of PaintObjects and send the same list over the network.
   */
  private static final long serialVersionUID = 1L;

  protected Point2D from, to;
  protected Color color;

  // All subclasses must call this constructor with super(Color, Point2D, Point2D
  public PaintObject(Color color, Point2D from, Point2D to) {
    this.color = color;
    this.from = from;
    this.to = to;
  }
   
  // Each subclass must override draw since each shape is drawn differently on a canvas
  public abstract void draw(GraphicsContext g);
}