package ar.com.ktulu.editorHuesos.model;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import ar.com.ktulu.editorHuesos.BonesStore;

public class BoneImage {
	private String name;
	private File imagePath;
	private List<BonePoint> points;
	private Bone parent;
	private int imageId;

	public BoneImage(Bone parent, String path) {
		this(parent);
		imagePath = new File(path);
		name = imagePath.getName();
		this.imageId = BonesStore.getInstance().nextImageId();
	}

	public BoneImage(Bone parent) {
		this.parent = parent;
		points = new ArrayList<BonePoint>();
	}

	public void setImagePath(String path) {
		this.imagePath = new File(path);
	}

	public String getName() {
		return name;
	}

	public File getImagePath() {
		return imagePath;
	}

	public List<BonePoint> getPoints() {
		return points;
	}

	public String toString() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// TODO no tiene 'get' para que en la serializacion a json no quede como un
	// atributo de este objeto. No veo algo más simple de hacer en la doc de org.json
	public String imageAbsolutePath() {
		return imagePath.getAbsolutePath();
	}

	public void addPoint(BonePoint point) {
		points.add(point);

		BonesStore.getInstance().dirty();
	}

	public BonePoint addPoint(String name, int x, int y) {
		BonePoint point = new BonePoint(name, x, y);
		addPoint(point);

		return point;
	}

	public void remove(BonePoint point) {
		if (point == null)
			return;

		points.remove(point);
		BonesStore.getInstance().dirty();
	}

	public void removeFromParent() {
		parent.remove(this);
	}

	public void relocate(File storePath) throws IOException {
		if (imagePath.getParent() != null) {
			Reader reader = null;
			Writer writer = null;
			try {
				String newImageName = String.format("%d.%s", imageId,
						FilenameUtils.getExtension(imagePath.getName()));
				File newImagePath = new File(storePath, newImageName);
				if (newImagePath.exists())
					throw new IOException(String.format(
							"El archivo %s no deberia existir", newImagePath));
				reader = new FileReader(imagePath);
				writer = new FileWriter(newImagePath);
				IOUtils.copy(reader, writer);

				imagePath = new File(newImagePath.getName());
			} finally {
				if (reader != null)
					IOUtils.closeQuietly(reader);
				if (writer != null)
					IOUtils.closeQuietly(writer);
			}
		}
	}
}
