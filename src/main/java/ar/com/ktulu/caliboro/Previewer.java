package ar.com.ktulu.caliboro;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
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
import ar.com.ktulu.caliboro.ui.Util;

public class Previewer {

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

	public void deploy(List<Bone> data, File basePath)
			throws FileNotFoundException, IOException, URISyntaxException {
		process(data, basePath);

		File tmpDir = createPreviewDirectory();
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

	private void deployResources(File tmpDir) throws FileNotFoundException,
			IOException {
		deployResource("jquery-2.0.2.min.js", tmpDir);
		deployResource("jquery-ui.min.js", tmpDir);
		deployResource("jquery-ui.css", tmpDir);
		deployResource("styles.css", tmpDir);
		deployResource("caliboro.js", tmpDir);
		deployResource("dot.png", tmpDir);
		
		File imgDir = new File(tmpDir, "images");
		if (!imgDir.mkdir())
			throw new RuntimeException("Error creando directorio de imagenes");
		
		deployResource("images/ui-icons_222222_256x240.png", tmpDir);
	}

	private void deployResource(String name, File dir)
			throws FileNotFoundException, IOException {
		File path = new File(dir, name);
		IOUtils.copy(Previewer.class.getResourceAsStream("/templates/" + name),
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
