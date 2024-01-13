import java.awt.*;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
public class BasicEnemy2 extends Enemy //shoots and moves
{
	private int attack; //damage
	private int delay; //delay between attacks
	private int moveTick; //tick for deciding when to attack
	private int attackTick; //tick for attacking
	private int xi; //initial x
	private int yi; //initial y
	private int bottom; //limit for bottom
	private Image projectile;
	public BasicEnemy2(int x, int y, int hp, int dx, int dy, int w, int h, Color colour, int atk, int d)
	{
		super(x, y, hp, dx, dy, w, h, colour);
		attack=atk;
		delay=d;
		try
		{
			projectile=ImageIO.read(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\rojectile1.png"));
		}
		catch(IOException e) {}
	}
	public BasicEnemy2(int x, int y, int atk)
	{
		
		super(x, y, 120, (int)(3*Math.random()+1), 0, 60, 40, Color.blue);
		delay=(int)(2000*Math.random()+1000);
		xi=x;
		yi=y;
		attack=atk;
		attackTick=0;
		moveTick=0;
		bottom=(int)(200*Math.random()+320);
		try
		{
			projectile=ImageIO.read(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\enemyProjectile1.png"));
			img=ImageIO.read(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\enemy2.png"));
			regularImg=ImageIO.read(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\enemy2.png"));
			hitImg=ImageIO.read(new File("C:\\Users\\Jc050\\Downloads\\SpaceGame Project\\assets\\enemy2_Hit.png"));
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
	
	public void move(Graphics g, ArrayList<Projectile> pArr)
	{
		if (hitTick>0)
		{
			img=hitImg;
			hitTick-=10;
		}
		else
			img=regularImg;
		moveTick+=10;
		if (visible)
		{
			x+=dx;
			if (x<30)
				dx=-dx;
			else if (x+width>650)
				dx=-dx;
			y+=dy;
		} 
		//attack movement
		if (moveTick>delay || attackTick>0)
		{
			attack(pArr);
		}
		draw(g);
	}
	public void attack(ArrayList<Projectile> pArr)
	{
		attackTick+=10;
		if (y<=bottom)
		{	
			dx=0;
			dy=2;
		}
		else if (attackTick<2500)
		{
			dy=0;
		}
		if (attackTick>1000)
		{
			dy=0;
			dx=0;
			if (attackTick%250==0)
				pArr.add(new Projectile(getX()+getWidth()/2-2, getY()+getHeight(), 0, 6, 15, 15, Color.red, attack, false, projectile));
		}
		if (attackTick>3250)
		{
			dx=0;
			if (y>yi)
				dy=-2;
			else
			{
				dy=0;
				dx=(int)(2*Math.random()+1);
				attackTick=0;
				moveTick=0;
				bottom=(int)(200*Math.random()+320);
				delay=(int)(2000*Math.random()+1000);
			}
		}
	}	
}
