import java.awt.Color;
import java.awt.Graphics;

public class Obstacle {
	int OBSTACLE_VEL = (int) (Math.random()* 5 + 3);
	int OBSTACLE_WIDTH = 50;
	int OBSTACLE_HEIGHT = 50;
	int obstacleX = (int) (Math.random() * (1280 - OBSTACLE_WIDTH) + 1);
	int obstacleY = -OBSTACLE_HEIGHT;
	int obstacleMidpointX = obstacleX + (OBSTACLE_WIDTH/2);
	
	public void fall(Graphics g, int WIN_HEIGHT) {
		// Draw Obstacle
		g.setColor(Color.DARK_GRAY);
		g.fillRect(obstacleX, obstacleY, OBSTACLE_WIDTH, OBSTACLE_HEIGHT);
		
		// Obstacle fall
		obstacleY += OBSTACLE_VEL;
		
		// Obstacles respawn after falling out of bounds
		if(obstacleY > WIN_HEIGHT) {
			OBSTACLE_VEL = (int) (Math.random()* 5 + 3);
			obstacleX = (int) (Math.random() * (1280 - OBSTACLE_WIDTH) + 1);
			obstacleMidpointX = obstacleX + (OBSTACLE_WIDTH/2);
			obstacleY = -OBSTACLE_HEIGHT;
		}
	}
	
	// Obstacle hits the top left corner of the player
	public boolean hitPlayerTopLeft(int playerX, int playerY) {
		return (playerX > obstacleX && playerX < obstacleX + OBSTACLE_WIDTH 
				&& playerY > obstacleY && playerY < obstacleY + OBSTACLE_HEIGHT);
	}
	
	// Obstacle hits the top right corner of the player
	public boolean hitPlayerTopRight(int playerX, int playerY, int PLAYER_WIDTH) {
		return (playerX + PLAYER_WIDTH > obstacleX && playerX + PLAYER_WIDTH < obstacleX + OBSTACLE_WIDTH
				&& playerY > obstacleY && playerY < obstacleY + OBSTACLE_HEIGHT);
	}
	
	// Obstacle hits the bottom left corner of the player
	public boolean hitPlayerBottomLeft(int playerX, int playerY, int PLAYER_HEIGHT) {
		return (playerX > obstacleX && playerX < obstacleX + OBSTACLE_HEIGHT
				&& playerY + PLAYER_HEIGHT > obstacleY && playerY + PLAYER_HEIGHT < obstacleY + OBSTACLE_HEIGHT);
	}
	
	// Obstacle hits the bottom right corner of the player
	public boolean hitPlayerBottomRight(int playerX, int playerY, int PLAYER_WIDTH, int PLAYER_HEIGHT) {
		return (playerX + PLAYER_WIDTH > obstacleX && playerX + PLAYER_WIDTH < obstacleX + OBSTACLE_HEIGHT
				&& playerY + PLAYER_HEIGHT > obstacleY && playerY + PLAYER_HEIGHT < obstacleY + OBSTACLE_HEIGHT);
	}
}
