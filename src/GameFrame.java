import javax.swing.JFrame;

public class GameFrame extends JFrame{
	GameFrame() {
		
		this.add(new GamePanel());
		this.setTitle("Snake");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack(); // This will take all the components that we add on the JFrame
		this.setVisible(true); // show the window of the app
		this.setLocationRelativeTo(null); // Window will appear in the middle of the screen
	}
}