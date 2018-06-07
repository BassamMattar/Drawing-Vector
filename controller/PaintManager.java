package controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.cs.Circle;
import eg.edu.alexu.csd.oop.draw.cs.DrawingEngineImp;
import eg.edu.alexu.csd.oop.draw.cs.Triangle;
import eg.edu.alexu.csd.oop.draw.cs.Ellipse;
import eg.edu.alexu.csd.oop.draw.cs.Line;
import eg.edu.alexu.csd.oop.draw.cs.Rectangle;
import eg.edu.alexu.csd.oop.draw.cs.ShapeImp;
import eg.edu.alexu.csd.oop.draw.cs.Square;
import gui.DrawBrush;
import gui.Paint;
import javafx.scene.control.ComboBox;

public class PaintManager {
	private static Point[] triArray = new Point[3];
	private static int triCounter = 0;
	private static Point[] cirArray = new Point[2];
	private static int cirCounter = 0;
	private static Point[] elliArray = new Point[2];
	private static int elliCounter = 0;
	private static Point[] recArray = new Point[2];
	private static int recCounter = 0;
	private static Point[] sqArray = new Point[2];
	private static int sqCounter = 0;
	private static Point[] lineArray = new Point[2];
	private static int lineCounter = 0;
	private static DrawingEngineImp DRAW_ENGINE = new DrawingEngineImp();
	private static Shape SHAPE_TO_BE_EXTENDE;
	private static Color selectedColor = Color.WHITE;
	private static Color Bordered= Color.BLACK;
	private static JFileChooser fileChooser = new JFileChooser();
	private static JFileChooser plugChooser = new JFileChooser();

	private static boolean RESIZE = false;
	private static Paint PAINT = new Paint();

	public static void main(String[] args) {
		initializeListners();
		initializeButtons();
		PAINT.main(args);
	}

	private static void initializeButtons() {

		PAINT.FILLCOLOR.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JColorChooser FillColor = new JColorChooser();
				Color x = FillColor.showDialog(null, "Choose Fill Color ", Color.BLACK);
				selectedColor = x;
				PAINT.FILLCOLOR.setBackground(x);
			}
		});
		PAINT.BORDERCOLOR.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JColorChooser FillColor = new JColorChooser();
				Color x = FillColor.showDialog(null, "Choose Border Color ", Color.BLACK);
				Bordered = x;
				PAINT.BORDERCOLOR.setBackground(x);
			}
		});
		PAINT.SET_TRIANGLE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				triCounter = 0;
				PAINT.DRAW_BRUSH.addMouseMotionListener(PAINT.TRIANGLE_ADABTER);
				PAINT.DRAW_BRUSH.addMouseListener(PAINT.TRIANGLE_ADABTER);
			}
		});
		PAINT.SET_CIRCLE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cirCounter = 0;
				PAINT.DRAW_BRUSH.addMouseListener(PAINT.CIRCLE_ADABTER);
				PAINT.DRAW_BRUSH.addMouseMotionListener(PAINT.CIRCLE_ADABTER);

			}
		});
		PAINT.SET_ELLIPSE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				elliCounter = 0;
				PAINT.DRAW_BRUSH.addMouseListener(PAINT.ELLIPSE_ADABTER);
				PAINT.DRAW_BRUSH.addMouseMotionListener(PAINT.ELLIPSE_ADABTER);

			}
		});
		PAINT.SET_RECTANGLE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				recCounter = 0;
				PAINT.DRAW_BRUSH.addMouseListener(PAINT.REC_ADABTER);
				PAINT.DRAW_BRUSH.addMouseMotionListener(PAINT.REC_ADABTER);
				// resetAdapters();

			}
		});
		PAINT.SET_SQUARE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sqCounter = 0;
				PAINT.DRAW_BRUSH.addMouseListener(PAINT.SQR_ADABTER);
				PAINT.DRAW_BRUSH.addMouseMotionListener(PAINT.SQR_ADABTER);
			}
		});
		PAINT.SET_LINE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lineCounter = 0;
				PAINT.DRAW_BRUSH.addMouseListener(PAINT.LINE_ADABTER);
				PAINT.DRAW_BRUSH.addMouseMotionListener(PAINT.LINE_ADABTER);
			}
		});
		PAINT.UNDO.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DRAW_ENGINE.undo();
				DRAW_ENGINE.refresh(PAINT.DRAW_BRUSH.getGraphics());
				restShapeComboBox();
			}
		});

		PAINT.REDO.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DRAW_ENGINE.redo();
				DRAW_ENGINE.refresh(PAINT.DRAW_BRUSH.getGraphics());
				restShapeComboBox();
			}
		});
		PAINT.BTN_SAVE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fileChooser.setDialogTitle("Save a Draw ");
				int userSelection = fileChooser.showSaveDialog(PAINT.FRAME);
				if (userSelection == JFileChooser.APPROVE_OPTION) {
					File fileToSave = fileChooser.getSelectedFile();
					DRAW_ENGINE.save(fileToSave.getAbsolutePath());
				} else {
					JOptionPane.showMessageDialog(null, "No Path");
				}
			}
		});

		PAINT.BTN_LOAD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					fileChooser.setDialogTitle("Load a Draw ");
					int userSelection = fileChooser.showSaveDialog(PAINT.FRAME);
					if (userSelection == JFileChooser.APPROVE_OPTION) {
						File fileToLoad = fileChooser.getSelectedFile();
						DRAW_ENGINE.load(fileToLoad.getAbsolutePath());
						DRAW_ENGINE.refresh(Paint.DRAW_BRUSH.getGraphics());
						restShapeComboBox();
					} else {
						JOptionPane.showMessageDialog(null, "No File Found");
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
			}
			
		});
		PAINT.MOVE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PAINT.DRAW_BRUSH.addMouseListener(PAINT.MOVE_ADABTER);
				PAINT.DRAW_BRUSH.addMouseMotionListener(PAINT.MOVE_ADABTER);

			}
		});
		PAINT.RESIZE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				RESIZE = true;
				try {
					ShapeImp x = (ShapeImp) (DRAW_ENGINE.getShapes()[PAINT.SHAPES.getSelectedIndex()]);
					x.resize(PAINT.DRAW_BRUSH);
				} catch (Exception e) {
					try {
						Shape plugShape = (Shape) DRAW_ENGINE.getShapes()[PAINT.SHAPES.getSelectedIndex()].clone();
						for (String key : plugShape.getProperties().keySet()) {
							String y = JOptionPane.showInputDialog("Value of " + key + ": ");
							double value = Double.parseDouble(y);
							plugShape.getProperties().put(key, value);
						}
						DRAW_ENGINE.updateShape(DRAW_ENGINE.getShapes()[PAINT.SHAPES.getSelectedIndex()], plugShape);
						DRAW_ENGINE.refresh(PAINT.DRAW_BRUSH.getGraphics());
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, "Un Supported Operation");
					}
				}
			}
		});
		PAINT.REMOVE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Shape[] shapes = DRAW_ENGINE.getShapes();
				Shape shapeToBeRemoved = shapes[PAINT.SHAPES.getSelectedIndex()];
				PAINT.SHAPES.removeItemAt(PAINT.SHAPES.getSelectedIndex());
				DRAW_ENGINE.removeShape(shapeToBeRemoved);
				restShapeComboBox();
				DRAW_ENGINE.refresh(PAINT.DRAW_BRUSH.getGraphics());
			}
		});
		PAINT.PAINT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Shape[] shapes = DRAW_ENGINE.getShapes();
				Shape shapeToBePainted;
				try {
					shapeToBePainted = (Shape) shapes[PAINT.SHAPES.getSelectedIndex()].clone();
					shapeToBePainted.setColor(Bordered);
					shapeToBePainted.setFillColor(selectedColor);
					Shape oldShape = shapes[PAINT.SHAPES.getSelectedIndex()];
					DRAW_ENGINE.updateShape(oldShape, shapeToBePainted);
					DRAW_ENGINE.refresh(PAINT.DRAW_BRUSH.getGraphics());

				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			}
		});
		PAINT.COPY.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PAINT.DRAW_BRUSH.addMouseListener(PAINT.COPY_ADABTER);
				PAINT.DRAW_BRUSH.addMouseMotionListener(PAINT.COPY_ADABTER);

			}
		});
		PAINT.ADD_PLUGIN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int userselection = plugChooser.showSaveDialog(PAINT.FRAME);
				if (userselection == JFileChooser.APPROVE_OPTION) {
					String path = plugChooser.getSelectedFile().getAbsolutePath();
					DRAW_ENGINE.jarPath =path;
					try {
						JarInputStream jarFile = new JarInputStream(new FileInputStream(path));
						File myJar = new File(path);
						URL url = myJar.toURI().toURL();
						Class[] parameters = new Class[] { URL.class };
						URLClassLoader sysLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
						Class sysClass = URLClassLoader.class;
						
						//DRAW_ENGINE.supportedClasses.add(sysClass);
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
								String name = jarEntry.getName().replaceAll("/", "\\.").replace(".class", "");
								JOptionPane.showMessageDialog(null, name);
								Constructor cs = ClassLoader.getSystemClassLoader().loadClass(name).getConstructor();
								DRAW_ENGINE.addPlug(cs);
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(null, "ahmed");
					}
				}
			}
		});
		PAINT.Draw_PLUGIN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Shape plugShape = (Shape) DRAW_ENGINE.plugin.newInstance();
					plugShape.setColor(Bordered);
					plugShape.setFillColor(selectedColor);
					String tempX = JOptionPane.showInputDialog("Pos X: ");
					String tempY = JOptionPane.showInputDialog("Pos Y: ");
					Point pos = new Point(Integer.parseInt(tempX), Integer.parseInt(tempY));
					plugShape.setPosition(pos);
					for (String key : plugShape.getProperties().keySet()) {
						String y = JOptionPane.showInputDialog("Value of " + key + ": ");
						double value = Double.parseDouble(y);
						plugShape.getProperties().put(key, value);
					}
					DRAW_ENGINE.addShape(plugShape);
					DRAW_ENGINE.refresh(PAINT.DRAW_BRUSH.getGraphics());
					PAINT.SHAPES.addItem((plugShape.getClass().getSimpleName()
							.concat(String.valueOf(DRAW_ENGINE.getShapes().length-1))));
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	private static void initializeListners() {
		PAINT.TRIANGLE_ADABTER = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (triCounter < 3) {
					// PAINT.textField.setText(": here" + arg0.getPoint());
					triArray[triCounter] = arg0.getPoint();
					if (triCounter == 2) {
						SHAPE_TO_BE_EXTENDE = new Triangle();
						PAINT.SHAPES.addItem((SHAPE_TO_BE_EXTENDE.getClass().getSimpleName()
								.concat(String.valueOf(DRAW_ENGINE.getShapes().length))));
						((Triangle) SHAPE_TO_BE_EXTENDE).setPosition(triArray[0]);
						((Triangle) SHAPE_TO_BE_EXTENDE).properties.put("x1", triArray[0].getX());
						((Triangle) SHAPE_TO_BE_EXTENDE).properties.put("x2", triArray[1].getX());
						((Triangle) SHAPE_TO_BE_EXTENDE).properties.put("x3", triArray[2].getX());
						((Triangle) SHAPE_TO_BE_EXTENDE).properties.put("y1", triArray[0].getY());
						((Triangle) SHAPE_TO_BE_EXTENDE).properties.put("y2", triArray[1].getY());
						((Triangle) SHAPE_TO_BE_EXTENDE).properties.put("y3", triArray[2].getY());
						((Triangle) SHAPE_TO_BE_EXTENDE).setColor(Bordered);
						((Triangle) SHAPE_TO_BE_EXTENDE).setFillColor(selectedColor);// need
						// to
						// change
						if (!RESIZE) {
							DRAW_ENGINE.addShape((Triangle) SHAPE_TO_BE_EXTENDE);
						} else {
							Shape oldShape = DRAW_ENGINE.getShapes()[PAINT.SHAPES.getSelectedIndex()];
							DRAW_ENGINE.updateShape(oldShape, (Triangle) SHAPE_TO_BE_EXTENDE);
							RESIZE = false;
						}
						DRAW_ENGINE.refresh(PAINT.DRAW_BRUSH.getGraphics());
						triCounter = 0;
						PAINT.DRAW_BRUSH.removeMouseListener(this);
						PAINT.DRAW_BRUSH.removeMouseMotionListener(this);
						return;
					}
					triCounter++;
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				if (triCounter == 1) {
					// PAINT.textField.setText(": " + e.getPoint());
					Line visualLine = new Line();
					visualLine.properties.put("xline", e.getPoint().getX());
					visualLine.properties.put("yline", e.getPoint().getY());
					visualLine.setColor(Bordered);
					visualLine.setPosition(triArray[0]);
					DRAW_ENGINE.addVisuals(visualLine);
					DRAW_ENGINE.refresh(PAINT.DRAW_BRUSH.getGraphics());
					DRAW_ENGINE.removeVisual();
				} else if (triCounter == 2) {
					Triangle visualTriangle = new Triangle();
					SHAPE_TO_BE_EXTENDE = new Triangle();
					visualTriangle.setPosition(triArray[0]);
					visualTriangle.properties.put("x1", triArray[0].getX());
					visualTriangle.properties.put("x2", triArray[1].getX());
					visualTriangle.properties.put("x3", (double) e.getX());
					visualTriangle.properties.put("y1", triArray[0].getY());
					visualTriangle.properties.put("y2", triArray[1].getY());
					visualTriangle.properties.put("y3", (double) e.getY());
					visualTriangle.setColor(Bordered);
					visualTriangle.setFillColor(selectedColor);
					DRAW_ENGINE.addVisuals(visualTriangle);
					DRAW_ENGINE.refresh(PAINT.DRAW_BRUSH.getGraphics());
					DRAW_ENGINE.removeVisual();
				}

			}
		};
		PAINT.CIRCLE_ADABTER = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (cirCounter < 2) {
					// PAINT.textField.setText(": " + arg0.getPoint());
					cirArray[cirCounter] = arg0.getPoint();
					if (cirCounter == 1) {
						double radius = (Math.pow(Math.pow(Math.abs(cirArray[0].x - cirArray[1].getX()), 2)
								+ Math.pow(Math.abs(cirArray[0].getY() - cirArray[1].getY()), 2), .5));
						SHAPE_TO_BE_EXTENDE = new Circle();
						Point center = new Point(cirArray[0].x - (int) radius, cirArray[0].y - (int) radius);
						PAINT.SHAPES.addItem((SHAPE_TO_BE_EXTENDE.getClass().getSimpleName()
								.concat(String.valueOf(DRAW_ENGINE.getShapes().length))));
						((Circle) SHAPE_TO_BE_EXTENDE).setPosition(center);
						((Circle) SHAPE_TO_BE_EXTENDE).properties.put(
								(String) ((Circle) SHAPE_TO_BE_EXTENDE).properties.keySet().toArray()[0], radius * 2);
						((Circle) SHAPE_TO_BE_EXTENDE).setColor(Bordered);
						((Circle) SHAPE_TO_BE_EXTENDE).setFillColor(selectedColor);
						if (!RESIZE) {
							DRAW_ENGINE.addShape((Circle) SHAPE_TO_BE_EXTENDE);
						} else {
							Shape oldShape = DRAW_ENGINE.getShapes()[PAINT.SHAPES.getSelectedIndex()];
							DRAW_ENGINE.updateShape(oldShape, (Circle) SHAPE_TO_BE_EXTENDE);
							RESIZE = false;
						}
						DRAW_ENGINE.refresh(PAINT.DRAW_BRUSH.getGraphics());
						cirCounter = 0;
						PAINT.DRAW_BRUSH.removeMouseListener(this);
						PAINT.DRAW_BRUSH.removeMouseMotionListener(this);
						return;
					}
					cirCounter++;
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				if (cirCounter == 1) {
					// PAINT.textField.setText(": " + e.getPoint());
					double radius = (Math.pow(Math.pow(Math.abs(cirArray[0].x - e.getX()), 2)
							+ Math.pow(Math.abs(cirArray[0].getY() - e.getY()), 2), .5));
					Circle visualCircle = new Circle();
					((Circle) visualCircle).properties
							.put((String) ((Circle) visualCircle).properties.keySet().toArray()[0], radius * 2);
					Point center = new Point(cirArray[0].x - (int) radius, cirArray[0].y - (int) radius);
					visualCircle.setPosition(center);
					visualCircle.setColor(Bordered);
					visualCircle.setFillColor(selectedColor);
					DRAW_ENGINE.addVisuals(visualCircle);
					DRAW_ENGINE.refresh(PAINT.DRAW_BRUSH.getGraphics());
					DRAW_ENGINE.removeVisual();
				}
			}
		};
		PAINT.ELLIPSE_ADABTER = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (elliCounter < 2) {
					// PAINT.textField.setText(": " + arg0.getPoint());
					elliArray[elliCounter] = arg0.getPoint();
					if (elliCounter == 1) {
						Point pivot = new Point();
						pivot.x = (int) Math.min(elliArray[0].getX(), elliArray[1].getX());
						pivot.y = (int) Math.min(elliArray[0].getY(), elliArray[1].getY());
						SHAPE_TO_BE_EXTENDE = new Ellipse();
						PAINT.SHAPES.addItem((SHAPE_TO_BE_EXTENDE.getClass().getSimpleName()
								.concat(String.valueOf(DRAW_ENGINE.getShapes().length))));
						((Ellipse) SHAPE_TO_BE_EXTENDE).properties.put("axisx",
								Math.abs(elliArray[0].getX() - elliArray[1].getX()));
						((Ellipse) SHAPE_TO_BE_EXTENDE).properties.put("axisy",
								Math.abs(elliArray[0].getY() - elliArray[1].getY()));
						((Ellipse) SHAPE_TO_BE_EXTENDE).setPosition(pivot);
						((Ellipse) SHAPE_TO_BE_EXTENDE).setColor(Bordered);
						((Ellipse) SHAPE_TO_BE_EXTENDE).setFillColor(selectedColor);
						if (!RESIZE) {
							DRAW_ENGINE.addShape((Ellipse) SHAPE_TO_BE_EXTENDE);
						} else {
							Shape oldShape = DRAW_ENGINE.getShapes()[PAINT.SHAPES.getSelectedIndex()];
							DRAW_ENGINE.updateShape(oldShape, (Ellipse) SHAPE_TO_BE_EXTENDE);
							RESIZE = false;
						}
						DRAW_ENGINE.refresh(PAINT.DRAW_BRUSH.getGraphics());
						elliCounter = 0;
						PAINT.DRAW_BRUSH.removeMouseListener(this);
						PAINT.DRAW_BRUSH.removeMouseMotionListener(this);
						return;
					}
					elliCounter++;
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				// PAINT.textField.setText(": " + e.getPoint());
				if (elliCounter == 1) {
					Point pivot = new Point();
					pivot.x = (int) Math.min(elliArray[0].getX(), e.getX());
					pivot.y = (int) Math.min(elliArray[0].getY(), e.getY());
					Ellipse visualEllipse = new Ellipse();
					visualEllipse.properties.put("axisx", Math.abs(elliArray[0].getX() - e.getX()));
					visualEllipse.properties.put("axisy", Math.abs(elliArray[0].getY() - e.getY()));
					visualEllipse.setPosition(pivot);
					visualEllipse.setFillColor(selectedColor);
					visualEllipse.setColor(Bordered);
					DRAW_ENGINE.addVisuals(visualEllipse);
					DRAW_ENGINE.refresh(PAINT.DRAW_BRUSH.getGraphics());
					DRAW_ENGINE.removeVisual();

				}
			}

		};
		PAINT.REC_ADABTER = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (recCounter < 2) {
					// PAINT.textField.setText(": " + arg0.getPoint());
					recArray[recCounter] = arg0.getPoint();
					if (recCounter == 1) {
						Point pivot = new Point();
						pivot.x = (int) Math.min(recArray[0].getX(), recArray[1].getX());
						pivot.y = (int) Math.min(recArray[0].getY(), recArray[1].getY());
						SHAPE_TO_BE_EXTENDE = new Rectangle();
						PAINT.SHAPES.addItem((SHAPE_TO_BE_EXTENDE.getClass().getSimpleName()
								.concat(String.valueOf(DRAW_ENGINE.getShapes().length))));
						((Rectangle) SHAPE_TO_BE_EXTENDE).properties.put("width",
								Math.abs(recArray[0].getX() - recArray[1].getX()));
						((Rectangle) SHAPE_TO_BE_EXTENDE).properties.put("height",
								Math.abs(recArray[0].getY() - recArray[1].getY()));
						((Rectangle) SHAPE_TO_BE_EXTENDE).setPosition(pivot);
						((Rectangle) SHAPE_TO_BE_EXTENDE).setColor(Bordered);
						((Rectangle) SHAPE_TO_BE_EXTENDE).setFillColor(selectedColor);
						if (!RESIZE) {
							DRAW_ENGINE.addShape((Rectangle) SHAPE_TO_BE_EXTENDE);
						} else {
							Shape oldShape = DRAW_ENGINE.getShapes()[PAINT.SHAPES.getSelectedIndex()];
							DRAW_ENGINE.updateShape(oldShape, (Rectangle) SHAPE_TO_BE_EXTENDE);
							RESIZE = false;
						}
						DRAW_ENGINE.refresh(PAINT.DRAW_BRUSH.getGraphics());
						recCounter = 0;
						PAINT.DRAW_BRUSH.removeMouseListener(this);
						PAINT.DRAW_BRUSH.removeMouseMotionListener(this);
						return;
					}
					recCounter++;
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				// PAINT.textField.setText(": " + e.getPoint());
				if (recCounter == 1) {
					Point pivot = new Point();
					pivot.x = (int) Math.min(recArray[0].getX(), e.getX());
					pivot.y = (int) Math.min(recArray[0].getY(), e.getY());
					Rectangle visualRec = new Rectangle();
					visualRec.properties.put("width", Math.abs(recArray[0].getX() - e.getX()));
					visualRec.properties.put("height", Math.abs(recArray[0].getY() - e.getY()));
					visualRec.setPosition(pivot);
					visualRec.setColor(Bordered);
					visualRec.setFillColor(selectedColor);
					DRAW_ENGINE.addVisuals(visualRec);
					DRAW_ENGINE.refresh(PAINT.DRAW_BRUSH.getGraphics());
					DRAW_ENGINE.removeVisual();

				}
			}

		};
		PAINT.SQR_ADABTER = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (sqCounter < 2) {
					// PAINT.textField.setText(": " + arg0.getPoint());
					sqArray[sqCounter] = arg0.getPoint();
					if (sqCounter == 1) {
						Point pivot = new Point();
						pivot.x = (int) Math.min(sqArray[0].getX(), sqArray[1].getX());
						pivot.y = (int) sqArray[0].getY();
						SHAPE_TO_BE_EXTENDE = new Square();
						PAINT.SHAPES.addItem((SHAPE_TO_BE_EXTENDE.getClass().getSimpleName()
								.concat(String.valueOf(DRAW_ENGINE.getShapes().length))));
						((Square) SHAPE_TO_BE_EXTENDE).properties.put("width",
								Math.abs(sqArray[0].getX() - sqArray[1].getX()));
						((Square) SHAPE_TO_BE_EXTENDE).setPosition(pivot);
						((Square) SHAPE_TO_BE_EXTENDE).setColor(Bordered);
						((Square) SHAPE_TO_BE_EXTENDE).setFillColor(selectedColor);
						if (!RESIZE) {
							DRAW_ENGINE.addShape((Square) SHAPE_TO_BE_EXTENDE);
						} else {
							Shape oldShape = DRAW_ENGINE.getShapes()[PAINT.SHAPES.getSelectedIndex()];
							DRAW_ENGINE.updateShape(oldShape, (Square) SHAPE_TO_BE_EXTENDE);
							RESIZE = false;
						}
						DRAW_ENGINE.refresh(PAINT.DRAW_BRUSH.getGraphics());
						sqCounter = 0;
						PAINT.DRAW_BRUSH.removeMouseListener(this);
						PAINT.DRAW_BRUSH.removeMouseMotionListener(this);
						return;
					}
					sqCounter++;
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				// PAINT.textField.setText(": " + e.getPoint());
				if (sqCounter == 1) {
					Point pivot = new Point();
					pivot.x = (int) Math.min(sqArray[0].getX(), e.getX());
					pivot.y = (int) sqArray[0].getY();
					Square visualSqr = new Square();
					visualSqr.properties.put("width", Math.abs(sqArray[0].getX() - e.getX()));
					visualSqr.setPosition(pivot);
					visualSqr.setColor(Bordered);
					visualSqr.setFillColor(selectedColor);
					DRAW_ENGINE.addVisuals(visualSqr);
					DRAW_ENGINE.refresh(PAINT.DRAW_BRUSH.getGraphics());
					DRAW_ENGINE.removeVisual();

				}
			}

		};
		PAINT.LINE_ADABTER = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (lineCounter < 2) {
					// PAINT.textField.setText(": " + arg0.getPoint());
					lineArray[lineCounter] = arg0.getPoint();
					if (lineCounter == 1) {
						SHAPE_TO_BE_EXTENDE = new Line();
						PAINT.SHAPES.addItem((SHAPE_TO_BE_EXTENDE.getClass().getSimpleName()
								.concat(String.valueOf(DRAW_ENGINE.getShapes().length))));
						((Line) SHAPE_TO_BE_EXTENDE).properties.put("xline", lineArray[1].getX());
						((Line) SHAPE_TO_BE_EXTENDE).properties.put("yline", lineArray[1].getY());
						((Line) SHAPE_TO_BE_EXTENDE).setPosition(lineArray[0]);
						((Line) SHAPE_TO_BE_EXTENDE).setColor(Bordered);
						if (!RESIZE) {
							DRAW_ENGINE.addShape((Line) SHAPE_TO_BE_EXTENDE);
						} else {
							Shape oldShape = DRAW_ENGINE.getShapes()[PAINT.SHAPES.getSelectedIndex()];
							DRAW_ENGINE.updateShape(oldShape, (Line) SHAPE_TO_BE_EXTENDE);
							RESIZE = false;
						}
						DRAW_ENGINE.refresh(PAINT.DRAW_BRUSH.getGraphics());
						lineCounter = 0;
						PAINT.DRAW_BRUSH.removeMouseListener(this);
						PAINT.DRAW_BRUSH.removeMouseMotionListener(this);
						return;
					}
					lineCounter++;
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				if (lineCounter == 1) {
					// PAINT.textField.setText(": " + e.getPoint());
					Line visualLine = new Line();
					visualLine.properties.put("xline", (double) e.getX());
					visualLine.properties.put("yline", (double) e.getY());
					visualLine.setPosition(lineArray[0]);
					visualLine.setColor(Bordered);
					DRAW_ENGINE.addVisuals(visualLine);
					DRAW_ENGINE.refresh(PAINT.DRAW_BRUSH.getGraphics());
					DRAW_ENGINE.removeVisual();
				}
			}
		};
		PAINT.MOVE_ADABTER = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// PAINT.textField.setText(": " + arg0.getPoint());
				Shape[] shapes = DRAW_ENGINE.getShapes();
				ShapeImp shapeToEdit;
				try {
					shapeToEdit = (ShapeImp) (shapes[PAINT.SHAPES.getSelectedIndex()].clone());
					shapeToEdit.move(arg0.getPoint());
					DRAW_ENGINE.updateShape(shapes[PAINT.SHAPES.getSelectedIndex()], shapeToEdit);
					DRAW_ENGINE.refresh(PAINT.DRAW_BRUSH.getGraphics());
				} catch (Exception e) {
					try {
						Shape plugShape = (Shape) shapes[PAINT.SHAPES.getSelectedIndex()].clone();
						plugShape.setPosition(arg0.getPoint());
						DRAW_ENGINE.updateShape(shapes[PAINT.SHAPES.getSelectedIndex()], plugShape);
						DRAW_ENGINE.refresh(PAINT.DRAW_BRUSH.getGraphics());
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, "Un Supported Operation");
					}
				}
				PAINT.DRAW_BRUSH.removeMouseListener(this);
				PAINT.DRAW_BRUSH.removeMouseMotionListener(this);
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				// PAINT.textField.setText("MOVE ADABTER " + e.getPoint());
				Shape[] shapes = DRAW_ENGINE.getShapes();
				ShapeImp visualShape;
				try {
					visualShape = (ShapeImp) (shapes[PAINT.SHAPES.getSelectedIndex()].clone());
					visualShape.move(e.getPoint());
					visualShape.setColor(Color.blue.brighter().brighter().brighter());
					DRAW_ENGINE.addVisuals(visualShape);
					DRAW_ENGINE.refresh(PAINT.DRAW_BRUSH.getGraphics());
					DRAW_ENGINE.removeVisual();
				} catch (Exception exc) {
					try {
						Shape plugShape = (Shape) shapes[PAINT.SHAPES.getSelectedIndex()].clone();
						plugShape.setColor(Color.blue.brighter().brighter().brighter());
						plugShape.setPosition(e.getPoint());
						DRAW_ENGINE.addVisuals(plugShape);
						DRAW_ENGINE.refresh(PAINT.DRAW_BRUSH.getGraphics());
						DRAW_ENGINE.removeVisual();
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, "Un Supported Operation");
					}
				}
			}
		};
		PAINT.COPY_ADABTER = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// PAINT.textField.setText(": " + arg0.getPoint());
				Shape[] shapes = DRAW_ENGINE.getShapes();
				ShapeImp shapeToEdit;
				try {
					shapeToEdit = (ShapeImp) (shapes[PAINT.SHAPES.getSelectedIndex()].clone());
					shapeToEdit.move(arg0.getPoint());
					PAINT.SHAPES.addItem((shapeToEdit.getClass().getSimpleName()
							.concat(String.valueOf(DRAW_ENGINE.getShapes().length))));
					DRAW_ENGINE.addShape(shapeToEdit);
					DRAW_ENGINE.refresh(PAINT.DRAW_BRUSH.getGraphics());
				} catch (Exception e) {
					try {
						Shape plugShape = (Shape) shapes[PAINT.SHAPES.getSelectedIndex()].clone();
						plugShape.setPosition(arg0.getPoint());
						PAINT.SHAPES.addItem((plugShape.getClass().getSimpleName()
								.concat(String.valueOf(DRAW_ENGINE.getShapes().length))));
						DRAW_ENGINE.addShape(plugShape);
						DRAW_ENGINE.refresh(PAINT.DRAW_BRUSH.getGraphics());
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, "Un Supported Operation");
					}
				}
				PAINT.DRAW_BRUSH.removeMouseListener(this);
				PAINT.DRAW_BRUSH.removeMouseMotionListener(this);
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				// PAINT.textField.setText("COPY ADABTER " + e.getPoint());
				Shape[] shapes = DRAW_ENGINE.getShapes();
				ShapeImp visualShape;
				try {
					visualShape = (ShapeImp) (shapes[PAINT.SHAPES.getSelectedIndex()].clone());
					visualShape.move(e.getPoint());
					visualShape.setColor(Bordered);
					DRAW_ENGINE.addVisuals(visualShape);
					DRAW_ENGINE.refresh(PAINT.DRAW_BRUSH.getGraphics());
					DRAW_ENGINE.removeVisual();
				} catch (Exception exc) {
					try {
						Shape plugShape = (Shape) shapes[PAINT.SHAPES.getSelectedIndex()].clone();
						plugShape.setColor(Color.blue.brighter().brighter().brighter());
						plugShape.setPosition(e.getPoint());
						DRAW_ENGINE.addVisuals(plugShape);
						DRAW_ENGINE.refresh(PAINT.DRAW_BRUSH.getGraphics());
						DRAW_ENGINE.removeVisual();
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, "Un Supported Operation");
					}
				}
			}
		};
	}

	public static void setTri(Point p, int index) {
		triCounter = index;
		triArray[triCounter - 1] = p;
	}

	public static void setRec(Point p, int index) {
		recCounter = index;
		recArray[recCounter - 1] = p;
	}

	public static void setCirc(Point p, int index) {
		cirCounter = index;
		cirArray[cirCounter - 1] = p;
	}

	public static void setLine(Point p, int index) {
		lineCounter = index;
		lineArray[lineCounter - 1] = p;
	}

	public static void setEllipse(Point p, int index) {
		elliCounter = index;
		elliArray[elliCounter - 1] = p;
	}

	public static void setSqr(Point p, int index) {
		sqCounter = index;
		sqArray[sqCounter - 1] = p;
	}

	public static void restShapeComboBox() {

		PAINT.SHAPES.removeAllItems();
		Shape[] allShapes = DRAW_ENGINE.getShapes();
		for (int i = 0; i < allShapes.length; i++) {
			Shape item = allShapes[i];
			PAINT.SHAPES.addItem(item.getClass().getSimpleName().concat(String.valueOf(i)));
		}
	}
	public void resetAdapters(){
		PAINT.DRAW_BRUSH.removeMouseListener((MouseListener) this);
		PAINT.DRAW_BRUSH.removeMouseMotionListener((MouseMotionListener) this);
	}
	

}
