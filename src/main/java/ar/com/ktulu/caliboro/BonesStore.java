package ar.com.ktulu.caliboro;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import ar.com.ktulu.caliboro.model.Bone;
import ar.com.ktulu.caliboro.model.BoneImage;
import ar.com.ktulu.caliboro.model.BonePoint;
import ar.com.ktulu.caliboro.model.BoneScale;
import ar.com.ktulu.caliboro.model.StoreRootNode;

public class BonesStore {
	private static BonesStore instance;

	private StoreRootNode data;
	private boolean frozen;

	private File storeBasePath;

	private BonesStore() {
		data = createRootNode();
	}

	private StoreRootNode createRootNode() {
		return new StoreRootNode();
	}

	public static BonesStore getInstance() {
		// no es thread-safe
		if (instance == null)
			instance = new BonesStore();
		return instance;
	}

	public void load() {
		File bonesPath = getBonesPath();
		if (!bonesPath.exists())
			return;

		try {
			freeze();

			InputStreamReader reader = new FileReader(bonesPath);
			JSONObject auxData = new JSONObject(new JSONTokener(reader));
			reader.close();

			data = createRootNode();
			data.setLastImageId(auxData.getInt("lastImageId"));
			data.clearBones();

			JSONArray bones = auxData.getJSONArray("bones");
			for (int i = 0; i < bones.length(); i++)
				addObject(bones.getJSONObject(i));
		} catch (FileNotFoundException e) {
			throw new StoreException(e);
		} catch (JSONException e) {
			throw new StoreException(e);
		} catch (IOException e) {
			throw new StoreException(e);
		} finally {
			unfreeze();
		}
	}

	private File getBonesPath() {
		File dir = getStorePath();
		return new File(dir, "bones.json");
	}

	private void addObject(JSONObject obj) throws JSONException {
		Bone bone = new Bone(obj.getString("name"));

		JSONArray images = obj.getJSONArray("images");
		for (int i = 0; i < images.length(); i++) {
			JSONObject imgObj = images.getJSONObject(i);

			BoneImage img = new BoneImage(bone);
			img.setName(imgObj.getString("name"));
			img.setImagePath(imgObj.getString("imagePath"));
			readScale(imgObj, img);
			readPoints(imgObj, img);

			bone.addImage(img);
		}

		data.addBone(bone);
	}

	private void readScale(JSONObject imgObj, BoneImage img)
			throws JSONException {
		// TODO esto es solo de transicion por los datos cargados que existen
		// sin este dato
		if (!imgObj.has("scale") || imgObj.isNull("scale")) {
			BoneScale scale = new BoneScale();
			scale.setDistance(100);
			scale.setAx(400);
			scale.setAy(400);
			scale.setBx(900);
			scale.setBy(400);
			img.setScale(scale);
		} else {
			JSONObject obj = imgObj.getJSONObject("scale");
			BoneScale scale = new BoneScale();
			scale.setDistance(obj.getInt("distance"));
			scale.setAx(obj.getInt("ax"));
			scale.setAy(obj.getInt("ay"));
			scale.setBx(obj.getInt("bx"));
			scale.setBy(obj.getInt("by"));
			img.setScale(scale);
		}
	}

	private void readPoints(JSONObject imgObj, BoneImage img)
			throws JSONException {
		JSONArray points = imgObj.getJSONArray("points");
		for (int i = 0; i < points.length(); i++) {
			JSONObject pointObj = points.getJSONObject(i);

			BonePoint point = new BonePoint();
			point.name = pointObj.getString("name");
			point.x = pointObj.getInt("x");
			point.y = pointObj.getInt("y");

			img.addPoint(point);
		}
	}

	private File getStorePath() {
		File dir = storeBasePath;
		if (dir == null)
			dir = new File(FileUtils.getUserDirectory(), "bones");
		File storePath = dir;

		if (!storePath.exists()) {
			if (!storePath.mkdirs())
				throw new StoreException("No se pudo crear " + storePath);
		} else if (!storePath.isDirectory())
			throw new StoreException("No es un directorio" + storePath);

		return storePath;
	}

	public void save() {
		if (frozen)
			return;

		try {
			copyBoneImages();

			JSONObject jsonData = dumpDataAsJson();
			File tmpPath = getBonesTempPath();
			Writer writer = new FileWriter(tmpPath);
			jsonData.write(writer);
			writer.close();

			renameFile(tmpPath, getBonesPath());
		} catch (IOException e) {
			throw new StoreException(e);
		} catch (JSONException e) {
			throw new StoreException(e);
		}
	}

	private void copyBoneImages() throws IOException {
		for (Bone bone : data.getBones())
			for (BoneImage image : bone.getImages())
				image.relocate(getStorePath());
	}

	private JSONObject dumpDataAsJson() {
		return new JSONObject(data);
	}

	private void renameFile(File tmpPath, File storePath) throws IOException {
		// TODO en algunos Windows (no se que version son) File.renameTo me
		// falla siempre.
		// tmpPath.renameTo(getStorePath()));

		File backupPath = new File(storePath.getCanonicalPath() + ".old");
		if (backupPath.exists() && !backupPath.delete())
			throw new StoreException("No se pudo borrar el archivo de backup");

		if (storePath.exists() && !storePath.renameTo(backupPath))
			throw new StoreException(
					"No se pudo renombrar el archivo de backup");

		if (!tmpPath.renameTo(storePath))
			throw new StoreException(
					"No se pudo renombrar el archivo temporario");
	}

	private File getBonesTempPath() throws IOException {
		// TODO en algunos Windows (no se que version son) el usar un archivo
		// creado por createTempFile hace que el renameTo siguiente falle.
		// return File.createTempFile("bone", "");

		return new File(getBonesPath() + ".tmp");
	}

	public void add(Bone bone) {
		if (frozen)
			return;

		data.addBone(bone);
		save();
	}

	public void dirty() {
		save();
	}

	public Iterable<Bone> data() {
		return data.getBones();
	}

	public void freeze() {
		frozen = true;
	}

	public void unfreeze() {
		frozen = false;
	}

	public void remove(Bone bone) {
		data.removeBone(bone);
		save();
	}

	/**
	 * Asume que en el contexto donde se llama a este metodo se va a guardar
	 * luego.
	 * 
	 * @return
	 */
	public int nextImageId() {
		return data.nextImage();
	}

	public void setPath(File path) {
		storeBasePath = path.getAbsoluteFile();
	}

	public File getPath() {
		return storeBasePath;
	}

	public StoreRootNode dataNode() {
		return data;
	}

	public void moveBone(Bone bone, int index) {
		data.moveBone(bone, index);
	}

	public void moveBoneLast(Bone bone) {
		data.moveBoneLast(bone);
	}

	public void moveImage(Bone sourceBone, BoneImage image, Bone newParent,
			int childIndex) {
		data.moveImage(sourceBone, image, newParent, childIndex);
	}

	public void moveImage(Bone sourceBone, BoneImage sourceImage, Bone newParent) {
		data.moeImage(sourceBone, sourceImage, newParent);
	}
}
