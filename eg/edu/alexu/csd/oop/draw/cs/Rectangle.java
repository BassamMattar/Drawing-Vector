package eg.edu.alexu.csd.oop.draw.cs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import controller.PaintManager;
import gui.Paint;

public class Rectangle extends ShapeImp {
	private final static String WIDTH = new String("width");
	private final static String HEIGHT = new String("height");



	public Rectangle() {
		properties.put(WIDTH, DUMMY_VALUE);
		properties.put(HEIGHT, DUMMY_VALUE);
	}

	@Override
	public void draw(Graphics canvas) {
		super.draw(canvas);
		Graphics2D g = (Graphics2D) canvas;
		Rectangle2D rectangle = new Rectangle2D.Double(getPosition().x, getPosition().y, getProperties().get(WIDTH),
				getProperties().get(HEIGHT));
		g.setPaint(getFillColor());
		g.fill(rectangle);
		g.setColor(getColor());
		g.draw(rectangle);
		

	}
	@Override
	public void resize(JPanel x) {

		x.addMouseListener(Paint.REC_ADABTER);
		x.addMouseMotionListener(Paint.REC_ADABTER);
		PaintManager.setRec(getPosition(), 1);
	}
	@Override
	public void move(Point newPoint) {
		setPosition(newPoint);
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		Rectangle cloned = new Rectangle();
		cloned.setColor(new Color(getColor().getRGB()));
		cloned.setFillColor(new Color(getFillColor().getRGB()));
		cloned.setPosition((Point) getPosition().clone());
		Map< String, Double> clonedProberties = new HashMap<>();
		clonedProberties.put(WIDTH, getProperties().get(WIDTH));
		clonedProberties.put(HEIGHT, getProperties().get(HEIGHT));
		cloned.setProperties(clonedProberties);
		return cloned;
		
	}

}
