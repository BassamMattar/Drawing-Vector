package eg.edu.alexu.csd.oop.draw.cs;

import java.awt.Graphics;
import java.awt.Point;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import javax.swing.JOptionPane;

import java.util.Stack;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import eg.edu.alexu.csd.oop.draw.DrawingEngine;
import eg.edu.alexu.csd.oop.draw.Shape;

public class DrawingEngineImp implements DrawingEngine {
	private static final int MAX_UNDO = 20;
	private static final int INDEX0 = 0;
	private static final int INDEX1 = 1;
	// public ArrayList<Class> supportedClasses = new ArrayList<>();
	public Class newClass;

	public String jarPath = "";
	public Constructor plugin;

	private boolean redone = false;
	private boolean undone = false;
	private LinkedList<Shape> shapes = new LinkedList<>();
	private Stack<LinkedList<Shape>> undo = new Stack<>();
	private Stack<LinkedList<Shape>> redo = new Stack<>();
	private Shape[] shapeArray;

	private JSONObject jsonWriter;
	private JSONObject jsonTotal = new JSONObject();
	private JSONArray JsonArray;
	private JSONParser parser = new JSONParser();
	private JSONObject returnObject;
	private Constructor cs;
	public Map<String, Double> PlugInMap = new HashMap<>();
	public Color PlugFillColor;
	public Color PlugBorderColor;
	public Point PosPlug = new Point();

	@Override
	public void refresh(Graphics canvas) {
		canvas.clearRect(INDEX0, INDEX0, 1800, 1000);
		for (int i = 0; i < shapes.size(); i++) {
			shapes.get(i).draw(canvas);
		}
	}

	@Override
	public void addShape(Shape shape) {
		storeUndo(shapes);
		shapes.add(shape);
		storeRedo(shapes);
	}

	@Override
	public void removeShape(Shape shape) {
		for (int i = 0; i < shapes.size(); i++) {
			if (shape.equals(shapes.get(i))) {
				storeUndo(shapes);
				shapes.remove(i);
				storeRedo(shapes);
			}
		}
	}

	@Override
	public void updateShape(Shape oldShape, Shape newShape) {
		for (int i = 0; i < shapes.size(); i++) {
			if (oldShape.equals(shapes.get(i))) {
				storeUndo(shapes);
				shapes.remove(i);
				shapes.add(i, newShape);
				storeRedo(shapes);
			}
		}
	}

	@Override
	public Shape[] getShapes() {
		Shape[] shapesArray = new Shape[shapes.size()];
		shapes.toArray(shapesArray);
		return shapesArray;
	}

	@Override
	public List<Class<? extends Shape>> getSupportedShapes() {
		LinkedList<Class<? extends Shape>> supportedShapes = new LinkedList<>();
		supportedShapes.add(Line.class);
		supportedShapes.add(Triangle.class);
		supportedShapes.add(Circle.class);
		supportedShapes.add(Rectangle.class);
		supportedShapes.add(Square.class);
		supportedShapes.add(Ellipse.class);
		supportedShapes.add(newClass);
		/*
		 * for(int i=0;i<supportedClasses.size();i++){ supportedShapes.add();
		 * 
		 * }
		 */
		return supportedShapes;
	}

	@Override
	public void undo() {
		if (undo.isEmpty()) {
			return;
		}
		while (undo.peek().equals(shapes)) {
			redo.push((LinkedList<Shape>) undo.pop().clone());
			if (undo.isEmpty()) {
				return;
			}
		}
		redo.push((LinkedList<Shape>) undo.peek().clone());
		shapes = undo.pop();
	}

	@Override
	public void redo() {
		if (redo.isEmpty()) {
			return;
		}
		while (redo.peek().equals(shapes)) {
			undo.push((LinkedList<Shape>) redo.pop().clone());
			if (redo.isEmpty()) {
				return;
			}
		}
		undo.push((LinkedList<Shape>) redo.peek().clone());
		shapes = redo.pop();
	}

	@Override
	public void save(String path) {
		// TODO Auto-generated method stub

		String fileName = path.substring(path.lastIndexOf("\\") + 1);
		String[] extension = fileName.split("\\.");
		if (path.charAt(path.length() - 1) == 'l') {
			saveXML(path);
		} else if (path.charAt(path.length() - 1) == 'n') {
			saveJSON(path);
		} else {
			throw null;
		}

	}

	@Override
	public void load(String path) {
		// TODO Auto-generated method stub
		shapes.clear();
		String fileName = path.substring(path.lastIndexOf("\\") + 1);
		String[] extension = fileName.split("\\.");

		if (path.charAt(path.length() - 1) == 'l') {
			loadXML(path);
		} else if (path.charAt(path.length() - 1) == 'n') {
			loadJSON(path);
		} else {
			throw null;
		}

	}

	/*
	 * @param x store in stack
	 */
	private void storeUndo(LinkedList<Shape> x) {
		LinkedList<Shape> deepCopy = (LinkedList<Shape>) x.clone();
		undo.push(deepCopy);
		if (undo.size() > MAX_UNDO) {
			undo.remove(INDEX0);
		}
	}

	/*
	 * @param x store in stack
	 */
	private void storeRedo(LinkedList<Shape> x) {
		LinkedList<Shape> deepCopy = (LinkedList<Shape>) x.clone();
		redo.push(deepCopy);
		while (redo.size() > INDEX1) {
			redo.remove(INDEX0);
		}
	}

	/*
	 * 
	 */
	public void addVisuals(Shape x) {
		shapes.add(x);
	}

	/*
	 *
	 */
	public void removeVisual() {
		shapes.removeLast();
	}

	public void loadXML(String path) {
		try {
			XMLDecoder decode = new XMLDecoder(new BufferedInputStream(new FileInputStream(path)));
			shapes = (LinkedList<Shape>) decode.readObject();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public void saveXML(String path) {
		try (FileWriter newfile = new FileWriter(path)) {
			XMLEncoder pen = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(path)));
			pen.writeObject(shapes);
			pen.close();

		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private void loadJSON(String path) {

		try {

			Object obj = parser.parse(new FileReader(path));
			returnObject = (JSONObject) obj;
			JsonArray = (JSONArray) returnObject.get("Shapes");
			for (int i = 0; i < JsonArray.size(); i++) {
				JSONObject jsonobject = (JSONObject) JsonArray.get(i);
				String name = jsonobject.get("name").toString();
				if (name.equalsIgnoreCase("Circle")) {
					Circle newCircle = new Circle();
					Point x = new Point();
					long x1 = (long) jsonobject.get("pointx");
					long y1 = (long) jsonobject.get("pointy");
					x.x = (int) (long) x1;
					x.y = (int) (long) y1;
					Color newColor = new Color((int) (long) jsonobject.get("ColorR"),
							(int) (long) jsonobject.get("ColorG"), (int) (long) jsonobject.get("ColorB"));
					Color newFillColor = new Color((int) (long) jsonobject.get("FillColorR"),
							(int) (long) jsonobject.get("FillColorG"), (int) (long) jsonobject.get("FillColorB"));
					newCircle.setPosition(x);
					newCircle.properties.put("Radius", (Double) jsonobject.get("Radius"));
					newCircle.setColor(newColor);
					newCircle.setFillColor(newFillColor);
					addShape(newCircle);

				} else if (name.equalsIgnoreCase("Rectangle")) {
					Rectangle newRectangle = new Rectangle();
					Point x = new Point();
					long x1 = (long) jsonobject.get("pointx");
					long y1 = (long) jsonobject.get("pointy");
					x.x = (int) (long) x1;
					x.y = (int) (long) y1;
					Color newColor = new Color((int) (long) jsonobject.get("ColorR"),
							(int) (long) jsonobject.get("ColorG"), (int) (long) jsonobject.get("ColorB"));
					Color newFillColor = new Color((int) (long) jsonobject.get("FillColorR"),
							(int) (long) jsonobject.get("FillColorG"), (int) (long) jsonobject.get("FillColorB"));
					newRectangle.setPosition(x);
					newRectangle.properties.put("width", (Double) jsonobject.get("width"));
					newRectangle.properties.put("height", (Double) jsonobject.get("height"));
					newRectangle.setColor(newColor);
					newRectangle.setFillColor(newFillColor);

					addShape(newRectangle);

				} else if (name.equalsIgnoreCase("Square")) {
					Square newSquare = new Square();
					Point x = new Point();
					long x1 = (long) jsonobject.get("pointx");
					long y1 = (long) jsonobject.get("pointy");
					x.x = (int) (long) x1;
					x.y = (int) (long) y1;
					Color newColor = new Color((int) (long) jsonobject.get("ColorR"),
							(int) (long) jsonobject.get("ColorG"), (int) (long) jsonobject.get("ColorB"));
					Color newFillColor = new Color((int) (long) jsonobject.get("FillColorR"),
							(int) (long) jsonobject.get("FillColorG"), (int) (long) jsonobject.get("FillColorB"));
					newSquare.setPosition(x);
					newSquare.properties.put("width", (Double) jsonobject.get("width"));
					newSquare.setColor(newColor);
					newSquare.setFillColor(newFillColor);
					addShape(newSquare);

				} else if (name.equalsIgnoreCase("Ellipse")) {
					Ellipse newEllipse = new Ellipse();
					Point x = new Point();
					long x1 = (long) jsonobject.get("pointx");
					long y1 = (long) jsonobject.get("pointy");
					x.x = (int) (long) x1;
					x.y = (int) (long) y1;
					Color newColor = new Color((int) (long) jsonobject.get("ColorR"),
							(int) (long) jsonobject.get("ColorG"), (int) (long) jsonobject.get("ColorB"));
					Color newFillColor = new Color((int) (long) jsonobject.get("FillColorR"),
							(int) (long) jsonobject.get("FillColorG"), (int) (long) jsonobject.get("FillColorB"));
					newEllipse.setPosition(x);
					newEllipse.properties.put("axisx", (Double) jsonobject.get("axisx"));
					newEllipse.properties.put("axisy", (Double) jsonobject.get("axisy"));
					newEllipse.setColor(newColor);
					newEllipse.setFillColor(newFillColor);
					addShape(newEllipse);

				} else if (name.equalsIgnoreCase("Line")) {
					Line newLine = new Line();
					Point x = new Point();
					long x1 = (long) jsonobject.get("pointx");
					long y1 = (long) jsonobject.get("pointy");
					x.x = (int) (long) x1;
					x.y = (int) (long) y1;
					Color newColor = new Color((int) (long) jsonobject.get("ColorR"),
							(int) (long) jsonobject.get("ColorG"), (int) (long) jsonobject.get("ColorB"));
					newLine.setPosition(x);
					newLine.properties.put("xline", (Double) jsonobject.get("xline"));
					newLine.properties.put("yline", (Double) jsonobject.get("yline"));
					newLine.setColor(newColor);
					addShape(newLine);

				} else if (name.equalsIgnoreCase("Triangle")) {
					Triangle newTriangle = new Triangle();
					Point x = new Point();
					long x1 = (long) jsonobject.get("pointx");
					long y1 = (long) jsonobject.get("pointy");
					x.x = (int) (long) x1;
					x.y = (int) (long) y1;
					Color newColor = new Color((int) (long) jsonobject.get("ColorR"),
							(int) (long) jsonobject.get("ColorG"), (int) (long) jsonobject.get("ColorB"));
					Color newFillColor = new Color((int) (long) jsonobject.get("FillColorR"),
							(int) (long) jsonobject.get("FillColorG"), (int) (long) jsonobject.get("FillColorB"));
					newTriangle.setPosition(x);
					newTriangle.properties.put("x1", (Double) jsonobject.get("x1"));
					newTriangle.properties.put("x2", (Double) jsonobject.get("x2"));
					newTriangle.properties.put("x3", (Double) jsonobject.get("x3"));
					newTriangle.properties.put("y1", (Double) jsonobject.get("y1"));
					newTriangle.properties.put("y2", (Double) jsonobject.get("y2"));
					newTriangle.properties.put("y3", (Double) jsonobject.get("y3"));
					newTriangle.setColor(newColor);
					newTriangle.setFillColor(newFillColor);
					addShape(newTriangle);

				} else if (name.equalsIgnoreCase("Null")) {
					addShape(null);

				} else {
					String pathname = (String) jsonobject.get("path");
					try {
						JarInputStream jarFile = new JarInputStream(new FileInputStream(pathname));
						File myJar = new File(pathname);
						URL url = myJar.toURI().toURL();
						Class[] parameters = new Class[] { URL.class };
						URLClassLoader sysLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
						Class sysClass = URLClassLoader.class;
						Method method = sysClass.getDeclaredMethod("addURL", parameters);
						method.setAccessible(true);
						method.invoke(sysLoader, new Object[] { url });
						JarEntry jarEntry;
						while (true) {
							jarEntry = jarFile.getNextJarEntry();
							if (jarEntry == null) {
								break;
							}
							if (jarEntry.getName().endsWith(".class")) {
								String name1 = jarEntry.getName().replaceAll("/", "\\.").replace(".class", "");
								JOptionPane.showMessageDialog(null, name1);
								Constructor cs = ClassLoader.getSystemClassLoader().loadClass(name1).getConstructor();
								this.addPlug(cs);
							}
						}
						Shape c = (Shape) this.plugin.newInstance();
						long x1 = (long) jsonobject.get("pointx");
						long y1 = (long) jsonobject.get("pointy");
						PosPlug.x = (int) (long) x1;
						PosPlug.y = (int) (long) y1;
						PlugBorderColor = new Color((int) (long) jsonobject.get("ColorR"),
								(int) (long) jsonobject.get("ColorG"), (int) (long) jsonobject.get("ColorB"));
						PlugFillColor = new Color((int) (long) jsonobject.get("FillColorR"),
								(int) (long) jsonobject.get("FillColorG"), (int) (long) jsonobject.get("FillColorB"));
						jarPath = (String) jsonobject.get("path");
						Set<String> hi = jsonobject.keySet();
						Object[] hello = hi.toArray();
						for (int j = 0; j < hello.length; j++) {
							if (hello[j].toString().equalsIgnoreCase("ColorR")
									|| hello[j].toString().equalsIgnoreCase("ColorB")
									|| hello[j].toString().equalsIgnoreCase("ColorG")
									|| hello[j].toString().equalsIgnoreCase("FillColorR")
									|| hello[j].toString().equalsIgnoreCase("FillColorG")
									|| hello[j].toString().equalsIgnoreCase("FillColorB")
									|| hello[j].toString().equalsIgnoreCase("path")
									|| hello[j].toString().equalsIgnoreCase("name")
									|| hello[j].toString().equalsIgnoreCase("pointx")
									|| hello[j].toString().equalsIgnoreCase("pointy")) {
								

							} else {
								Double d = (Double) jsonobject.get(hello[j].toString());
								PlugInMap.put(hello[j].toString(), d);
							}

						}
						c.setPosition(PosPlug);
						c.setFillColor(PlugFillColor);
						c.setColor(PlugBorderColor);
						c.setProperties(PlugInMap);
						addShape(c);

					}

					catch (Exception e) {
						e.printStackTrace();
					JOptionPane.showMessageDialog(null, "ahmed");
					}

				}
			}
		} catch (

		Exception e) {
			// TODO: handle exception
		}

	}

	private void saveJSON(String path) {
		try (FileWriter newfile = new FileWriter(path)) {
			JsonArray = new JSONArray();
			shapeArray = getShapes();
			for (int i = 0; i < shapeArray.length; i++) {
				jsonWriter = new JSONObject();
				if (shapeArray[i].getClass().getSimpleName().equalsIgnoreCase("Circle")) {
					jsonWriter.put("name", "Circle");
					jsonWriter.put("pointx", shapeArray[i].getPosition().x);
					jsonWriter.put("pointy", shapeArray[i].getPosition().y);
					jsonWriter.put("Radius", shapeArray[i].getProperties().get("Radius"));
					jsonWriter.put("ColorR", shapeArray[i].getColor().getRed());
					jsonWriter.put("ColorB", shapeArray[i].getColor().getBlue());
					jsonWriter.put("ColorG", shapeArray[i].getColor().getGreen());
					jsonWriter.put("FillColorR", shapeArray[i].getFillColor().getRed());
					jsonWriter.put("FillColorB", shapeArray[i].getFillColor().getBlue());
					jsonWriter.put("FillColorG", shapeArray[i].getFillColor().getGreen());
					JsonArray.add(jsonWriter);

				} else if (shapeArray[i].getClass().getSimpleName().equalsIgnoreCase("Square")) {
					jsonWriter.put("name", "Square");
					jsonWriter.put("pointx", shapeArray[i].getPosition().x);
					jsonWriter.put("pointy", shapeArray[i].getPosition().y);
					jsonWriter.put("width", shapeArray[i].getProperties().get("width"));

					jsonWriter.put("ColorR", shapeArray[i].getColor().getRed());
					jsonWriter.put("ColorB", shapeArray[i].getColor().getBlue());
					jsonWriter.put("ColorG", shapeArray[i].getColor().getGreen());
					jsonWriter.put("FillColorR", shapeArray[i].getFillColor().getRed());
					jsonWriter.put("FillColorB", shapeArray[i].getFillColor().getBlue());
					jsonWriter.put("FillColorG", shapeArray[i].getFillColor().getGreen());
					JsonArray.add(jsonWriter);

				} else if (shapeArray[i].getClass().getSimpleName().equalsIgnoreCase("Rectangle")) {
					jsonWriter.put("name", "Rectangle");
					jsonWriter.put("pointx", shapeArray[i].getPosition().x);
					jsonWriter.put("pointy", shapeArray[i].getPosition().y);
					jsonWriter.put("width", shapeArray[i].getProperties().get("width"));
					jsonWriter.put("height", shapeArray[i].getProperties().get("height"));
					jsonWriter.put("ColorR", shapeArray[i].getColor().getRed());
					jsonWriter.put("ColorB", shapeArray[i].getColor().getBlue());
					jsonWriter.put("ColorG", shapeArray[i].getColor().getGreen());
					jsonWriter.put("FillColorR", shapeArray[i].getFillColor().getRed());
					jsonWriter.put("FillColorB", shapeArray[i].getFillColor().getBlue());
					jsonWriter.put("FillColorG", shapeArray[i].getFillColor().getGreen());
					JsonArray.add(jsonWriter);

				} else if (shapeArray[i].getClass().getSimpleName().equalsIgnoreCase("Ellipse")) {
					jsonWriter.put("name", "Ellipse");
					jsonWriter.put("pointx", shapeArray[i].getPosition().x);
					jsonWriter.put("pointy", shapeArray[i].getPosition().y);
					jsonWriter.put("axisx", shapeArray[i].getProperties().get("axisx"));
					jsonWriter.put("axisy", shapeArray[i].getProperties().get("axisy"));
					jsonWriter.put("ColorR", shapeArray[i].getColor().getRed());
					jsonWriter.put("ColorB", shapeArray[i].getColor().getBlue());
					jsonWriter.put("ColorG", shapeArray[i].getColor().getGreen());
					jsonWriter.put("FillColorR", shapeArray[i].getFillColor().getRed());
					jsonWriter.put("FillColorB", shapeArray[i].getFillColor().getBlue());
					jsonWriter.put("FillColorG", shapeArray[i].getFillColor().getGreen());
					JsonArray.add(jsonWriter);

				} else if (shapeArray[i].getClass().getSimpleName().equalsIgnoreCase("Line")) {
					jsonWriter.put("name", "Line");
					jsonWriter.put("pointx", shapeArray[i].getPosition().x);
					jsonWriter.put("pointy", shapeArray[i].getPosition().y);
					jsonWriter.put("xline", shapeArray[i].getProperties().get("xline"));
					jsonWriter.put("yline", shapeArray[i].getProperties().get("yline"));
					jsonWriter.put("ColorR", shapeArray[i].getColor().getRed());
					jsonWriter.put("ColorB", shapeArray[i].getColor().getBlue());
					jsonWriter.put("ColorG", shapeArray[i].getColor().getGreen());
					JsonArray.add(jsonWriter);

				} else if (shapeArray[i].getClass().getSimpleName().equalsIgnoreCase("Triangle")) {
					jsonWriter.put("name", "Triangle");
					jsonWriter.put("pointx", shapeArray[i].getPosition().x);
					jsonWriter.put("pointy", shapeArray[i].getPosition().y);
					jsonWriter.put("x1", shapeArray[i].getProperties().get("x1"));
					jsonWriter.put("y1", shapeArray[i].getProperties().get("y1"));
					jsonWriter.put("x2", shapeArray[i].getProperties().get("x2"));
					jsonWriter.put("y2", shapeArray[i].getProperties().get("y2"));
					jsonWriter.put("x3", shapeArray[i].getProperties().get("x3"));
					jsonWriter.put("y3", shapeArray[i].getProperties().get("y3"));
					jsonWriter.put("ColorR", shapeArray[i].getColor().getRed());
					jsonWriter.put("ColorB", shapeArray[i].getColor().getBlue());
					jsonWriter.put("ColorG", shapeArray[i].getColor().getGreen());
					jsonWriter.put("FillColorR", shapeArray[i].getFillColor().getRed());
					jsonWriter.put("FillColorB", shapeArray[i].getFillColor().getBlue());
					jsonWriter.put("FillColorG", shapeArray[i].getFillColor().getGreen());
					JsonArray.add(jsonWriter);
				} else if (shapeArray[i] == null) {
					jsonWriter.put("name", "Null");
					jsonWriter.put("pointx", null);
					jsonWriter.put("pointy", null);
					jsonWriter.put("x1", null);
					jsonWriter.put("y1", null);
					jsonWriter.put("x2", null);
					jsonWriter.put("y2", null);
					jsonWriter.put("x3", null);
					jsonWriter.put("y3", null);
					jsonWriter.put("ColorR", null);
					jsonWriter.put("ColorB", null);
					jsonWriter.put("ColorG", null);
					jsonWriter.put("FillColorR", null);
					jsonWriter.put("FillColorB", null);
					jsonWriter.put("FillColorG", null);
					JsonArray.add(jsonWriter);
				} else {
					jsonWriter.put("name", shapeArray[i].getClass().getSimpleName());
					jsonWriter.put("pointx", shapeArray[i].getPosition().x);
					jsonWriter.put("pointy", shapeArray[i].getPosition().y);
					jsonWriter.put("ColorR", shapeArray[i].getColor().getRed());
					jsonWriter.put("ColorB", shapeArray[i].getColor().getBlue());
					jsonWriter.put("ColorG", shapeArray[i].getColor().getGreen());
					jsonWriter.put("FillColorR", shapeArray[i].getFillColor().getRed());
					jsonWriter.put("FillColorB", shapeArray[i].getFillColor().getBlue());
					jsonWriter.put("FillColorG", shapeArray[i].getFillColor().getGreen());
					for (Map.Entry<String, Double> entry : shapeArray[i].getProperties().entrySet()) {
						jsonWriter.put(entry.getKey(), entry.getValue());
					}
					jsonWriter.put("path", jarPath);
					JsonArray.add(jsonWriter);
				}

			}
			jsonTotal.put("Shapes", JsonArray);
			newfile.write(jsonTotal.toJSONString());
			newfile.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void addPlug(Constructor p) {
		plugin = p;
	}

}
