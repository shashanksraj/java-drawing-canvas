import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Line extends PaintObject {
	
	public Line(Color color, Point2D to, Point2D from) {
		super(color, to, from);
	}

	@Override
	public void draw(GraphicsContext g) {
		g.setStroke(color);
		g.strokeLine(from.getX(), from.getY(), to.getX(), to.getY());
	}
}
