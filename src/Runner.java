
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Stack;

import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;



public class Runner{
	
	static boolean debug = true;
	static final int advanceKey = KeyEvent.VK_ENTER;
	static Machine virtual;
    static JFrame frame = new JFrame("CHIP-8");
    static JSplitPane splitPane;
	static Display display = new Display();
	static Keyboard k = new Keyboard();
	
	
	/*
	 * DebugGUI
	 */
	static JPanel debugPanel;
	static JLabel varsLabel;
	public static void main(String[] args) throws IOException, InterruptedException
	{
		
		frame.addKeyListener(k);
        if(debug)
        {
        	debugPanel = new JPanel();
        	varsLabel = new JLabel();
        	varsLabel.setText("DWAOIJ");
        	debugPanel.add(varsLabel);
        	debugPanel.setSize(300, 300);
        	DebugTools debugListen = new DebugTools();
            frame.addKeyListener(debugListen);
        }
        
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        splitPane = new JSplitPane(SwingConstants.VERTICAL, display, debugPanel);
        splitPane.setOrientation(SwingConstants.VERTICAL);
        frame.getContentPane().add(splitPane);
        frame.setSize(300 + display.PIXEL_SIZE * display.SCREEN_WIDTH, display.PIXEL_SIZE * display.SCREEN_HEIGHT);
        
        
        frame.setVisible(true);

        
        
        
        splitPane.setDividerLocation(display.PIXEL_SIZE * display.SCREEN_WIDTH);
        
        
		virtual = new Machine(display, k, "3-corax+.ch8");
		if(!debug)
		{
			while(true) {
				virtual.Step();
				TimeUnit.MILLISECONDS.sleep(10);
			}
		}
	}
	
	public static class DebugTools implements KeyListener 
	{
		
		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		
		long step = 0;
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == advanceKey)
			{
				System.out.print(step);
				 
				virtual.Step();
				step++;
			}
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}


}
