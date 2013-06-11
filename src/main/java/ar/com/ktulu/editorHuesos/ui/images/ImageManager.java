package ar.com.ktulu.editorHuesos.ui.images;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;

import ar.com.ktulu.editorHuesos.BonesStore;
import ar.com.ktulu.editorHuesos.model.BoneImage;
import ar.com.ktulu.editorHuesos.ui.BoneImageTreeNode;
import ar.com.ktulu.editorHuesos.ui.MainFrame;

public class ImageManager {

	public JSlider imageZoom;
	public JLabel imageInfo;
	public ImageView imageView;
	private Dot draggingPoint;
	private MainFrame mainFrame;

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

	private void setImageVisibility(boolean visible) {
		imageView.removeImage();
		imageInfo.setVisible(visible);
		imageZoom.setVisible(visible);
	}

	public void loadBoneImage(BoneImageTreeNode imgNode) {
		if (imgNode.fileExists()) {
			showImage();
			imageView.loadImage(imgNode);
			imageZoom.setValue(100);
			updateImageInfo();
			invalidateImageContainer();
		}
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

	public void mousePressed(int x, int y) {
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
		if (draggingPoint == null)
			draggingPoint = imageView.findDotAt(x, y);

		if (draggingPoint != null) {
			imageView.movePoint(draggingPoint, x, y);
			imageView.repaint();
		}
	}

	public void finishDragging() {
		if (draggingPoint != null)
			BonesStore.getInstance().dirty();

		draggingPoint = null;
	}

}
