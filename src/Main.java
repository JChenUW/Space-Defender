import javax.swing.JFrame;
import java.util.*;
import java.awt.*;
public class Main
{
	public static void main(String[] args)
	{
		JFrame frame=new JFrame();
		SpaceGame game=new SpaceGame();
		frame.setBounds(0, 0, 700, 850);
		frame.setTitle("Secret Game");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(game);
		frame.setVisible(true);

	}
}
