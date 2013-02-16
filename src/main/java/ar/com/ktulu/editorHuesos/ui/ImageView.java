package ar.com.ktulu.editorHuesos.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import ar.com.ktulu.editorHuesos.model.BonePoint;

@SuppressWarnings("serial")
public class ImageView extends JPanel {
	private BufferedImage image;
	private List<Dot> dots;

	public ImageView() {
		dots = new ArrayList<Dot>();
	}

	public void loadImage(BoneImageTreeNode imgNode) {
		try {
			image = ImageIO.read(new File(imgNode.getImagePath()));

			List<Dot> newDots = new ArrayList<Dot>();
			for (BonePoint point : imgNode.getPoints())
				newDots.add(new Dot(point));
			dots = newDots;
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
	public String name;

	public Dot(int x, int y) throws IOException {
		this();
		pos.x = x;
		pos.y = y;

		pos.x -= img.getWidth() / 2;
		pos.y -= img.getHeight() / 2;
	}

	public Dot() throws IOException {
		img = ImageIO.read(getClass().getResource("/dot.png"));
		pos = new Point(0, 0);
	}

	public Dot(BonePoint point) throws IOException {
		this(point.x, point.y);
		name = point.name;
	}
}
