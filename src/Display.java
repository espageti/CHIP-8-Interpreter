import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Display extends JPanel {
	
	public int PIXEL_SIZE = 15;
	//screen width and height in pixels
	public int SCREEN_WIDTH = 64;
	public int SCREEN_HEIGHT = 32;
	
//stores whether 
	private boolean[][] pixels = new boolean[SCREEN_WIDTH][SCREEN_HEIGHT];
	BufferedImage img;
	public void paint(Graphics g) {
		BufferedImage img = new BufferedImage(SCREEN_WIDTH * PIXEL_SIZE,SCREEN_HEIGHT * PIXEL_SIZE
				,BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < img.getWidth(); x++)
		{
			for (int y = 0; y < img.getHeight(); y++)
			{
				if(pixels[x/PIXEL_SIZE][y/PIXEL_SIZE])
				{
					img.setRGB(x, y, 0x329321);
				}
				else
				{
					img.setRGB(x,  y, 0);
				}
				
			}
		}
		g.drawImage(img, 0, 0,this);
	}
	
	
	void setPixel(int x, int y, boolean on)
	{
		
		pixels[x][y] = on;
	}
	
	boolean getPixel(int x, int y)
	{
		return (pixels[x][y] == true);
	}
	void clearScreen()
	{
		for (int x = 0; x < SCREEN_WIDTH; x++)
		{
			for (int y = 0; y < SCREEN_HEIGHT; y++)
			{
				pixels[x][y] = false;
				
			}
		}
	}
   
}