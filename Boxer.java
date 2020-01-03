import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Boxer implements ActionListener, KeyListener {

	JFrame frame = new JFrame("Boxer - James Cahyadi");;
	BoxerPanel panel = new BoxerPanel();
	Timer timer = new Timer(1000/60, this);
	JButton quit = new JButton("Quit"); 

	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource() == timer) {
			panel.repaint();
		}
		
		if(panel.p1.gameOver || panel.p2.gameOver) {
			quit.setSize(100,50);
			quit.setLocation(1180, 670);
			quit.addActionListener(this);
			panel.add(quit);
		}
		
		if(evt.getSource() == quit) {
			System.exit(0);
		}
	}

	public void keyReleased(KeyEvent evt) {
		switch (evt.getKeyCode()) {
		// PLAYER ONE
		case 65: 
			panel.p1.playerLeft = false;
			break;		
		case 68: 
			panel.p1.playerRight = false; 
			break;

			// PLAYER TWO
		case 37: 
			panel.p2.playerLeft = false;
			break;		
		case 39: 
			panel.p2.playerRight = false; 
			break;
		}
	}

	public void keyPressed(KeyEvent evt) {
	//	System.out.println(evt.getKeyCode());
		switch (evt.getKeyCode()) {
		// PLAYER ONE
		case 65: // a key
			if(panel.p1.punching == false) {	
				panel.p1.playerLeft = true;
				if(panel.p1.facingLeft == false) {
					panel.p1.turnLeft();
				}
			}
			break;		
		case 68: // d key
			if(panel.p1.punching == false) {
				panel.p1.playerRight = true; 
				if(panel.p1.facingLeft) {
					panel.p1.turnRight();
				}
			}
			break;
		case 87: // w key
			if(panel.p1.falling == false) {
				panel.p1.playerUp = true;
			}
			break;
		case 16: // shift key
			panel.p1.punch();
			break;

			// PLAYER TWO
		case 37: // left arrow
			panel.p2.playerLeft = true;
			if(panel.p2.facingLeft == false && panel.p2.punching == false) {
				panel.p2.turnLeft();
			}
			break;		
		case 39: // right arrow 
			panel.p2.playerRight = true; 
			if(panel.p2.facingLeft && panel.p2.punching == false) {
				panel.p2.turnRight();
			}
			break;
		case 38: // up arrow
			if(panel.p2.falling == false) {
				panel.p2.playerUp = true;
			}
			break;
		case 17: // right ctrl key
			panel.p2.punch();
			break;
		}
	}

	public void keyTyped(KeyEvent evt) {

	}

	public Boxer() {
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(1280, 720));

		frame.addKeyListener(this);
		frame.setContentPane(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);

		timer.restart();
	}

	public static void main(String[] args) {
		new Boxer();
	}

}
