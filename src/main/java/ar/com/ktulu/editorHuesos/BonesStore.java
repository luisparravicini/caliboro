package ar.com.ktulu.editorHuesos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

public class BonesStore {
	private static BonesStore instance;

	private BonesStore() {
	}

	public static BonesStore getInstance() {
		// no es thread-safe
		if (instance == null)
			instance = new BonesStore();
		return instance;
	}

	public void load() {
		String dir = System.getProperty("user.home");
		File file = new File(dir, "bones.json");

		if (file.exists())
			try {
				InputStreamReader reader = new FileReader(file);
				JSONArray bones = (JSONArray) JSONValue.parse(reader);
				System.out.println(bones);
			} catch (FileNotFoundException e) {
				throw new StoreException(e);
			}
	}

	public void save() {

	}

}
