import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
public class Enemy
{
	protected int x;
	protected int y;
	protected int health;
	protected int dx;
	protected int dy;
	protected int width;
	protected int height;
	protected Color c;
	protected boolean visible;
	protected BufferedImage img;
	protected BufferedImage regularImg;
	protected BufferedImage hitImg;
	protected int hitTick;
	public Enemy(int x, int y, int hp, int dx, int dy, int w, int h, Color colour)
	{
		this.x=x;
		this.y=y;
		this.dx=dx;
		this.dy=dy;
		health=hp;
		c=colour;	
		width=w;
		height=h;
		visible=true;
		img=null;
		regularImg=null;
		hitImg=null;
		hitTick=0;
	}
	
	//accessors 
	public int getX() {return x;}
	public int getY() {return y;}
	public int getDx() {return dx;}
	public int getDy() {return dy;}
	public int getWidth() {return width;}
	public int getHeight() {return height;}
	public int getHealth() {return health;}
	public boolean getVisible() {return visible;}
	//mutators
	public void setX(int x2) {x=x2;}
	public void setY(int y2) {y=y2;}
	public void setDx(int dx) {this.dx=dx;}
	public void setDy(int dy) {this.dy=dy;}
	public void setColor(Color c) {this.c=c;}
	public void damage(int dmg) 
	{
		health-=dmg;
		hitTick=100;
	}
	public void destroy()
	{visible=false;}
	
	public void draw(Graphics g)
	{
		if (hitTick>0)
		{
			img=hitImg;
			hitTick-=10;
		}
		else
			img=regularImg;
		if(visible)
		{
			g.drawImage(img, x, y, width, height, null, null);
			/* g.setColor(c);
			g.drawRect(x+5, y+3, width-5, height-7); hitbox testing */
		}
	}
	public void move(Graphics g)
	{
		if (visible)
		{
			x+=dx;
			if (x<30)
				dx=-dx;
			else if (x+width>650)
				dx=-dx;
			y+=dy;
		}
		draw(g);
	}
	public void collide(Ship s)
	{
		Rectangle e=new Rectangle(x+5, y+3, width-5, height-7);
		Rectangle ship=new Rectangle(s.getX(), s.getY(), s.getWidth(), s.getHeight());
		if (e.intersects(ship))
		{
			s.damage(50);
			if (s.getITick()==0)
			{
				s.setITick(500);
				destroy();
			}
		}
	}
}
