import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

public class Machine {
	private final boolean modern = false;
	private final byte fontLoc = 0x050;
	short PC = 0x200;
    short I = 0x200;
    Stack<Short> stack = new Stack<>();
    byte delayTimer = 0;
    byte soundTimer = 0;
    byte[] variables = new byte[16];
    short pointer = 0x200;
    byte[] memory = new byte[4096];
    

	
	Display d;
	Keyboard k;
	public Machine(Display display, Keyboard keyboard, String fileName) throws IOException
	{
		d = display;
		k = keyboard;
		byte[] program = Files.readAllBytes(new File(fileName).toPath());
		
        
		byte[] font = new byte[]
				{
					(byte)0xF0, (byte)0x90, (byte)0x90, (byte)0x90, (byte)0xF0, // 0
					(byte)0x20, (byte)0x60, (byte)0x20, (byte)0x20, (byte)0x70, // 1
					(byte)0xF0, (byte)0x10, (byte)0xF0, (byte)0x80, (byte)0xF0, // 2
					(byte)0xF0, (byte)0x10, (byte)0xF0, (byte)0x10, (byte)0xF0, // 3
					(byte)0x90, (byte)0x90, (byte)0xF0, (byte)0x10, (byte)0x10, // 4
					(byte)0xF0, (byte)0x80, (byte)0xF0, (byte)0x10, (byte)0xF0, // 5
					(byte)0xF0, (byte)0x80, (byte)0xF0, (byte)0x90, (byte)0xF0, // 6
					(byte)0xF0, (byte)0x10, (byte)0x20, (byte)0x40, (byte)0x40, // 7
					(byte)0xF0, (byte)0x90, (byte)0xF0, (byte)0x90, (byte)0xF0, // 8
					(byte)0xF0, (byte)0x90, (byte)0xF0, (byte)0x10, (byte)0xF0, // 9
					(byte)0xF0, (byte)0x90, (byte)0xF0, (byte)0x90, (byte)0x90, // A
					(byte)0xE0, (byte)0x90, (byte)0xE0, (byte)0x90, (byte)0xE0, // B
					(byte)0xF0, (byte)0x80, (byte)0x80, (byte)0x80, (byte)0xF0, // C
					(byte)0xE0, (byte)0x90, (byte)0x90, (byte)0x90, (byte)0xE0, // D
					(byte)0xF0, (byte)0x80, (byte)0xF0, (byte)0x80, (byte)0xF0, // E
					(byte)0xF0, (byte)0x80, (byte)0xF0, (byte)0x80, (byte)0x80  // F	
				};
		
		for (byte i = 0; i < 80; i++)
		{
			memory[fontLoc + i] = font[i];
		}
        
        
        
        for (byte x : program) {
            memory[pointer] = x;
            //System.out.println(x & 0xff);
            pointer++;
        }
	}
	
	
	
	public void Step()
	{
		System.out.println("advance");
		if(delayTimer > 0)
    	{
    		delayTimer--;
    	}
    	if(soundTimer > 0)
    	{
    		soundTimer--;
    	}
    	byte first = memory[PC];
    	byte second = memory[PC + 1];
    	PC += 2;
    	short instruction=(short)((((first << 8) & 0xffff) | second & 0xff));
    	//System.out.println(first+ " "+  second+ " "+ (instruction & 0xffff));

    	byte nib1 = (byte)((instruction >> 12) & 0xf);
    	byte nib2 = (byte)((instruction >> 8) & 0xf);
    	byte nib3 = (byte)((instruction >> 4) & 0xf);
    	byte nib4 = (byte)(instruction & 0xf);
    	//System.out.println("Nib1: "+ nib1);
    	//System.out.println("Nib2: "+ nib2);
    	//System.out.println("Nib3: "+ nib3);
    	//System.out.println("Nib4: "+ nib4);
    	short NNN = (short)((instruction % 0x1000) & 0xfff);
    	byte NN = (byte)((NNN % 0x100) & 0xff);
		//System.out.println(NNN);
    	switch(nib1)
    	{
    	case 0x0:
    		switch(NNN& 0xfff)
    		{
    		case 0x0E0:
    			d.clearScreen();
    			//System.out.println("Clearing screen");
    			break;
    		case 0x0EE:
    			//System.out.println("Popping " + instruction % 0xffff+ " " + NNN%0xfff);
    			PC = stack.pop();
    			break;
    		}


    		
    		break;
    	case 0x1:
    		//System.out.println("jumping");
    		PC = NNN;
    		break;
    	case 0x2:
    		stack.push(PC);
    		PC = NNN;
    		break;
    	
    	case 0x3:
    		System.out.println("compare " + nib2 + " " + variables[nib2] + " " + NN + " " + instruction);
    		//ok i can literally just  cheat this test by turning off a check
    		if(variables[nib2] %0xff == NN)
    		{
    			PC+= 2;
    		}
    		break;
    	case 0x4:
    		if(variables[nib2]%0xff  != NN)
    		{
    			PC+= 2;
    		}
    		break;
    	case 0x5:
    		if(variables[nib2] == variables[nib3])
    		{
    			PC+= 2;
    		}
    		break;
    	case 0x6:
    		
    		variables[nib2] = (byte)(NN & 0xff); 
    		System.out.println("Set register " +nib2 + " "+  NN + " " + variables[nib2] + " " + instruction);
    		break;
    	case 0x7:
    		//System.out.println("Add register " + nib2 + " "+ NN);
    		
    		variables[nib2] += NN;
    		break;
    	case 0x8:
    		//System.out.println(NNN + " " + nib4 + " " + (instruction & 0xffff) + " " + PC);
    		switch (nib4)
    		{
    		case 0x0:
    			System.out.println("SET");
    			variables[nib2] = variables[nib3];
    			break;
    		case 0x1:
    			variables[nib2] = (byte)(variables[nib2] | variables[nib3]);
    			break;
    		case 0x2:
    			variables[nib2] = (byte)(variables[nib2] & variables[nib3]);
    			break;
    		case 0x3:
    			variables[nib2] = (byte)(variables[nib2] ^ variables[nib3]);
    			break;
    		case 0x4:
    			variables[nib2] = (byte)(variables[nib2] + variables[nib3]);
    			if(variables[nib2] + variables[nib3] > 255)
    			{
    				variables[0xf] = 1;
    			}
    			else
    			{
    				variables[0xf] = 0;
    			}
    			break;
    		case 0x5:
    			//System.out.println("SUBTRACTING " + variables[nib2] + " - " + variables[nib3]);
    			
    			if(variables[nib2] < variables[nib3])
    			{
    				variables[0xf] = 0;
    			}
    			else
    			{
    				variables[0xf] = 1;
    			}
    			variables[nib2] = (byte)(variables[nib2] - variables[nib3]);
    			//System.out.println("RESULT " + " "+ variables[nib2] +  " ");
    			break;
    		case 0x7:
    			if(variables[nib3] < variables[nib2])
    			{
    				variables[nib2] = (byte)(256 + variables[nib3] - variables[nib2]);
    				variables[0xf] = 0;
    			}
    			else
    			{
    				variables[nib2] = (byte)(variables[nib3] - variables[nib2]);
    				variables[0xf] = 1;
    			}
    			//System.out.println("RESULT " + variables[nib2] +  " ");
    			break;
    		case 0x6:
    			if(!modern)
    			{
    				variables[nib2] = variables[nib3];
    			}
    			variables[0xf] = (byte)(variables[nib2] % 0b10);
    			variables[nib2] >>= 1;
    			
    			break;
			case 0xE:
    			if(!modern)
    			{
    				variables[nib2] = variables[nib3];
    			}
    			variables[0xf] = (byte)((variables[nib2] >> 3) & 0b1);
    			variables[nib2] <<= 1;
    			
    			break;
    		}
    		
    		break;
    	case 0x9:
    		if(variables[nib2] != variables[nib3])
    		{
    			PC+= 2;
    		}
    		break;
    	case 0xA:
    		//System.out.println("SET I " + NNN);
    		I = NNN;
    		break;
    	case 0xB:
    		if(!modern)
    		{
    			PC = (short)(NNN & 0xfff + (variables[0] & 0xff));
    		}
    		else
    		{
    			PC = (short)(NNN & 0xfff + (variables[nib2] & 0xff));
    		}
    	case 0xD:
    		byte X = (byte)(variables[nib2] % 64);
    		byte Y = (byte)(variables[nib3] % 32);
    		byte N = nib4;
    		variables[0xf] = 0;
    		for (int row = 0; row < N; row++)
    		{
    			X = (byte)(variables[nib2] % 64);
    			byte spritebyte = (byte)(memory[I + row] & 0b11111111);
    			for (int col = 0; col < 8; col++)
    			{
    				
    				if(X >= d.SCREEN_WIDTH)
        			{
        				break;
        			}
    				boolean on = (((spritebyte & 0b11111111) >> 7-col)% 0b10) == 1;
    				if(on)
    				{
    					if(d.getPixel(X, Y))
    					{
    						d.setPixel(X, Y, false);
    						variables[0xf] = 1;
    					}
    					else
    					{
    						d.setPixel(X, Y, true);
    					}
    					
    				}
    				X++;
    			}
    			Y++;
    			if(Y >= d.SCREEN_HEIGHT)
    			{
    				break;
    			}
    			////System.out.println("Draw " + X + " " + Y + " " + (spritebyte& 0b11111111));
    		}
    		
    		break;
    	case 0xE:
    		switch(NN)
    		{
    		//for some reason i need to cast to byte? I guess it's large enough that it thinks it's an int??
    		case (byte)0x9e:
    			if(k.keysHeld[nib2])
    			{
    				PC += 2;
    			}
    			break;
    		case (byte)0xA1:
    			if(!k.keysHeld[nib2])
    			{
    				PC += 2;
    			}
    			break;
    		
    		}
    	case 0xF:
    		switch(NN)
    		{
    		case 0x1E:
    			if(I + variables[nib2] > 0x1000)
    			{
    				variables[0xF] = 1;
    			}
    			I += variables[nib2];
    			break;
    		case 0x0A:
    			boolean held = false;
    			for (byte i = 0; i < k.keysHeld.length; i++)
    			{
    				if(!k.keysHeld[nib2])
        			{
    					variables[nib2] = i;
            			held = true;
            			break;
        			}
    			}
    			if(!held)
    			{
    				PC-=2;
    			}
    			break;
    		case 0x07:
    			variables[nib2] = delayTimer;
    			break;
    		case 0x15:
    			delayTimer = variables[nib2];
    		case 0x18:
    			soundTimer = variables[nib2];
    		case 0x29:
    			I = (short)(fontLoc + nib2 * 5);
    			break;
    		case 0x33:
    			//uhhh if i don't do this it might be negative, since byte also includes negative in java 
    			short num = (short)(variables[nib2] & 0xff);
    			memory[I] = (byte)(num/100);
    			memory[I + 1] = (byte)((num%100)/10);
    			memory[I + 2] = (byte)(num%10); 
    			System.out.println(variables[nib2] + " " + num + " " + memory[I] + " " + memory[I+1] + " " + memory[I+2]);
    			break;
    		case 0x55:
    			for (short i = 0; i <= nib2; i++)
    			{
    				memory[I + i] = variables[i];
    			}
    			break;
    		case 0x65:
    			for (short i = 0; i <= nib2; i++)
    			{
    				variables[i] = memory[I + i];
    			}
    			break;
    		}
    		break;
    	}
    	d.update(d.getGraphics());
	}
	
	public byte[] getVariables()
	{
		return variables;
	}
}
