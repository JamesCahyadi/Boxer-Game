import javax.swing.*;
import java.awt.*;

public class BoxerPanel extends JPanel {

	int WIN_WIDTH = 1280;
	int WIN_HEIGHT = 720;

	// Ground
	int GROUND_X = 100;
	int GROUND_Y = 620;
	int GROUND_WIDTH = 1080;
	int GROUND_HEIGHT = 30;

	int P1_START_X = 200;
	int P1_START_Y = 200;
	int P1_HEART_X = 5;
	int P1_HEART_Y = 5;
	boolean P1_FACING_LEFT = false;

	int P2_START_X = 1060;
	int P2_START_Y = 200;
	int P2_HEART_X = 1180;
	int P2_HEART_Y = 5;
	boolean P2_FACING_LEFT = true;

	Player p1 = new Player(P1_START_X, P1_START_Y, P1_HEART_X, P1_HEART_Y, P1_FACING_LEFT);
	Player p2 = new Player(P2_START_X, P2_START_Y, P2_HEART_X, P2_HEART_Y, P2_FACING_LEFT);
	
	int NUM_OF_OBSTACLES = 10;
	Obstacle[] fallingObstacles = new Obstacle[NUM_OF_OBSTACLES];
	
	public void paintComponent(Graphics g) {
		// Background Rectangle
		g.setColor(new Color(42, 117, 222));
		g.fillRect(0, 0, WIN_WIDTH, WIN_HEIGHT);

		// Ground Platform
		g.setColor(new Color(50, 200 ,45));
		g.fillRect(GROUND_X, GROUND_Y, GROUND_WIDTH, GROUND_HEIGHT);

		// Player 1
		g.setColor(Color.ORANGE);
		p1.move(g, GROUND_X, GROUND_Y, GROUND_WIDTH, GROUND_HEIGHT, WIN_WIDTH, WIN_HEIGHT);
		if(p1.punching) {
			p1HitPunch(); // check if p1 punches p2
		}

		// Player 2
		g.setColor(Color.WHITE);
		p2.move(g, GROUND_X, GROUND_Y, GROUND_WIDTH, GROUND_HEIGHT, WIN_WIDTH, WIN_HEIGHT);
		if(p2.punching) {
			p2HitPunch(); // check if p2 punches p1
		}
		
		// Make the obstacles fall
		for(Obstacle o: fallingObstacles) {
			o.fall(g, WIN_HEIGHT);
			// Obstacle hits player 1
			if(o.hitPlayerTopLeft(p1.playerX, p1.playerY)
					|| o.hitPlayerTopRight(p1.playerX, p1.playerY, p1.PLAYER_WIDTH)
					|| o.hitPlayerBottomLeft(p1.playerX, p1.playerY, p1.PLAYER_HEIGHT)
					|| o.hitPlayerBottomRight(p1.playerX, p1.playerY, p1.PLAYER_WIDTH, p1.PLAYER_HEIGHT)) {
				if(p1.playerMidpointX < o.obstacleMidpointX) {
					p1.knockBackLeft = true;
				} else {
					p1.knockBackRight = true;
				}
			}
			// Obstacle hits player 2
			if(o.hitPlayerTopLeft(p2.playerX, p2.playerY)
					|| o.hitPlayerTopRight(p2.playerX, p2.playerY, p2.PLAYER_WIDTH)
					|| o.hitPlayerBottomLeft(p2.playerX, p2.playerY, p2.PLAYER_HEIGHT)
					|| o.hitPlayerBottomRight(p2.playerX, p2.playerY, p2.PLAYER_WIDTH, p2.PLAYER_HEIGHT)) {
				if(p2.playerMidpointX < o.obstacleMidpointX) {
					p2.knockBackLeft = true;
				} else {
					p2.knockBackRight = true;
				}
			}
			
			if(p1.gameOver) {
				g.setColor(Color.WHITE);
				g.setFont(new Font("Cooper Black", Font.PLAIN, 100)); 
				g.drawString("PLAYER TWO WINS!", 120, 120);
				p2.PLAYER_SPEED = 0;
				o.OBSTACLE_VEL = 0;
			} else if(p2.gameOver) {
				g.setColor(Color.YELLOW);
				g.setFont(new Font("Cooper Black", Font.PLAIN, 100)); 
				g.drawString("PLAYER ONE WINS!", 120, 120);
				p1.PLAYER_SPEED = 0;
				o.OBSTACLE_VEL = 0;
			}
		}	
	}

	// If p1 hits p2 with a punch, p2 gets knocked back
	public void p1HitPunch() {
		if((p1.handX >= p2.playerX && p1.handX <= p2.playerX + p2.PLAYER_WIDTH
				&& p1.handY >= p2.playerY && p1.handY <= p2.playerY + p2.PLAYER_HEIGHT) // check if p1 hits right side of p2
				||(p1.handX + p1.HAND_WIDTH >= p2.playerX && p1.handX + p1.HAND_WIDTH <= p2.playerX + p2.PLAYER_WIDTH
				&& p1.handY >= p2.playerY && p1.handY <= p2.playerY + p2.PLAYER_HEIGHT)) { // check if p1 hits left side of p2
			if(p1.playerX > p2.playerX) {
				p2.knockBackLeft = true;
			} else {
				p2.knockBackRight = true;
			}
		}
	}

	// If p2 hits p1 with a punch, p1 gets knocked back
	public void p2HitPunch() {
		if ((p2.handX >= p1.playerX && p2.handX <= p1.playerX + p1.PLAYER_WIDTH
				&& p2.handY >= p1.playerY && p2.handY <= p1.playerY + p1.PLAYER_HEIGHT) // check if p2 hits right side of p1
				|| (p2.handX + p2.HAND_WIDTH >= p1.playerX && p2.handX + p2.HAND_WIDTH <= p1.playerX + p1.PLAYER_WIDTH
				&& p2.handY >= p1.playerY && p2.handY <= p1.playerY + p1.PLAYER_HEIGHT)) { // check if p2 hits left side of p1
			if(p2.playerX > p1.playerX) {
				p1.knockBackLeft = true;
			} else {
				p1.knockBackRight = true;
			}
		}
	}

	public BoxerPanel() {
		super();	
		
		// Create obstacles
		for(int i = 0; i < fallingObstacles.length; i++) {
			fallingObstacles[i] = new Obstacle();
		}
	}

}
