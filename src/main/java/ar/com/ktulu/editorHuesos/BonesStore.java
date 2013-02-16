package ar.com.ktulu.editorHuesos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import ar.com.ktulu.editorHuesos.model.Bone;
import ar.com.ktulu.editorHuesos.model.BoneImage;
import ar.com.ktulu.editorHuesos.model.BonePoint;

public class BonesStore {
	private static BonesStore instance;

	private List<Bone> data;

	private boolean frozen;

	private BonesStore() {
		data = new ArrayList<Bone>();
	}

	public static BonesStore getInstance() {
		// no es thread-safe
		if (instance == null)
			instance = new BonesStore();
		return instance;
	}

	public void load() {
		File storePath = getStorePath();
		if (!storePath.exists())
			return;

		try {
			freeze();
			
			InputStreamReader reader = new FileReader(storePath);
			JSONArray bones = new JSONArray(new JSONTokener(reader));
			reader.close();

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

	private void addObject(JSONObject obj) throws JSONException {
		Bone bone = new Bone(obj.getString("name"));

		JSONArray images = obj.getJSONArray("images");
		for (int i = 0; i < images.length(); i++) {
			JSONObject imgObj = images.getJSONObject(i);

			BoneImage img = new BoneImage();
			img.setName(imgObj.getString("name"));
			img.setImagePath(imgObj.getString("imagePath"));
			readPoints(imgObj, img);

			bone.addImage(img);
		}

		data.add(bone);
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
		String dir = System.getProperty("user.home");
		return new File(dir, "bones.json");
	}

	public void save() {
		if (frozen)
			return;

		if (data.size() == 0) {
			System.err.println("pum");
			throw new RuntimeException("Sin datos");
		}
		
		JSONArray jsonData = new JSONArray();
		for (Bone bone : data) {
			JSONObject obj = new JSONObject(bone);
			jsonData.put(obj);
		}

		File tmpPath;
		try {
			tmpPath = getTempPath();
			Writer writer = new FileWriter(tmpPath);
			jsonData.write(writer);
			writer.close();

			if (!tmpPath.renameTo(getStorePath()))
				throw new StoreException(
						"No se pudo renombrar el archivo temporario");
		} catch (IOException e) {
			throw new StoreException(e);
		} catch (JSONException e) {
			throw new StoreException(e);
		}
	}

	private File getTempPath() throws IOException {
		return File.createTempFile("bone", "");
	}

	public void add(Bone bone) {
		if (frozen)
			return;

		data.add(bone);
		save();
	}

	public void dirty() {
		save();
	}

	public Iterable<Bone> data() {
		return data;
	}

	public void freeze() {
		frozen = true;
	}

	public void unfreeze() {
		frozen = false;
	}

}
