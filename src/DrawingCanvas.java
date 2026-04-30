import java.util.Vector;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.ToggleGroup;


/**
 * A GUI for NetPaint that has all PaintObjects drawn on it. This file also
 * represents the controller as it controls how paint objects are drawn and
 * sends new paint objects to the server. All Client objects also listen to the
 * server to read the Vector of paint objects and repaint every time any client
 * adds a new one.
 * 
 * @author Rick Mercer and Shashank Raj Pitla
 * 
 */
public class DrawingCanvas extends Application {

	RadioButton lineButton;
	RadioButton recButton;
	RadioButton ovalButton;
	RadioButton pictureButton;
	ToggleGroup shapesToggle;
	ColorPicker colorPicker;

	public static void main(String[] args) {
		launch(args);
	}

	// Use Vector instead of ArrayList
	private Vector<PaintObject> allPaintObjects;

	enum CurrentPaintObject {
		LINE, RECTANGLE, OVAL, PICTURE
	}

	private CurrentPaintObject currentShape = CurrentPaintObject.LINE;

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane all = new BorderPane();
		GridPane grid = new GridPane();
		shapesToggle = new ToggleGroup();
		colorPicker = new ColorPicker();
		RadioButton lineButton = new RadioButton("Line");
		RadioButton recButton = new RadioButton("Rectangle");
		RadioButton ovalButton = new RadioButton("Oval");
		RadioButton pictureButton = new RadioButton("Picture");

		lineButton.setToggleGroup(shapesToggle);
		recButton.setToggleGroup(shapesToggle);
		ovalButton.setToggleGroup(shapesToggle);
		pictureButton.setToggleGroup(shapesToggle);

		grid.add(lineButton, 0, 0);
		grid.add(recButton, 1, 0);
		grid.add(ovalButton, 2, 0);
		grid.add(pictureButton, 3, 0);
		grid.add(colorPicker, 4, 0);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setAlignment(Pos.CENTER);

		shapesToggle.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {
			if (lineButton.isSelected()) {
				currentShape = CurrentPaintObject.LINE;
			} else if (recButton.isSelected()) {
				currentShape = CurrentPaintObject.RECTANGLE;
			} else if (ovalButton.isSelected()) {
				currentShape = CurrentPaintObject.OVAL;
			} else if (pictureButton.isSelected()) {
				currentShape = CurrentPaintObject.PICTURE;
			}
		});
		// Put the drawing pane, a Canvas, into the center
		Canvas canvas = new Canvas(960, 700);
		setMouseHandlersOn(canvas);
		all.setCenter(canvas);
		all.setBottom(grid);

		// You will need to call allPaintObjects after you clear the Canvas with
		// fillRect.
		// At that point, draw all shapes and then draw the shape being created with
		// mouse event handlers while the current shape is being drawn at each mouse
		// move.
		allPaintObjects = new Vector<PaintObject>();

		Scene scene = new Scene(all, 960, 740);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void setMouseHandlersOn(Canvas canvas) {
	    final GraphicsContext gc = canvas.getGraphicsContext2D();
	    Point2D[] point = {null,null};
	    canvas.setOnMouseClicked(e -> {
	    	if (point[0] == null) {
	    		point[0] = new Point2D(e.getX(), e.getY());
	    	}else {
	    		point[1] = new Point2D(e.getX(), e.getY());
	    		PaintObject newObject = createPaintObject(point[0], point[1]);
	    		if (newObject != null) {
	    			allPaintObjects.add(newObject);
	    			drawAll(gc);
	    		}
	    		point[0] = null;
	    		point[1] = null;
	    	}
	    });
	    canvas.setOnMouseMoved(e -> {
	    	if (point[0] != null) {
	    		drawAll(gc);
	    		drawCurrentShape(gc, point[0],new Point2D(e.getX(), e.getY()));
	    	}
	    });
	}

	private void drawCurrentShape(GraphicsContext gc, Point2D start, Point2D end) {
		if (start == null || end == null)
			return;
		switch (currentShape) {
		case LINE:
			gc.strokeLine(start.getX(), start.getY(), end.getX(), end.getY());
			break;
		case RECTANGLE:
			gc.strokeRect(Math.min(start.getX(), end.getX()), Math.min(start.getY(), end.getY()),
					Math.abs(end.getX() - start.getX()), Math.abs(end.getY() - start.getY()));
			break;
		case OVAL:
			gc.strokeOval(Math.min(start.getX(), end.getX()), Math.min(start.getY(), end.getY()),
					Math.abs(end.getX() - start.getX()), Math.abs(end.getY() - start.getY()));
			break;
		case PICTURE:
			Picture picture = new Picture(new Point2D(start.getX(), start.getY()), new Point2D(end.getX(), end.getY()),
					"doge.jpeg");
			picture.draw(gc);
			break;
		}
	}

	private PaintObject createPaintObject(Point2D start, Point2D end) {
		javafx.scene.paint.Color fxColor = colorPicker.getValue();
		switch (currentShape) {
		case LINE:
			return new Line(fxColor, start, end);
		case RECTANGLE:
			return new Rectangle(fxColor, start, end);
		case OVAL:
			return new Oval(fxColor, start, end);
		default:
			return new Picture(start, end, "doge.jpeg");

		}
	}

	private void drawAll(GraphicsContext gc) {
		gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
		for (PaintObject obj : allPaintObjects) {
			obj.draw(gc);
		}

	}
}