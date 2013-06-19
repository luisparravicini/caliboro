package ar.com.ktulu.editorHuesos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.json.JSONArray;

import ar.com.ktulu.editorHuesos.model.Bone;

public class Previewer {

	private String indexContent;
	private String indexPath;

	// TODO esto esta publico para el test
	public String process(List<Bone> data, File basePath)
			throws URISyntaxException {
		if (data == null)
			throw new IllegalArgumentException("Faltan datos");

		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class",
				ClasspathResourceLoader.class.getName());
		ve.init();
		Template template = ve.getTemplate("templates/index.html.vm", "utf-8");
		VelocityContext context = new VelocityContext();
		context.put("bonesData", new JSONArray(data));
		context.put("bonesBasePath",
				new URI("file://" + basePath.getAbsolutePath()));
		StringWriter writer = new StringWriter();
		template.merge(context, writer);

		indexContent = writer.toString();

		return null;
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
		deployResource("styles.css", tmpDir);
		deployResource("huesitos.js", tmpDir);
		deployResource("dot.png", tmpDir);
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
