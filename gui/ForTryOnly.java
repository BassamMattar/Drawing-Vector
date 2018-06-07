package gui;

import java.awt.EventQueue;
import java.awt.MouseInfo;

import javax.print.DocFlavor.INPUT_STREAM;
import javax.swing.JFrame;

import java.awt.event.InputEvent;
import java.awt.event.InputMethodEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.JPanel;

public class ForTryOnly {

	private static JFrame frame;
	static JLabel lblNewLabel;
	private static boolean pressed = false;
	private static int x = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ForTryOnly window = new ForTryOnly();

					window.frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ForTryOnly() {
		initialize();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				pressed = true;
				lblNewLabel.setText("that " + e.getPoint() + "AFTER " + x + " Operations");
			}
		});
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(56, 208, 284, 16);
		frame.getContentPane().add(lblNewLabel);

		JPanel panel = new JPanel();
		panel.setBounds(12, 0, 408, 93);
		frame.getContentPane().add(panel);
	}

	public static boolean NodeClick() {
		pressed = false;
		frame.getContentPane().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				pressed = true;
				lblNewLabel.setText("that " + e.getPoint() + "AFTER " + x + " Operations");
			}
		});
		return pressed;

	}
}
