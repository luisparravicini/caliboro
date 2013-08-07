package ar.com.ktulu.caliboro;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import ar.com.ktulu.caliboro.model.Bone;

public class Previewer extends BaseExporter {

	public void deploy(List<Bone> data, File basePath)
			throws FileNotFoundException, IOException, URISyntaxException {
		File tmpDir = createPreviewDirectory();
		process(data, basePath);
		deployResources(tmpDir);
		indexPath = deployIndex(tmpDir);
	}

	private File createPreviewDirectory() {
		String tmpDir = System.getProperty("java.io.tmpdir");

		int tries = 0;
		long n = System.currentTimeMillis();
		File dir;
		do {
			dir = new File(tmpDir, String.format("bones_preview_%d", n));
			n++;

			tries++;
			if (tries > 10)
				throw new RuntimeException(
						"Error tratando de generar el directorio de previsualización");
		} while (!dir.mkdirs());

		return dir;
	}

}
