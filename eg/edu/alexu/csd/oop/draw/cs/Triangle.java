package eg.edu.alexu.csd.oop.draw.cs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.text.Position;

import controller.PaintManager;
import gui.Paint;

public class Triangle extends ShapeImp {
	private final static String X2 = new String("x2");
	private final static String X3 = new String("x3");
	private final static String Y2 = new String("y2");
	private final static String Y3 = new String("y3");

	public Triangle() {
		properties.put(X2, DUMMY_VALUE);
		properties.put(X3, DUMMY_VALUE);
		properties.put(Y2, DUMMY_VALUE);
		properties.put(Y3, DUMMY_VALUE);

	}

	@Override
	public void draw(Graphics canvas) {
		super.draw(canvas);
		Graphics2D g2 = (Graphics2D) canvas;
		Polygon p = new Polygon();
		p.addPoint(getPosition().x,getPosition().y);
		p.addPoint(getProperties().get(X2).intValue(),getProperties().get(Y2).intValue());
		p.addPoint(getProperties().get(X3).intValue(),getProperties().get(Y3).intValue());
		g2.setPaint(getFillColor());
		g2.fill(p);
		g2.setColor(getColor());
		g2.draw(p);		
	}
	@Override
	public void move(Point newPoint)
	{
		int deltaX = newPoint.x - getPosition().x;
		int deltaY = newPoint.y - getPosition().y;
		setPosition(new Point(getPosition().x + deltaX, getPosition().y + deltaY));

		getProperties().put(X2, getProperties().get(X2) + deltaX);
		getProperties().put(X3, getProperties().get(X3) + deltaX);

		getProperties().put(Y2, getProperties().get(Y2) + deltaY);
		getProperties().put(Y3, getProperties().get(Y3) + deltaY);
	}
	@Override
	public void resize(JPanel x) {

		x.addMouseListener(Paint.TRIANGLE_ADABTER);
		x.addMouseMotionListener(Paint.TRIANGLE_ADABTER);
		PaintManager.setTri(getPosition(), 1);
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		Triangle cloned = new Triangle();
		cloned.setColor(new Color(getColor().getRGB()));
		cloned.setFillColor(new Color(getFillColor().getRGB()));
		cloned.setPosition((Point) getPosition().clone());
		Map< String, Double> clonedProberties = new HashMap<>();
		clonedProberties.put(X2, getProperties().get(X2));
		clonedProberties.put(Y2, getProperties().get(Y2));
		clonedProberties.put(X3, getProperties().get(X3));
		clonedProberties.put(Y3, getProperties().get(Y3));
		cloned.setProperties(clonedProberties);
		return cloned;
		
	}

}

