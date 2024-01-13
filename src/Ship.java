import java.awt.*;
import java.util.*;
import java.util.*;
public class Ship 
{
	private int x;
	private int y;
	private int w;
	private int h;
	private int dx;
	private int dy;
	private int speed;
	private int health;
	private int potions;
	private int score;
	private int maxHp;
	private Image img;
	private int iTick; //invinciblity frame
	
	public Ship(int x, int y, int width, int height, int s, int hp, Image i)
	{
		this.x=x;
		this.y=y;
		w=width;
		h=height;
		speed=s;
		health=hp;
		score=0;
		maxHp=hp;
		img=i;
		iTick=0;
		potions=1;
	}
	
	//accessors and mutators
	public int getX() {return x;}
	public int getY() {return y;}
	public int getSpeed() {return speed;}
	public int getHealth() {return health;}
	public int getWidth() {return w;}
	public int getHeight() {return h;}
	public int getScore() {return score;}
	public int getMaxHp() {return maxHp;}
	public int getITick() {return iTick;}
	public int getPotions() {return potions;}
	public void setX(int x2) {x=x2;}
	public void setY(int y2) {y=y2;}
	public void setDx(int dx) {this.dx=dx;}
	public void setDy(int dy) {this.dy=dy;}
	public void setSpeed (int s) {speed=s;}
	public void damage(int dmg)
	{
		if (iTick==0)
		{
			health-=dmg;
		}
	}
	//healing mechanic
	public void heal(int h) 
	{
		if (potions>0)
		{
			health+=h;
			potions--;
			if (health>maxHp)
				health=maxHp;
		}
	}
	public void setPotions(int p) {potions=p;}
	public void setScore(int s) {score=s;}
	public void setImage(Image i) {img=i;}
	public void setITick(int i) {iTick=i;}
	public void draw(Graphics g)
	{
		//g.drawImage(img, x, y, null);
		g.drawImage(img, x, y, w, h, null, null);
		//g.setColor(Color.magenta); 
		//g.drawRect(x, y, w, h); hitbox testing
	}
	//using dx and dy for smooth movement 
	public void move()
	{
		x+=dx;
		y+=dy;
		if (iTick!=0)
			iTick-=10;
	}

	
	
	//movement (obsolete)
	/*public void moveRight()
	{
		x+=speed;
	}
	public void moveLeft()
	{
		x-=speed;
	}
	public void moveUp()
	{
		y-=speed;
	}
	public void moveDown()
	{
		y+=speed;
	} */
}
