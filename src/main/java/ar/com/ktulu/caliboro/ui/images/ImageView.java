package ar.com.ktulu.caliboro.ui.images;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JSlider;

import ar.com.ktulu.caliboro.model.BonePoint;
import ar.com.ktulu.caliboro.ui.treeModel.BoneImageTreeNode;

@SuppressWarnings("serial")
public class ImageView extends JPanel {
	private BufferedImage image;
	private BufferedImage scaledImage;
	private int zoom;
	private List<Dot> dots;

	public ImageView() {
		setup();
	}

	private void setup() {
		image = scaledImage = null;
		dots = new ArrayList<Dot>();
		zoom = 100;
	}

	public void loadImage(BoneImageTreeNode imgNode, JSlider zoomSlider) throws ImageException {
		try {
			image = ImageIO.read(new File(imgNode.getImagePath()));
			findProperZoomLevel(zoomSlider);
			updateZoomedImage();

			List<Dot> newDots = new ArrayList<Dot>();
			for (BonePoint point : imgNode.getPoints())
				newDots.add(new Dot(point));
			dots = newDots;
		} catch (IIOException e) {
			throw new ImageException(e);
		} catch (IOException e) {
			// viva la runtime exception
			throw new RuntimeException(e);
		}

		invalidate();
		repaint();
	}

	private void findProperZoomLevel(JSlider zoomSlider) {
		float zoomLevel = 1;
		float step = zoomSlider.getMajorTickSpacing() / 100.0f;
		float minZoomLevel = zoomSlider.getMinimum() / 100.0f;
		int width = getWidth();
		int newWidth;
		boolean found;
		do {
			newWidth = (int) (image.getWidth() * zoomLevel);
			found = (newWidth <= width || zoomLevel <= minZoomLevel);
			if (!found)
				zoomLevel -= step;
		} while (!found);

		zoom = (int) (zoomLevel * 100);
		zoomSlider.setValue(zoom);
		System.out.println(zoom);
	}

	private void updateZoomedImage() {
		if (image == null)
			scaledImage = null;
		else {
			float zoomLevel = getZoomLevel();
			int newImageWidth = (int) (image.getWidth() * zoomLevel);
			int newImageHeight = (int) (image.getHeight() * zoomLevel);
			BufferedImage resizedImage = new BufferedImage(newImageWidth,
					newImageHeight, image.getType());
			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(image, 0, 0, newImageWidth, newImageHeight, null);
			g.dispose();

			scaledImage = resizedImage;
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return (!isVisible() || scaledImage == null ? super.getPreferredSize()
				: new Dimension(scaledImage.getWidth(), scaledImage.getHeight()));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (scaledImage != null)
			g.drawImage(scaledImage, 0, 0, this);

		float zoomLevel = getZoomLevel();
		for (Dot dot : dots) {
			int x = (int) (dot.pos.x * zoomLevel);
			int y = (int) (dot.pos.y * zoomLevel);

			if (dot.getName() != null) {
				g.drawString(dot.nameAttrs.getIterator(), x,
						(int) (y + dot.img.getHeight() * 2.5));
			}
			g.drawImage(dot.img, x, y, this);
		}
	}

	public void addPoint(BonePoint point) {
		try {
			point.x /= getZoomLevel();
			point.y /= getZoomLevel();

			dots.add(new Dot(point));
			repaint();
		} catch (IOException e) {
			// viva la runtime exception
			throw new RuntimeException(e);
		}
	}

	public List<Dot> getDots() {
		return dots;
	}

	public void remove(Dot dot) {
		dots.remove(dot);
		repaint();
	}

	public void setZoom(int value) {
		zoom = value;
		updateZoomedImage();
	}

	private float getZoomLevel() {
		return zoom / 100f;
	}

	public int getZoom() {
		return zoom;
	}

	public boolean hasImage() {
		return (image != null);
	}

	public int getImageWidth() {
		return (image != null ? image.getWidth() : 0);
	}

	public int getImageHeight() {
		return (image != null ? image.getHeight() : 0);
	}

	public boolean isInsideImage(int x, int y) {
		return scaledImage != null && x >= 0 && y >= 0
				&& x < scaledImage.getWidth() && y < scaledImage.getHeight();
	}

	public void removeImage() {
		setup();
	}

	public Dot findDotAt(int x, int y) {
		boolean init = false;
		int d;
		Rectangle hitBox = null;

		float zoomLevel = getZoomLevel();
		Point pos = unzoomPoint(x, y);
		for (Dot point : getDots()) {
			if (!init) {
				init = true;
				d = (int) (point.img.getWidth() / zoomLevel);
				hitBox = new Rectangle(pos.x - d, pos.y - d, d * 2, d * 2);
			}
			if (hitBox.contains(point.pos)) {
				System.out.println(pos.x - point.pos.x);
				System.out.println(pos.y - point.pos.y);

				return point;
			}
		}

		return null;
	}

	private Point unzoomPoint(int x, int y) {
		float zoomLevel = getZoomLevel();
		Point pos = new Point(x, y);
		pos.x /= zoomLevel;
		pos.y /= zoomLevel;

		return pos;
	}

	public void movePoint(Dot dot, int x, int y) {
		Point pos = unzoomPoint(x, y);
		dot.setPos(pos.x, pos.y);
	}

}

class Dot {
	public BufferedImage img;
	public Point pos;
	public BonePoint point;
	public AttributedString nameAttrs;

	private Dot(int x, int y) throws IOException {
		this();
		pos.x = x;
		pos.y = y;

		pos.x -= img.getWidth() / 2;
		pos.y -= img.getHeight() / 2;
	}

	private Dot() throws IOException {
		img = ImageIO.read(getClass().getResource("/dot.png"));
		pos = new Point(0, 0);
	}

	public Dot(BonePoint point) throws IOException {
		this(point.x, point.y);
		this.point = point;
		nameAttrs = new AttributedString(point.getName());
		nameAttrs.addAttribute(TextAttribute.BACKGROUND, Color.GRAY);
		nameAttrs.addAttribute(TextAttribute.FOREGROUND, Color.WHITE);
	}

	public void setPos(int x, int y) {
		pos.x = x;
		pos.y = y;
		if (point != null) {
			point.x = x;
			point.y = y;
		}
	}

	public String getName() {
		return point.getName();
	}
}
