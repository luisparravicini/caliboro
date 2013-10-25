package ar.com.ktulu.caliboro.ui.images;

import java.awt.Cursor;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;

import ar.com.ktulu.caliboro.BonesStore;
import ar.com.ktulu.caliboro.model.BoneImage;
import ar.com.ktulu.caliboro.ui.MainFrame;
import ar.com.ktulu.caliboro.ui.treeModel.BoneImageTreeNode;

public class ImageManager {
	public JSlider imageZoom;
	public JLabel imageInfo;
	public ImageView imageView;
	private Dot draggingPoint;
	private MainFrame mainFrame;
	private boolean visible;
	private Cursor moveCursor = new Cursor(Cursor.HAND_CURSOR);
	private boolean draggingPointA;
	private boolean draggingPointB;

	public ImageManager(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public void sliderChangedValue() {
		if (imageZoom.getValueIsAdjusting())
			return;

		imageView.setZoom(imageZoom.getValue());
		updateImageInfo();
		invalidateImageContainer();
	}

	private void updateImageInfo() {
		String msg = null;
		if (imageView.hasImage())
			msg = String.format("Imagen: %dx%d   Zoom: %d%%",
					imageView.getImageWidth(), imageView.getImageHeight(),
					imageView.getZoom());

		imageInfo.setText(msg);
	}

	public void hideImage() {
		setImageVisibility(false);
	}

	private void showImage() {
		setImageVisibility(true);
	}

	public boolean isVisible() {
		return visible;
	}

	private void setImageVisibility(boolean visible) {
		this.visible = visible;
		imageView.setVisible(visible);
		imageInfo.setVisible(visible);
		imageZoom.setVisible(visible);
	}

	public void loadBoneImage(BoneImageTreeNode imgNode) throws ImageException {
		if (imgNode.fileExists()) {
			imageView.loadImage(imgNode, imageZoom);
			showImage();
			updateImageInfo();
			invalidateImageContainer();
		} else
			hideImage();
	}

	private void invalidateImageContainer() {
		JComponent parent = (JComponent) imageView.getParent();
		parent.revalidate();
		parent.repaint();
	}

	private String userInputsPointName() {
		String name;
		do {
			name = (String) JOptionPane.showInputDialog(mainFrame, "Nombre",
					null, JOptionPane.PLAIN_MESSAGE, null, null, null);
			if (name == null)
				return null;

			name = name.trim();

			if (name.isEmpty())
				JOptionPane.showMessageDialog(mainFrame,
						"El nombre no puede ser vacío");
		} while (name.isEmpty());

		return name;
	}

	public void mouseClicked(int x, int y) {
		BoneImage img = mainFrame.getImageSelected();
		if (img == null)
			return;

		if (!imageView.isInsideImage(x, y))
			return;

		if (mainFrame.isAddingPoints()) {
			String name = userInputsPointName();
			if (name == null)
				return;

			imageView.addPoint(img.addPoint(name, x, y));
		} else {
			Dot dot = imageView.findDotAt(x, y);
			if (dot != null) {
				imageView.remove(dot);
				img.remove(dot.point);
			}
		}
	}

	public void mouseDragged(int x, int y) {
		if (isDraggingMeasure())
			dragScale(x, y);
		else
			dragPoint(x, y);
	}

	private void dragScale(int x, int y) {
		if (isDraggingMeasure()) {
			if (draggingPointA)
				imageView.moveMeasureEndA(x, y);
			else
				imageView.moveMeasureEndB(x, y);
		}
	}

	private boolean isDraggingMeasure() {
		return draggingPointA || draggingPointB;
	}

	private void dragPoint(int x, int y) {
		if (draggingPoint == null)
			draggingPoint = imageView.findDotAt(x, y);

		if (draggingPoint != null) {
			imageView.movePoint(draggingPoint, x, y);
			imageView.repaint();
		}
	}

	public void finishDragging() {
		if (draggingPoint != null || isDraggingMeasure())
			BonesStore.getInstance().dirty();

		draggingPoint = null;
	}

	public void mouseMoved(int x, int y) {
		if (imageView.isInsideScaleEnd(x, y))
			imageView.setCursor(moveCursor);
		else
			imageView.setCursor(Cursor.getDefaultCursor());
	}

	public void mousePressed(int x, int y) {
		draggingPointA = imageView.isInsideScaleEndA(x, y);
		draggingPointB = imageView.isInsideScaleEndB(x, y);
	}

}
