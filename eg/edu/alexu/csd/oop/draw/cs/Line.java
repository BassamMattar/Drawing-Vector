package eg.edu.alexu.csd.oop.draw.cs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import controller.PaintManager;
import gui.Paint;

public class Line extends ShapeImp {
	private final static String X1 = "xline";
	private final static String Y1 = "yline";


	public Line() {
		properties.put(X1, DUMMY_VALUE);
		properties.put(Y1, DUMMY_VALUE);
	}

	@Override
	public void draw(Graphics canvas) {
		super.draw(canvas);
		Graphics2D g = (Graphics2D) canvas;
		Line2D line = new Line2D.Double(getPosition().x, getPosition().y, getProperties().get(X1),
				getProperties().get(Y1));
		g.setColor(getColor());
		g.draw(line);

	}

	@Override
	public void move(Point newPoint) {
		int deltaX = newPoint.x - getPosition().x;
		int deltaY = newPoint.y - getPosition().y;
		setPosition(newPoint);
		properties.put(X1, getProperties().get(X1)+deltaX);
		properties.put(Y1, getProperties().get(Y1)+deltaY);
	}
	@Override
	public void resize(JPanel x) {

		x.addMouseListener(Paint.LINE_ADABTER);
		x.addMouseMotionListener(Paint.LINE_ADABTER);
		PaintManager.setLine(getPosition(),1);
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		Line cloned = new Line();
		cloned.setColor(new Color(getColor().getRGB()));
		cloned.setPosition((Point) getPosition().clone());
		Map<String, Double> clonedProberties = new HashMap<>();
		for(String key : properties.keySet())
		{
			clonedProberties.put(key, properties.get(key));
		}
		cloned.setProperties(clonedProberties);
		return cloned;
	}
}
