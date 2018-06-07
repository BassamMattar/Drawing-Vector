package eg.edu.alexu.csd.oop.draw.cs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import controller.PaintManager;
import gui.Paint;

public class Circle extends ShapeImp {
	private final static String RADIUS = new String("Radius");
	public Circle() {
		properties.put(RADIUS, DUMMY_VALUE);
	}


	@Override
	public void draw(Graphics canvas) {
		super.draw(canvas);
		Graphics2D g2 = (Graphics2D) canvas;
		double radius = getProperties().get(RADIUS);
		Ellipse2D circle = new Ellipse2D.Double(getPosition().x, getPosition().y, radius, radius);
		g2.fill(circle);
		g2.setColor(getColor());
		g2.draw(circle);
	}

	@Override
	public void move(Point newPoint) {
		newPoint.x -= (getProperties().get(RADIUS) / 2);
		newPoint.y -= (getProperties().get(RADIUS) / 2);
		setPosition(newPoint);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Circle cloned = new Circle();
		cloned.setColor(new Color(getColor().getRGB()));
		cloned.setFillColor(new Color(getFillColor().getRGB()));
		cloned.setPosition((Point) getPosition().clone());
		Map<String, Double> clonedProberties = new HashMap<>();
		clonedProberties.put(RADIUS, getProperties().get(RADIUS));
		cloned.setProperties(clonedProberties);
		return cloned;

	}
	@Override
	public void resize(JPanel x) {
		Point upperLiftCorner = new Point();
		upperLiftCorner.x = (int) (getPosition().x + (getProperties().get(RADIUS) / 2));
		upperLiftCorner.y = (int) (getPosition().y + (getProperties().get(RADIUS) / 2));
		PaintManager.setCirc(upperLiftCorner, 1);
		x.addMouseMotionListener(Paint.CIRCLE_ADABTER);
		x.addMouseListener(Paint.CIRCLE_ADABTER);
	}
}
