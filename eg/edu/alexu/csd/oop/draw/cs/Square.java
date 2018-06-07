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

public class Square extends ShapeImp {
	private final static String LENGTH = new String("width");

	
	public Square() {
		properties.put(LENGTH, DUMMY_VALUE);
	}

	@Override
	public void draw(Graphics canvas) {
		super.draw(canvas);
		Graphics2D g = (Graphics2D) canvas;
		Rectangle2D sqaure = new Rectangle2D.Double(getPosition().x,getPosition().y,getProperties().get(LENGTH),getProperties().get(LENGTH));
		g.setPaint(getFillColor());
		g.fill(sqaure);
		g.setColor(getColor());
		g.draw(sqaure);
	}

	@Override
	public void move(Point newPoint)
	{
		setPosition(newPoint);
	}
	@Override
	public void resize(JPanel x) {

		x.addMouseListener(Paint.SQR_ADABTER);
		x.addMouseMotionListener(Paint.SQR_ADABTER);
		PaintManager.setSqr(getPosition(), 1);
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		Square cloned = new Square();
		cloned.setColor(new Color(getColor().getRGB()));
		cloned.setFillColor(new Color(getFillColor().getRGB()));
		cloned.setPosition((Point) getPosition().clone());
		Map< String, Double> clonedProberties = new HashMap<>();
		clonedProberties.put(LENGTH, getProperties().get(LENGTH));
		cloned.setProperties(clonedProberties);
		return cloned;
		
	}
}
