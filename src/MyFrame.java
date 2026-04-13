import java.awt.*;
import javax.swing.*;

public class MyFrame extends JFrame{
	
	MyPanel panel;
	MyFrame(String title){
		
		panel = new MyPanel();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		this.add(panel);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.getTitle();
		this.setTitle(title);
	}
	

	

}
