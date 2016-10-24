import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;


public class SudokuFrame extends JFrame {
	private JPanel panel;
	private JCheckBox checkbox;
	private JLabel label;
	private JSlider slider;
	private JTextArea left;
	private JTextArea right;

	public SudokuFrame() {
		super("Sudoku Solver");

		// YOUR CODE HERE
		// Get content pane -- contents of the window
		JComponent content = (JComponent) getContentPane();

		// Set to use the "flow" layout
		// (controls the arrangement of the components in the content)
		content.setLayout(new FlowLayout());

		// ****
		// Set up controls in the frame
		// ****

		// Create a vertical box component
		JComponent box = new JPanel();
		box.setLayout(new BoxLayout(box, BoxLayout.X_AXIS));
		content.add(box);

		left  = new JTextArea(15, 20);
		right = new JTextArea(15, 20);

		left.setBorder(new TitledBorder("Puzzle"));
		right.setBorder(new TitledBorder("Solution"));

		box.add(left);
		box.add(right);

		// put a checkbox in the panel
		checkbox = new JCheckBox("Auto Check");
		box.add(checkbox);

		box.add(Box.createVerticalStrut(20)); // 20 pixels vertical space


		// later, access the control's state with:
		// (boolean) checkbox.isSelected()
		// (int) slider.getValue()

		// ****
		// Done installing controls
		// ****

		// Could do this:
		// setLocationByPlatform(true);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	
	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		SudokuFrame frame = new SudokuFrame();
	}

}
