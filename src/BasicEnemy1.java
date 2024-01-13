import java.awt.*;
import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;
public class BasicEnemy1 extends Enemy //shoots and moves
{
	private int attack; //damage
	private int delay; //delay between shots
	private int moveTick;
	private Image projectile;
	public BasicEnemy1(int x, int y, int hp, int dx, int dy, int w, int h, Color colour, int atk, int d)
	{
		super(x, y, hp, dx, dy, w, h, colour);
		attack=atk;
		delay=d;
		try
		{
			projectile=ImageIO.read(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\enemyProjectile1.png"));
		}
		catch(IOException e) {}
	}
	public BasicEnemy1(int x, int y, int atk)
	{
		super(x, y, 200, -2, 0, 60, 40, Color.blue);
		attack=atk;
		delay=2000;
		try
		{
			projectile=ImageIO.read(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\enemyProjectile1.png"));
			img=ImageIO.read(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\enemy1.png"));
			regularImg=ImageIO.read(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\enemy1.png"));
			hitImg=ImageIO.read(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\enemy1_Hit.png"));
		}
		catch(IOException e) {}
	}
	
	//mutators and accessors
	public int getAttack() {return attack;}
	public int getDelay() {return delay;}
	public int getTick() {return moveTick;}
	public void setTick(int t) {moveTick=t;}
	public void setAttack(int a) {attack=a;}
	public void setDelay(int d) {delay=d;}
	
	//fires projectile with fixed time
	public void shoot(ArrayList<Projectile> pArr)
	{
		moveTick+=10;
		if (moveTick>=delay)
		{
			pArr.add(new Projectile(getX()+getWidth()/2-2, getY()+getHeight(), 0, 4, 10, 15, Color.red, attack, false, projectile));
			moveTick=0;
		}
	}	
}
