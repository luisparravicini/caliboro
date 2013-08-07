package ar.com.ktulu.caliboro;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.json.JSONArray;
import org.json.JSONObject;

import ar.com.ktulu.caliboro.model.Bone;
import ar.com.ktulu.caliboro.model.BoneImage;
import ar.com.ktulu.caliboro.ui.Util;

public class Exporter {

	private String indexContent;
	private String indexPath;

	// TODO esto esta publico para el test
	public String process(List<Bone> data, File basePath) throws IOException {
		if (data == null)
			throw new IllegalArgumentException("Faltan datos");

		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class",
				ClasspathResourceLoader.class.getName());
		ve.init();
		Template template = ve.getTemplate("templates/index.html.vm", "utf-8");
		VelocityContext context = new VelocityContext();
		context.put("bonesData", getDataAsJSON(data));
		context.put("bonesBasePath",
				Util.encodePath(basePath.getAbsolutePath()));
		StringWriter writer = new StringWriter();
		template.merge(context, writer);

		indexContent = writer.toString();

		return null;
	}

	private Object getDataAsJSON(List<Bone> data) {
		return new JSONArray(CollectionUtils.collect(data, new Transformer() {
			@Override
			public Object transform(Object obj) {
				return new JSONObject(obj);
			}
		}));
	}

	protected String getIndexContent() {
		return indexContent;
	}

	public void export(File exportPath, List<Bone> data, File basePath)
			throws FileNotFoundException, IOException, URISyntaxException {
		process(data, basePath);

		createExportPath(exportPath);
		deployResources(exportPath);
		indexPath = deployIndex(exportPath);
		copyImages(data, basePath, exportPath);
	}

	private void copyImages(List<Bone> data, File basePath, File exportPath) {
		try {
			for (Bone bone : data) {
				for (BoneImage image : bone.getImages()) {
					Reader in = new FileReader(image.imageAbsolutePath());
					Writer out = new FileWriter(new File(exportPath, image
							.getImagePath().getName()));
					IOUtils.copy(in, out);
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

	private void deployResources(File dir) throws FileNotFoundException,
			IOException {
		deployResource("jquery-2.0.2.min.js", dir);
		deployResource("jquery-ui.min.js", dir);
		deployResource("jquery-ui.css", dir);
		deployResource("styles.css", dir);
		deployResource("caliboro.js", dir);
		deployResource("dot.png", dir);

		File imgDir = new File(dir, "images");
		if (!imgDir.isDirectory() && !imgDir.mkdir())
			throw new RuntimeException("Error creando directorio de imagenes");

		deployResource("images/ui-icons_222222_256x240.png", dir);
	}

	private void deployResource(String name, File dir)
			throws FileNotFoundException, IOException {
		File path = new File(dir, name);
		IOUtils.copy(Exporter.class.getResourceAsStream("/templates/" + name),
				new FileOutputStream(path));
	}

	private String deployIndex(File dir) throws FileNotFoundException,
			IOException {
		File path = new File(dir, "index.html");
		IOUtils.write(indexContent, new FileOutputStream(path));
		return path.getAbsolutePath();
	}

	public String getIndexPath() {
		return indexPath;
	}

}
