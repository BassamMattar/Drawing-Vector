package eg.edu.alexu.csd.oop.draw.cs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import eg.edu.alexu.csd.oop.draw.Shape;
import gui.Paint;

public class ShapeImp implements Shape {
	private Point position;
	public Map<String, Double> properties = new HashMap<String, Double>();
	private Color outerColor;
	private Color fillColor;
	protected static final double DUMMY_VALUE = 0;
	@Override
	public void setPosition(final Point position) {
		this.position = position;
	}

	@Override
	public Point getPosition() {
		return position;
	}

	@Override
	public void setProperties(final Map<String, Double> properties) {
		this.properties = properties;
	}

	@Override
	public Map<String, Double> getProperties() {
		return properties;
	}

	@Override
	public void setColor(final Color color) {
		this.outerColor = color;
	}

	@Override
	public Color getColor() {
		return outerColor;
	}

	@Override
	public void setFillColor(final Color color) {
		fillColor = color;
	}

	@Override
	public Color getFillColor() {
		return fillColor;
	}

	@Override
	public void draw(final Graphics canvas) {
		canvas.setColor(fillColor);
	}

	/**
	 * @throws CloneNotSupportedException
	 * @return clone of the shape
	 */
	public Object clone() throws CloneNotSupportedException {
		ShapeImp cloned = new ShapeImp();
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

	/**
	 *
	 */
	public void move(Point newPoint) {

	}

	/**
	 *
	 */
	public void resize(JPanel x) {
		
	}

}
