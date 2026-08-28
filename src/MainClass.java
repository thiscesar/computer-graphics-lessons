import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class MainClass {
	public static void main(String[] args) {
		MainCanvas meuCanvas = new MainCanvas();

		JFrame f = new JFrame();
        // talvez separar e não chumbar as dimensões do Canvsa com a janela.
		f.setSize(MainCanvas.WIDTH, MainCanvas.HEIGHT);
		f.setVisible(true);
		f.getContentPane().add(meuCanvas);
		
		f.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent e) {
		        System.exit(0);
		    }
		});
		
		meuCanvas.start();
	}
}