
package gui;

import java.awt.Color;
import java.awt.EventQueue;

import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.ComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.cs.Circle;
import eg.edu.alexu.csd.oop.draw.cs.DrawingEngineImp;
import eg.edu.alexu.csd.oop.draw.cs.Triangle;

import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JColorChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.Icon;
import java.awt.Font;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.JSeparator;

public class Paint {

	public static JFrame FRAME;
	public static DrawBrush DRAW_BRUSH = new DrawBrush();
	public static JButton SET_TRIANGLE = new JButton("Triangle");
	public static MouseAdapter TRIANGLE_ADABTER;

	public static JButton SET_CIRCLE = new JButton("Circle");
	public static MouseAdapter CIRCLE_ADABTER;

	
	public static JButton SET_ELLIPSE = new JButton("Ellipse");
	public static MouseAdapter ELLIPSE_ADABTER;

	public static JButton SET_LINE = new JButton("Line");
	public static MouseAdapter LINE_ADABTER;

	public static JButton SET_RECTANGLE = new JButton("Rec");
	public static MouseAdapter REC_ADABTER;

	public static JButton SET_SQUARE = new JButton("Sqr");
	public static MouseAdapter SQR_ADABTER;

	public static Color[] COLORS = { Color.black, Color.red, Color.blue, Color.green, Color.black, Color.cyan,
			Color.darkGray, Color.magenta, Color.orange };
	/*public static JComboBox COLORBOX = new JComboBox(COLORS);
	public static JComboBox FILLCOLORBOX = new JComboBox(COLORS);*/
	public static JComboBox<String> SHAPES = new JComboBox<>();

	public static JButton REDO = new JButton(
			new ImageIcon("D:\\BASSAM\\1\\Drive of years\\Second year first semester\\image\\redo32.png"));
	public static JButton UNDO = new JButton(
			new ImageIcon("D:/BASSAM/1/Drive of years/Second year first semester/image/undo32.png"));

	public static JButton MOVE = new JButton((Icon) null);
	public static MouseAdapter MOVE_ADABTER;

	public static JButton RESIZE = new JButton((Icon) null);
	public static MouseAdapter RESIZE_ADABTER;

	public static JButton REMOVE = new JButton((Icon) null);
	public static MouseAdapter REMOVE_ADABTER;

	public static JButton PAINT = new JButton((Icon) null);

	public static JButton BTN_SAVE = new JButton("Save");
	public static JButton BTN_LOAD = new JButton("load");
	
	public static JButton COPY = new JButton("Copy");
	public static MouseAdapter COPY_ADABTER;
	
    public static JButton ADD_PLUGIN = new JButton("Add Plug");
	
	public static JButton Draw_PLUGIN = new JButton("Draw Plug");
	public static JButton FILLCOLOR = new JButton("");
	public static JButton BORDERCOLOR = new JButton("");
	public static MouseAdapter DRAW_PLUGIN_ADABTER;
	
	private static final JLabel lblFillColor = new JLabel("Fill Color");
	private static final JLabel lblBorderColor = new JLabel("Border Color");
	private static final JSeparator separator = new JSeparator();
	private static final JSeparator separator_1 = new JSeparator();
	private static final JSeparator separator_2 = new JSeparator();
	private static final JSeparator separator_3 = new JSeparator();



	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				separator.setOrientation(SwingConstants.VERTICAL);
				separator.setBounds(1735, 106, 5, 847);
				separator.setForeground(Color.BLACK);
				separator.setBackground(Color.BLACK);
				REDO.setText("Redo");
				REDO.setBounds(322, 63, 101, 23);
				REDO.setForeground(Color.RED);
				REDO.setBackground(Color.WHITE);
				try {
					FRAME = new JFrame("Paint");
					FRAME.setVisible(true);
					JPanel panel = new JPanel();
					panel.setBounds(0, 0, 1352, 99);

					panel.setBackground(Color.DARK_GRAY);
					DRAW_BRUSH.setBounds(10, 115, 1721, 825);
					DRAW_BRUSH.setLayout(null);
					UNDO.setText("Undo");
					UNDO.setBounds(322, 25, 101, 23);

					UNDO.setForeground(Color.RED);
					UNDO.setBackground(Color.WHITE);
					MOVE.setBounds(445, 25, 101, 23);
					MOVE.setText("Move");
					MOVE.setFont(new Font("Tahoma", Font.BOLD, 13));

					MOVE.setForeground(Color.MAGENTA);
					MOVE.setBackground(Color.WHITE);
					RESIZE.setBounds(445, 62, 101, 23);
					RESIZE.setFont(new Font("Tahoma", Font.BOLD, 13));
					RESIZE.setText("Resize");

					RESIZE.setForeground(Color.BLUE);
					RESIZE.setBackground(Color.WHITE);
					REMOVE.setBounds(568, 25, 101, 23);
					REMOVE.setFont(new Font("Tahoma", Font.BOLD, 13));
					REMOVE.setText("Remove");

					REMOVE.setForeground(Color.MAGENTA);
					REMOVE.setBackground(Color.WHITE);
					PAINT.setBounds(568, 62, 101, 23);
					PAINT.setFont(new Font("Tahoma", Font.BOLD, 13));
					PAINT.setText("Paint");

					PAINT.setForeground(Color.BLUE);
					PAINT.setBackground(Color.WHITE);
					COPY.setBounds(679, 23, 101, 24);
					COPY.setFont(new Font("Tahoma", Font.BOLD, 13));
					

					COPY.setForeground(Color.MAGENTA);
					COPY.setBackground(Color.WHITE);
					panel.setLayout(null);
					panel.add(UNDO);
					SET_TRIANGLE.setBounds(12, 61, 79, 25);
					panel.add(SET_TRIANGLE);
					SET_CIRCLE.setBounds(12, 23, 79, 25);
					panel.add(SET_CIRCLE);
					SET_ELLIPSE.setBounds(103, 61, 73, 25);
					panel.add(SET_ELLIPSE);
					SET_LINE.setBounds(103, 23, 73, 25);
					panel.add(SET_LINE);
					SET_RECTANGLE.setBounds(188, 23, 73, 25);
					panel.add(SET_RECTANGLE);
					SET_SQUARE.setBounds(188, 61, 73, 25);
					panel.add(SET_SQUARE);
					panel.add(REDO);
					panel.add(MOVE);
					panel.add(RESIZE);
					panel.add(REMOVE);
					panel.add(PAINT);
					panel.add(COPY);
					BTN_SAVE.setFont(new Font("Tahoma", Font.BOLD, 15));
					BTN_SAVE.setForeground(new Color(0, 0, 0));
					BTN_SAVE.setBounds(934, 24, 120, 25);
					panel.add(BTN_SAVE);
					BTN_LOAD.setFont(new Font("Tahoma", Font.BOLD, 15));
					BTN_LOAD.setBounds(934, 61, 120, 25);
					panel.add(BTN_LOAD);
					FILLCOLOR.setBackground(Color.WHITE);
					FILLCOLOR.setForeground(Color.WHITE);
					FILLCOLOR.setFont(new Font("Tahoma", Font.BOLD, 15));
					FILLCOLOR.setBounds(1244, 13, 63, 36);
					panel.add(FILLCOLOR);
					
					BORDERCOLOR.setBackground(Color.BLACK);
					BORDERCOLOR.setForeground(Color.BLACK);
					BORDERCOLOR.setFont(new Font("Tahoma", Font.BOLD, 15));
					BORDERCOLOR.setBounds(1243, 54, 64, 36);
					panel.add(BORDERCOLOR);
					lblFillColor.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 19));
					lblFillColor.setForeground(Color.CYAN);
					lblFillColor.setBounds(1093, 15, 109, 35);
					
					panel.add(lblFillColor);
					lblBorderColor.setForeground(Color.CYAN);
					lblBorderColor.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 19));
					lblBorderColor.setBounds(1093, 52, 140, 35);
					
					panel.add(lblBorderColor);
					
					ADD_PLUGIN.setForeground(Color.BLACK);
					ADD_PLUGIN.setFont(new Font("Tahoma", Font.BOLD, 15));
					ADD_PLUGIN.setBounds(790, 23, 112, 23);
					panel.add(ADD_PLUGIN);
					
					Draw_PLUGIN.setForeground(Color.BLACK);
					Draw_PLUGIN.setFont(new Font("Tahoma", Font.BOLD, 15));
					Draw_PLUGIN.setBounds(790, 59, 112, 31);
					panel.add(Draw_PLUGIN);
					
					GroupLayout gl_dB = new GroupLayout(DRAW_BRUSH);
					gl_dB.setHorizontalGroup(
						gl_dB.createParallelGroup(Alignment.LEADING)
							.addGap(0, 1731, Short.MAX_VALUE)
					);
					gl_dB.setVerticalGroup(
						gl_dB.createParallelGroup(Alignment.TRAILING)
							.addGap(0, 800, Short.MAX_VALUE)
					);
					DRAW_BRUSH.setLayout(gl_dB);
					FRAME.getContentPane().setLayout(null);
					FRAME.getContentPane().add(separator);
					FRAME.getContentPane().add(DRAW_BRUSH);
					FRAME.getContentPane().add(panel);
					SHAPES.setBounds(1317, 40, 39, 22);
					panel.add(SHAPES);
					separator_1.setForeground(Color.BLACK);
					separator_1.setBounds(0, 106, 1735, 5);
					FRAME.getContentPane().add(separator_1);
					separator_1.setBackground(Color.BLACK);
					separator_2.setOrientation(SwingConstants.VERTICAL);
					separator_2.setForeground(Color.BLACK);
					separator_2.setBackground(Color.BLACK);
					separator_2.setBounds(0, 106, 5, 847);
					
					FRAME.getContentPane().add(separator_2);
					separator_3.setForeground(Color.BLACK);
					separator_3.setBackground(Color.BLACK);
					separator_3.setBounds(2, 951, 1735, 5);
					
					FRAME.getContentPane().add(separator_3);
					FRAME.setSize(1500, 1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Paint() {
		initialize();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		FRAME = new JFrame();
		FRAME.getContentPane().setBackground(Color.WHITE);

		JPanel panel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(FRAME.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(panel,
				Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(237, Short.MAX_VALUE)));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGap(0, 466, Short.MAX_VALUE));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGap(0, 65, Short.MAX_VALUE));
		panel.setLayout(gl_panel);
		FRAME.getContentPane().setLayout(groupLayout);
		FRAME.setBackground(Color.BLACK);
		FRAME.setBounds(100, 100, 482, 341);
		FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
