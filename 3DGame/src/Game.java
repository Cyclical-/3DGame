import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;
import javax.swing.*;

public class Game extends JFrame implements Runnable { //Main Game class

	private static final long serialVersionUID = 1L; //version
	private Thread thread;
	public static int[][] map = new MazeGen(5).grid; //generate a maze (Has no end)
	public int mapWidth = map.length; //Width of the map
	public int mapHeight = map[0].length; //Height of the map
	private boolean running; //running flag
	private BufferedImage image; //screen image
	public int[] pixels; //array of pixels for rendering textures
	public ArrayList<Texture> textures; //arraylist of textures
	public Camera camera; //Camera object
	public Screen screen; //Screen object
	//Below is a static map that has an end to it 
	/*
	public static int[][] map = { 
			{ 2, 5, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 },
			{ 4, 0, 4, 0, 4, 0, 4, 0, 0, 0, 0, 0, 2 },
			{ 2, 0, 4, 0, 0, 0, 4, 0, 2, 0, 2, 0, 2 },
			{ 2, 0, 0, 0, 4, 4, 4, 0, 2, 0, 4, 0, 2 },
			{ 2, 0, 4, 0, 0, 0, 0, 0, 3, 0, 2, 2, 2 },
			{ 2, 0, 4, 0, 2, 2, 4, 0, 2, 7, 2, 2, 2 },
			{ 2, 0, 4, 0, 0, 2, 4, 0, 2, 2, 2, 0, 2 },
			{ 2, 0, 4, 0, 2, 2, 2, 2, 2, 2, 2, 0, 2 },
			{ 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2 },
			{ 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 } 
			};
	*/
	

	public Game() { //constructor
		thread = new Thread(this); //create a new thread
		image = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB); //create the image for the screen
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData(); //assign pixels to the image
		textures = new ArrayList<Texture>(); //create the textures arraylist
		//add a bunch of textures to the arraylist
		textures.add(Texture.brick);
		textures.add(Texture.redbrick);
		textures.add(Texture.bluestone);
		textures.add(Texture.stone);
		textures.add(Texture.painting1);
		textures.add(Texture.intro);
		textures.add(Texture.johncena);
		textures.add(Texture.painting3);
		camera = new Camera(2, 1.5, 1, 0, 0, -0.75); //create a new camera at the start of the maze
		screen = new Screen(map, mapWidth, mapHeight, textures, 640, 480); //initialize the rendering
		addKeyListener(camera); //add the key listener for movement
		setSize(640, 480); //set the window size
		setResizable(false); //set the window as non-resizable (would break rendering)
		setTitle("3D Graphics test"); //title
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //terminate on close
		setBackground(Color.black); //background colour
		setLocationRelativeTo(null);
		setVisible(true); //make the program visible
		start(); //call start()
	}

	private synchronized void start() { //start the game thread
		running = true; //set the running flag
		thread.start(); //starts the game thread
	}

	public synchronized void stop() { //stops the game thread
		running = false; //sets the running flag to false
		try {
			thread.join(); //wait for the thread to die
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void render() { //rendering initialization and drawing
		BufferStrategy buffers = getBufferStrategy(); //create a bufferstrategy
		if (buffers == null) { //if none exists
			createBufferStrategy(3); //set the bufferstrategy to triple-buffering
			return; //exit the method (first run)
		}
		Graphics g = buffers.getDrawGraphics(); //create the graphics object from the buffer
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null); // draw the screen on the graphic object
		buffers.show(); //show the buffer
	}

	public void run() { //main running code
		double lastTime = System.nanoTime(); //get the current system time in nanoseconds
		final double fps = 1000000000 / 60.0; //1/60th of a second: 60 frames per second
		double d = 0; //frametime
		requestFocus(); //request screen focus
		while (running) { //while the game is running
			double timeNow = System.nanoTime(); //get the current time in nanoseconds
			d = d + ((timeNow - lastTime) / fps); //frametime = previous frametime + the difference between this frame and the last divided by the FPS
			lastTime = timeNow; //time exchange
			while (d >= 1) { //while a frame should be rendered
				screen.update(camera, pixels); //update the screen
				camera.update(map); //update map position
				d--; //subtract one from frametime
			}
			render(); //redraw
		}
	}

	public static void main(String[] args) { //main method
		new Game(); //start the game by calling Game()
	}
}