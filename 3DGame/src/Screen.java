import java.util.ArrayList; 
import java.awt.Color; 
/*
 * This class does the actual rendering of the game environment
 * 
 */

public class Screen { 
	public int[][] map; //declare the map array
	public int arrayWidth, arrayHeight, width, height; //map size variables
	public ArrayList<Texture> textures; //arraylist of textures

	public Screen(int[][] m, int mapW, int mapH, ArrayList<Texture> tex, int w, int h) { 
		map = m; //assign the map
		arrayWidth = mapW; //assign the width of the map
		arrayHeight = mapH; //assign the height of the map
		textures = tex; //assign the arraylist of textures
		width = w; //assign the width
		height = h; //assign the height
	} 
/*
 * This method renders the screen, returning an array of the pixels to be displayed to the user
 * 
 */
	public int[] update(Camera camera, int[] pixels) { 
		for(int i = 0; i < pixels.length / 2; i++) { //Background colouring
			if(pixels[i] != Color.DARK_GRAY.getRGB()) {
				pixels[i] = Color.DARK_GRAY.getRGB(); 
			}
		} 
		for(int j = pixels.length / 2; j < pixels.length; j++){ //background colouring
			if(pixels[j] != Color.gray.getRGB()) {
				pixels[j] = Color.gray.getRGB(); 
			}
		} 

		for(int x = 0; x < width; x = x + 1) { //for every column in the screen
			double cameraX = 2 * x / (double)(width) -1; //rendering position
			double rayDirX = camera.xDir + camera.xPlane * cameraX; //direction of ray vector
			double rayDirY = camera.yDir + camera.yPlane * cameraX; //direction of ray vector
			
			int mapX = (int)camera.xPos; //X-coordinate location on the map
			int mapY = (int)camera.yPos; //Y-coordinate location on the map
			//length of ray from current position of camera  to next side 
			double distToSideX; 
			double distToSideY; 
			//Length of ray from edge to edge
			double deltaDistX = Math.sqrt(1 + (rayDirY*rayDirY) / (rayDirX*rayDirX)); 
			double deltaDistY = Math.sqrt(1 + (rayDirX*rayDirX) / (rayDirY*rayDirY)); 
			double perpWallDist; 
			//Direction 
			int stepX, stepY; 
			int lineHeight;
			boolean collision = false; //collision flag
			int side = 0; //Wall orientation
			//Calculate step direction and distance to side of wall 
			if (rayDirX < 0) 
			{ 
				stepX = -1; 
				distToSideX = (camera.xPos - mapX) * deltaDistX; 
			} 
			else 
			{ 
				stepX = 1; 
				distToSideX = (mapX + 1.0 - camera.xPos) * deltaDistX; 
			} 
			if (rayDirY < 0) 
			{ 
				stepY = -1; 
				distToSideY = (camera.yPos - mapY) * deltaDistY; 
			} 
			else 
			{ 
				stepY = 1; 
				distToSideY = (mapY + 1.0 - camera.yPos) * deltaDistY; 
			} 
			//Loop to find where the ray collisions a wall 
			while(!collision) { 
				//Jump to next square 
				if (distToSideX < distToSideY) 
				{ 
					distToSideX += deltaDistX; 
					mapX += stepX; 
					side = 0; 
				} 
				else 
				{ 
					distToSideY += deltaDistY; 
					mapY += stepY; 
					side = 1; 
				} 
				//Check if ray has collision a wall 
				if(map[mapX][mapY] > 0) 
					collision = true; 
			} 
			//Calculate distance to the point of impact 
			if(side == 0) 
				perpWallDist = Math.abs((mapX - camera.xPos + (1 - stepX) / 2) / rayDirX); 
			else 
				perpWallDist = Math.abs((mapY - camera.yPos + (1 - stepY) / 2) / rayDirY);  
			//Now calculate the height of the wall based on the distance from the camera  
			if(perpWallDist > 0) 
				lineHeight = Math.abs((int)(height / perpWallDist)); 
			else 
				lineHeight = height; 
			//calculate lowest and highest pixel to fill in current stripe 
			int drawStart = -lineHeight / 2 + height / 2; 
			if(drawStart < 0) 
				drawStart = 0; 
			int drawEnd = lineHeight / 2 + height / 2; 
			if(drawEnd >= height)  
				drawEnd = height - 1; 
			//add a texture 
			int texNum = map[mapX][mapY] - 1; 
			double wallLocX; //position of collision with wall
			if(side == 1) {//Is the wall on the Y axis?
				wallLocX = (camera.xPos + ((mapY - camera.yPos + (1 - stepY) / 2) / rayDirY) * rayDirX); 
			} else {//Is the wall on the X axis?
				wallLocX = (camera.yPos + ((mapX - camera.xPos + (1 - stepX) / 2) / rayDirX) * rayDirY); 
			} 
			wallLocX -= Math.floor(wallLocX); 
			//X-coordinate on the texture 
			int texX = (int)(wallLocX * (textures.get(texNum).SIZE)); 
			if(side == 0 && rayDirX > 0) texX = textures.get(texNum).SIZE - texX - 1; 
			if(side == 1 && rayDirY < 0) texX = textures.get(texNum).SIZE - texX - 1; 
			//calculate Y-coordinate on texture 
			for(int y = drawStart; y < drawEnd; y++) { 
				int texY = (((y*2 - height + lineHeight) << 6) / lineHeight) / 2; 
				int color; 
				if(side == 0) 
					color = textures.get(texNum).pixels[texX + (texY * textures.get(texNum).SIZE)]; 
				else 
					color = (textures.get(texNum).pixels[texX + (texY * textures.get(texNum).SIZE)] >> 1) & 8355711; //Make y sides darker 
				pixels[x + y * (width)] = color; 
			} 
		} 
		return pixels; //return the pixels to be rendered
	} 
}