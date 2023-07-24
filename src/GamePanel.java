import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{
	
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25; // Size of the objects in the game
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE; // number objects that could fit on the screen
	static int delay = 120; // the higher the number is and the slower the game is (75 is average speed)
	static final int MAXDELAY = 30; // max speed
	final int x[] = new int[GAME_UNITS]; // coordinates of the snake body parts (size of the snake not bigger than the screen)
	final int y[] = new int[GAME_UNITS]; // coordinates of the snake body parts (size of the snake not bigger than the screen)
	int bodyParts = 6; // initial amount of body parts of the snake
	int applesEaten; // will equal initially 0
	int appleX; // random coordinate of apples
	int appleY; 
	char direction = 'R'; // snake goes Right initially 
	boolean running = false;
	Timer timer; 
	Random random;
	
	GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
		
	}
	
	public void startGame() {
		newApple(); // make new apple appear randomly
		running = true;
		timer = new Timer(delay, this);
		timer.start();
	}
	
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) { 
		if(running) {
			// grid for development help purpose
			/*
			for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT); // draw vertical lines on the panel
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE); // draw horizontal lines on the panel
			}
			*/
			
			// draw an apple
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			
			// draw snake head and body parts
			for(int i = 0; i< bodyParts;i++) {
				if(i == 0) { // head of he snake
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else { // body parts of the snake
					g.setColor(new Color(45,180,0));
					// random changing multicolor body parts
					g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255))); 
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
					
				}
			}
			
			// displaying the score
			g.setColor(Color.CYAN);
			g.setFont(new Font("Ink Free", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
			
			// displaying the speed
			g.setColor(Color.GREEN);
			g.setFont(new Font("Ink Free", Font.BOLD, 40));
			//FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Speed: "+delay, (SCREEN_WIDTH - metrics.stringWidth("Speed: "+delay))-5, g.getFont().getSize()); 
		}
		else {
			gameOver(g);
		}
		
	}
	
	public void newApple() { // generates coordinates of a new apple when this function is called
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	
	public void move() {
		for(int i = bodyParts;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
			
		}
		
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
		
	}
	
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++; // the score
			newApple();
			
			if (delay > MAXDELAY) {
				delay-=5;
			}

		}
	}
	
	public void checkCollisions() {
		// checks if head of snake collides with body
		for(int i = bodyParts; i>0;i--) {
			if((x[0] == x[i]) && (y[0] == y[i])) { 
				running = false;
			}
		}
		// check if head of snake touches left border
		if(x[0] < 0) {
			// running = false;
			x[0] += SCREEN_WIDTH; // snake reappear on the right border
		}
		// check if head of snake touches right border
		if(x[0] > SCREEN_WIDTH) {
			// running = false;
			x[0] -= SCREEN_WIDTH; // snake reappear on the left border
		}
		// check if head of snake touches top border
		if(y[0] < 0) {
			// running = false;
			y[0] += SCREEN_HEIGHT; // snake reappear at the bottom
		}
		// check if head of snake touches bottom border
		if(y[0] > SCREEN_HEIGHT) {
			// running = false;
			y[0] -= SCREEN_HEIGHT; // snake reappear at the top
		}
		
		if(!running) {
			timer.stop();
		}
	}
	
	public void gameOver(Graphics g) {
		// display 
		g.setColor(Color.CYAN);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
		
		// Game Over text
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2); // Center the text middle screen
		
		// Display restart button after Game over 
		JButton b=new JButton("Try again", new ImageIcon("C:\\Users\\yb74\\eclipse-workspace\\Snake\\src\\images\\tryagain.png"));  
		//JButton b=new JButton(new ImageIcon("C:\\tryagain.png")); 
		b.setBounds(150,375,300,150); // button size and position
		this.add(b); // adding button to the jframe (to the app window)
		// handeling event on click
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reset(b);
			}
		});
	}
	
	public void reset(JButton b) {
		newApple(); // make new apple appear randomly
		running = true;
		timer.start();
		
		bodyParts = 6; // Reset the snake's bodyparts at initial number
		applesEaten = 0;
		delay = 120;
		
		b.setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint(); // if the game is not running, we call this method
		
	}
	
	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			// prevent from switching to 180° to avoid collision with body parts
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}
		
}
