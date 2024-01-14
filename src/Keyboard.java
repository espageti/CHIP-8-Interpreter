
import java.awt.event.*;    

public class Keyboard implements KeyListener{

	public int[] keycodes = 
		{
			KeyEvent.VK_X, //0
			KeyEvent.VK_1, //1
			KeyEvent.VK_2, //2
			KeyEvent.VK_3, //3
			KeyEvent.VK_Q, //4
			KeyEvent.VK_W, //5
			KeyEvent.VK_E, //6
			KeyEvent.VK_A, //7
			KeyEvent.VK_S, //8
			KeyEvent.VK_D, //9
			KeyEvent.VK_Z, //A
			KeyEvent.VK_C, //B
			KeyEvent.VK_4, //C
			KeyEvent.VK_R, //D
			KeyEvent.VK_F, //E
			KeyEvent.VK_V, //F
			
		};
	public boolean[] keysHeld = new boolean[16];

	@Override
	public void keyPressed(KeyEvent e) {
		
		for (int i = 0; i < keycodes.length; i++)
		{
			if(e.getKeyCode() == keycodes[i])
			{
				keysHeld[i] = true;
				return;
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		for (int i = 0; i < keycodes.length; i++)
		{
			if(e.getKeyCode() == keycodes[i])
			{
				keysHeld[i] = false;
				return;
			}
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		//err i don't think i gotta do anything here
	}
	
}
