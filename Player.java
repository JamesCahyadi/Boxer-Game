import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Player {

	int PLAYER_WIDTH = 20;
	int PLAYER_HEIGHT = 40;
	int PLAYER_SPEED = 5;
	int JUMP_DEFAULT = 10;
	int jumpHeightSoFar = JUMP_DEFAULT;
	int MAX_JUMP_HEIGHT = 200;
	int playerX, playerY, changeInY, playerMidpointX;

	int numOfLives = 3;
	int HEART_X;
	int HEART_Y;
	int HEART_SPACING = 30;

	int EYE_WIDTH = 10;
	int EYE_HEIGHT = 5;
	int eyeX, eyeY;

	int HAND_WIDTH = 8;
	int HAND_HEIGHT = 5;
	int HAND_RANGE = 28;
	int handX, handY, handBackSoFar;
	boolean punching = false;

	boolean falling = true;
	boolean playerLeft = false;
	boolean playerRight = false;
	boolean playerUp = false;
	boolean facingLeft;

	int KNOCK_BACK_DEFAULT = 10;
	int knockBackDistance = 325;
	int knockBackSoFar = KNOCK_BACK_DEFAULT;
	boolean knockBackLeft = false;
	boolean knockBackRight = false;
	
	boolean gameOver = false;

	BufferedImage heart;

	public Player(int startX, int startY, int HEART_X, int HEART_Y, boolean facingLeft) {		
		this.facingLeft = facingLeft;
		this.playerX = startX;
		this.playerY = startY;
		if(facingLeft) {
			this.eyeX = startX;
			this.eyeY = startY + 3;
			this.handX = startX - HAND_WIDTH - 3;
			this.handY = startY + 10;
		} else {
			this.eyeX = startX + (PLAYER_WIDTH - EYE_WIDTH);
			this.eyeY = startY + 3;
			this.handX = startX + PLAYER_WIDTH + 3;
			this.handY = startY + 10;
		}
		this.HEART_X = HEART_X;
		this.HEART_Y = HEART_Y;
		try{
			heart = ImageIO.read(new File("heart.png"));
		}catch(IOException e){
			System.out.println("Unable to load heart");
		}
	}

	public void move(Graphics g, int GROUND_X, int GROUND_Y, int GROUND_WIDTH, int GROUND_HEIGHT, int WIN_WIDTH, int WIN_HEIGHT) {
		// Draw hearts
		if(numOfLives == 3) {
			g.drawImage(heart, HEART_X, HEART_Y, null);
			g.drawImage(heart, HEART_X + HEART_SPACING, HEART_Y, null);
			g.drawImage(heart, HEART_X + (HEART_SPACING * 2), HEART_Y, null);
		} else if (numOfLives == 2) {
			g.drawImage(heart, HEART_X, HEART_Y, null);
			g.drawImage(heart, HEART_X + HEART_SPACING, HEART_Y, null);
		} else if(numOfLives == 1) {
			g.drawImage(heart, HEART_X, HEART_Y, null);
		} else if(numOfLives == 0) {
			gameOver = true;
		}

		// Player Body
		g.fillRect(playerX, playerY, PLAYER_WIDTH, PLAYER_HEIGHT);
		// Determine the x-coordinate midpoint of player
		playerMidpointX = playerX + (PLAYER_WIDTH/2);

		// Player Eye
		g.setColor(Color.BLACK);
		g.fillRect(eyeX, eyeY, EYE_WIDTH, EYE_HEIGHT);

		// Player hand
		g.setColor(Color.RED);
		g.fillRect(handX, handY, HAND_WIDTH, HAND_HEIGHT);

		// Player falling down
		if(falling) {
			playerY += PLAYER_SPEED;
			eyeY += PLAYER_SPEED;
			handY += PLAYER_SPEED;
		}

		if((playerX + PLAYER_WIDTH < GROUND_X || playerX > GROUND_X + GROUND_WIDTH)
				&& (playerY + PLAYER_HEIGHT >= GROUND_Y)){ // Player out of bounds
			falling = true;
		} else if (playerY + PLAYER_HEIGHT > GROUND_Y && playerY + PLAYER_HEIGHT < GROUND_Y + GROUND_HEIGHT
				&& playerX + PLAYER_WIDTH >= GROUND_X && playerX <= GROUND_X + GROUND_WIDTH) { // Player is on the ground
			changeInY = playerY + PLAYER_HEIGHT - GROUND_Y; // determine how low into the ground the player was
			playerY = GROUND_Y - PLAYER_HEIGHT;
			eyeY -= changeInY; // re-adjust the eye after landing
			handY -= changeInY; //re-adjust the hand after landing
			falling = false;
			playerUp = false;
		} 

		// Player Jumping
		if(playerUp) {
			if(jumpHeightSoFar >= MAX_JUMP_HEIGHT && falling == false) {  // player reaches max height
				playerY += PLAYER_SPEED;
				eyeY += PLAYER_SPEED;
				handY += PLAYER_SPEED;
				jumpHeightSoFar = JUMP_DEFAULT; // reset jump height
				falling = true;
			} else if(jumpHeightSoFar < MAX_JUMP_HEIGHT && falling == false){ // player still jumping
				playerY -= PLAYER_SPEED;
				eyeY -= PLAYER_SPEED;
				handY -= PLAYER_SPEED;
				jumpHeightSoFar += PLAYER_SPEED;
			}
		}

		// Player Movement
		if(playerLeft) {
			playerX -= PLAYER_SPEED;
			eyeX -= PLAYER_SPEED;	
			handX -= PLAYER_SPEED;
		}
		if(playerRight) {
			playerX += PLAYER_SPEED;
			eyeX += PLAYER_SPEED;
			handX += PLAYER_SPEED;
		}

		// Return hand back after punching
		if(facingLeft == false) { // Facing Right
			if(punching && handBackSoFar < 7) { // 7 is used since 1+2+3+4+5+6+7 = 28 = HAND_RANGE (change to 6 if HAND_RANGE = 15)
				handBackSoFar += 1;
				handX = handX - handBackSoFar;
			} else if (punching && handBackSoFar >= 7) {
				punching = false;
			}
		} else if(facingLeft) { // Facing Left
			if(punching && handBackSoFar < 7) {
				handBackSoFar += 1;
				handX = handX + handBackSoFar;
			} else if (punching && handBackSoFar >= 7) {
				punching = false;
			}
		}

		// If knocked back to the left
		if(knockBackLeft && knockBackSoFar < 25) { // note: 1+2...+24+25=325= knockBackDistance
			knockBackSoFar += 1;
			playerX -= knockBackSoFar;
			eyeX -= knockBackSoFar;
			handX -= knockBackSoFar;
		} else if(knockBackLeft && knockBackSoFar >= 25) {
			knockBackSoFar = KNOCK_BACK_DEFAULT;
			knockBackLeft = false;
		}

		// If knocked back to the right
		if(knockBackRight && knockBackSoFar < 25) { // note: 10+11...+24+25=325= knockBackDistance
			knockBackSoFar += 1;
			playerX += knockBackSoFar;
			eyeX += knockBackSoFar;
			handX += knockBackSoFar;
		} else if(knockBackRight && knockBackSoFar >= 25) {
			knockBackSoFar = KNOCK_BACK_DEFAULT;
			knockBackRight = false;
		}

		// Respawn player if player falls out of bounds, lose one life
		if(playerY > WIN_HEIGHT) {
			numOfLives -= 1;
			if(numOfLives > 0) {
				playerX = WIN_WIDTH/2 - PLAYER_WIDTH/2; 
				playerY = 0;
				facingLeft = true;
				eyeX = playerX;
				eyeY = playerY + 3;
				handX = playerX - HAND_WIDTH - 3;
				handY = playerY + 10;
			}
		}
	}

	// To look right
	public void turnRight() {
		eyeX = eyeX + (PLAYER_WIDTH - EYE_WIDTH);
		handX = handX + HAND_WIDTH + PLAYER_WIDTH + 7;
		facingLeft = false;
	}

	// To look left
	public void turnLeft() {
		eyeX = eyeX - (PLAYER_WIDTH - EYE_WIDTH);
		handX = handX - HAND_WIDTH - PLAYER_WIDTH - 7;
		facingLeft = true;
	}

	// Punching animation
	public void punch(){
		if(punching == false) {
			handBackSoFar = 0;
			punching = true;
			if(facingLeft == false) {
				handX = handX + HAND_RANGE;
			} else if(facingLeft) {
				handX = handX - HAND_RANGE;
			}
		}
	}
}
