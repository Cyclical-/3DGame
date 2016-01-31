import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
//import java.io.*; //used for end-condition checking
//import javax.sound.sampled.*;//same as above

/*
 * Camera class, keeps track of player position on the map
 * 
 * 
 */
public class Camera implements KeyListener { //Camera class
	public double xPos, yPos, xDir, yDir, xPlane, yPlane; //position and direction variables
	public boolean left, right, forward, back; //key booleans
	public final double MOVEMENT_SPEED = .065; //movement speed
	public final double ROTATION_SPEED = .045; //rotation speed
	//public int flag = 0; //Used for playing end music, not needed since generated maze has no end

	public Camera(double xP, double yP, double xD, double yD, double xPl,
			double yPl) { //class constructor
		xPos = xP; // position of camera on map
		yPos = yP; // position of camera on map
		xDir = xD; // direction vector
		yDir = yD; // direction vector
		xPlane = xPl; // vector perpendicular to the direction, field of view
		yPlane = yPl; // vector perpendicular to the direction, field of view
	}

	public void keyPressed(KeyEvent key) { //keylisteners, sets key booleans if the corresponding key is pressed
		if ((key.getKeyCode() == KeyEvent.VK_LEFT))
			left = true;
		if ((key.getKeyCode() == KeyEvent.VK_RIGHT))
			right = true;
		if ((key.getKeyCode() == KeyEvent.VK_UP))
			forward = true;
		if ((key.getKeyCode() == KeyEvent.VK_DOWN))
			back = true;
		if ((key.getKeyCode() == KeyEvent.VK_ESCAPE))
			System.exit(0);
	}

	public void keyReleased(KeyEvent key) { //unsets key booleans if corresponding keys are released
		if ((key.getKeyCode() == KeyEvent.VK_LEFT))
			left = false;
		if ((key.getKeyCode() == KeyEvent.VK_RIGHT))
			right = false;
		if ((key.getKeyCode() == KeyEvent.VK_UP))
			forward = false;
		if ((key.getKeyCode() == KeyEvent.VK_DOWN))
			back = false;
	}

	public void update(int[][] map) { //update map position
		if (forward) { //if the player is moving forward
			if (map[(int) (xPos + xDir * MOVEMENT_SPEED)][(int) yPos] == 0) { //if the player is moving along the X-axis
				xPos += xDir * MOVEMENT_SPEED;
			}
			if (map[(int) xPos][(int) (yPos + yDir * MOVEMENT_SPEED)] == 0)//if the player is moving along the Y-axis
				yPos += yDir * MOVEMENT_SPEED;
		}
		if (back) {
			if (map[(int) (xPos - xDir * MOVEMENT_SPEED)][(int) yPos] == 0)//if the player is moving along the X-axis
				xPos -= xDir * MOVEMENT_SPEED;
			if (map[(int) xPos][(int) (yPos - yDir * MOVEMENT_SPEED)] == 0)//if the player is moving along the Y-axis
				yPos -= yDir * MOVEMENT_SPEED;
		}
		if (right) {
			double oldxDir = xDir; //old direction
			xDir = xDir * Math.cos(-ROTATION_SPEED) - yDir //change the direction based on the rotation speed
					* Math.sin(-ROTATION_SPEED);
			yDir = oldxDir * Math.sin(-ROTATION_SPEED) + yDir //change direction based on the rotation speed
					* Math.cos(-ROTATION_SPEED);
			double oldxPlane = xPlane;
			xPlane = xPlane * Math.cos(-ROTATION_SPEED) - yPlane //alter the FOV vectors to keep synchronisation
					* Math.sin(-ROTATION_SPEED);
			yPlane = oldxPlane * Math.sin(-ROTATION_SPEED) + yPlane //alter the FOV vectors to keep synchronisation
					* Math.cos(-ROTATION_SPEED);
		}
		if (left) {
			double oldxDir = xDir;
			xDir = xDir * Math.cos(ROTATION_SPEED) - yDir//change the direction based on the rotation speed
					* Math.sin(ROTATION_SPEED);
			yDir = oldxDir * Math.sin(ROTATION_SPEED) + yDir//change the direction based on the rotation speed
					* Math.cos(ROTATION_SPEED);
			double oldxPlane = xPlane;
			xPlane = xPlane * Math.cos(ROTATION_SPEED) - yPlane//alter the FOV vectors to keep synchronisation
					* Math.sin(ROTATION_SPEED);
			yPlane = oldxPlane * Math.sin(ROTATION_SPEED) + yPlane//alter the FOV vectors to keep synchronisation
					* Math.cos(ROTATION_SPEED);
		}
		//The code below is used to calculate if the player is close enough to the end position to trigger graphic change and music. Commented out because generated maze has no end.
		/*
		if ((Math.round(xPos) == 3 || Math.round(xPos) == 4) && (Math.round(yPos) == 9 || Math.round(yPos) == 10) && flag != 1) {
			for (int i = 0; i < Game.map[0].length; i++) {
				for (int j = 0; j < Game.map.length; j++) {
					if (Game.map[j][i] > 0) {
						Game.map[j][i] = 7;
					}
				}

			}
			try {
				File yourFile = new File("res/cena.wav");
				AudioInputStream stream;
				AudioFormat format;
				DataLine.Info info;
				Clip clip;

				stream = AudioSystem.getAudioInputStream(yourFile);
				format = stream.getFormat();
				info = new DataLine.Info(Clip.class, format);
				clip = (Clip) AudioSystem.getLine(info);
				clip.open(stream);
				clip.start();
				flag = 1;
			} catch (Exception e) {
			}
		}
		*/

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}
}