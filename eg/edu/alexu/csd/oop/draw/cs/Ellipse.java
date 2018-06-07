package eg.edu.alexu.csd.oop.draw.cs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JPanel;

import controller.PaintManager;
import gui.Paint;

public class Ellipse extends ShapeImp {
	private final static String AXIS_X = new String("axisx");
	private final static String AXIS_Y = new String("axisy");


	public Ellipse() {
		properties.put(AXIS_X, DUMMY_VALUE);
		properties.put(AXIS_Y, DUMMY_VALUE);
	}
	@Override
	public void draw(Graphics canvas) {
		super.draw(canvas);
		Graphics2D g2 = (Graphics2D)canvas;
		double axisX = getProperties().get(AXIS_X);
		double axisY = getProperties().get(AXIS_Y);
		Ellipse2D ellipse = new Ellipse2D.Double(getPosition().x,getPosition().y,axisX,axisY);
		g2.setPaint(getFillColor());
		g2.fill(ellipse);
		g2.setColor(getColor());
		g2.draw(ellipse);
	}
	@Override
	public void move(Point newPoint)
	{
		newPoint.x -= getProperties().get(AXIS_X)/2;
		newPoint.y -= getProperties().get(AXIS_Y)/2;
		setPosition(newPoint);
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		Ellipse cloned = new Ellipse();
		cloned.setColor(new Color(getColor().getRGB()));
		cloned.setFillColor(new Color(getFillColor().getRGB()));
		cloned.setPosition((Point) getPosition().clone());
		Map<String, Double> clonedProberties = new HashMap<>();
		for(String key : properties.keySet())
		{
			clonedProberties.put(key, properties.get(key));
		}
		cloned.setProperties(clonedProberties);
		return cloned;

	}
	@Override
	public void resize(JPanel x) {
		PaintManager.setEllipse(getPosition(), 1);
		x.addMouseMotionListener(Paint.ELLIPSE_ADABTER);
		x.addMouseListener(Paint.ELLIPSE_ADABTER);
	}
}
