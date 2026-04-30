import java.io.File;
import java.net.MalformedURLException;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Picture extends PaintObject {
	private Image image;

	public Picture(Point2D from, Point2D to, String filePath) {
		super(null, from, to);
		File file = new File("file:" + filePath);
		try {
			String imagePath = file.toURI().toURL().toExternalForm();
			image = new Image("file:08_Drawing/" + filePath);
		} catch (MalformedURLException e) {
			throw new RuntimeException("Invalid path", e);
		}
	}

	@Override
	public void draw(GraphicsContext g) {
		double width = Math.abs(to.getX() - from.getX());
		double height = Math.abs(to.getY() - from.getY());
		double minX = Math.min(from.getX(), to.getX());
		double minY = Math.min(from.getY(), to.getY());

		g.drawImage(image, minX, minY, width, height);
	}
}