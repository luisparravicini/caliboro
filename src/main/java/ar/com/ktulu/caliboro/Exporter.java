package ar.com.ktulu.caliboro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.commons.io.IOUtils;

import ar.com.ktulu.caliboro.model.Bone;
import ar.com.ktulu.caliboro.model.BoneImage;

public class Exporter extends BaseExporter {

	public void export(File exportPath, List<Bone> data, File basePath)
			throws FileNotFoundException, IOException, URISyntaxException {
		createExportPath(exportPath);
		process(data, null);
		deployResources(exportPath);
		indexPath = deployIndex(exportPath);
		copyImages(data, basePath, exportPath);
	}

	private void copyImages(List<Bone> data, File basePath, File exportPath) {
		try {
			for (Bone bone : data) {
				for (BoneImage image : bone.getImages()) {
					InputStream in = new FileInputStream(
							image.imageAbsolutePath());
					OutputStream out = new FileOutputStream(new File(
							exportPath, image.getImagePath().getName()));
					IOUtils.copy(in, out);
					in.close();
					out.close();
				}
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void createExportPath(File exportPath) {
		if (!exportPath.exists())
			if (!exportPath.mkdirs())
				throw new RuntimeException(
						"No se pudo crear la carpeta de exportación");
	}

}
