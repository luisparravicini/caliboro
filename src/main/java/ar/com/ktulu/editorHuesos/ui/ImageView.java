package ar.com.ktulu.editorHuesos.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.management.RuntimeErrorException;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.omg.CORBA.RepositoryIdHelper;

public class ImageView extends JPanel {
	private BufferedImage image;
	private List<Dot> dots;

	public ImageView() {
		// try {
		// // Use this if the image exists within the file system
		// image = ImageIO.read(new File("/path/to/image/imageName.png"));
		// // Use this if the image is an embedded resource
		// // image =
		// //
		// ImageIO.read(getClass().getResource("/path/to/resource/imageName.png"));
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//

		dots = new ArrayList<Dot>();
	}

	public void loadImage(String path) {
		try {
			image = ImageIO.read(new File(path));
		} catch (IOException e) {
			// viva la runtime exception
			throw new RuntimeException(e);
		}
		invalidate();
		repaint();
	}

	@Override
	public Dimension getPreferredSize() {
		return (image == null ? super.getPreferredSize() : new Dimension(
				image.getWidth(), image.getHeight()));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (image != null)
			g.drawImage(image, 0, 0, this);

		for (Dot dot : dots)
			g.drawImage(dot.img, dot.pos.x, dot.pos.y, this);
	}

	public void addPoint(int x, int y) {
		try {
			dots.add(new Dot(x, y));
			repaint();
		} catch (IOException e) {
			// viva la runtime exception
			throw new RuntimeException(e);
		}
	}

}

class Dot {
	public BufferedImage img;
	public Point pos;

	public Dot(int x, int y) throws IOException {
		img = ImageIO.read(getClass().getResource("/dot.png"));
		pos = new Point(x,  y);
		
		pos.x -= img.getWidth()/2;
		pos.y -= img.getHeight()/2;
	}
}
