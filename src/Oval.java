import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Oval extends PaintObject {

	public Oval(Color color, Point2D from, Point2D to) {
		super(color, from, to);
	}

	@Override
	public void draw(GraphicsContext g) {
		g.setFill(color);
		double x = Math.min(from.getX(), to.getX());
		double y = Math.min(from.getY(), to.getY());
		double width = Math.abs(from.getX() - to.getX());
		double height = Math.abs(from.getY() - to.getY());
		g.fillOval(x, y, width, height);
	}

}
