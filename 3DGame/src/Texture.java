import java.awt.image.BufferedImage; 
import java.io.File; 
import java.io.IOException; 
import javax.imageio.ImageIO; 
 /*
  * This class is used to create the textures to be rendered
  */
public class Texture { 
 public int[] pixels; //pixel array
 private String path; //file path
 public final int SIZE; //size of the image
  
 public Texture(String location, int size) {  //class constructor
  path = location; //set path to location parameter
  SIZE = size; //set SIZE to size parameter
  pixels = new int[SIZE * SIZE];  //set size of pixel array to the number of pixels in the image
  load(); //call the load() method
 } 
  
 private void load() { //loads the image into the array
  try { 
   BufferedImage image = ImageIO.read(new File(path));  //create a buffered image at from the file at that path
   int w = image.getWidth();  //get the image width
   int h = image.getHeight(); //get the image height
   image.getRGB(0, 0, w, h, pixels, 0, w);  //Get the RGB value of each pixel in the image
  } catch (IOException e) { 
   e.printStackTrace(); 
  } 
 } 
 
 //The lines below import the images and create Texture objects containing an array of the RGB values of the pixels in the image.
 
 public static Texture brick = new Texture("res/brick.png", 64); 
 public static Texture redbrick = new Texture("res/redbrick.png", 64); 
 public static Texture bluestone = new Texture("res/bluestone.png", 64); 
 public static Texture stone = new Texture("res/greystone.png", 64); 
 public static Texture painting1 = new Texture("res/painting1.png", 64);
 public static Texture intro = new Texture("res/intro.png", 64);
 public static Texture johncena = new Texture("res/johncena.jpg", 64);
 public static Texture painting3 = new Texture("res/painting3.jpg", 64);

}