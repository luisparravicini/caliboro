package ar.com.ktulu.caliboro;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
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

public class BaseExporter {

	protected String indexContent;
	protected String indexPath;

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
		String encodedBasePath = null;
		if (basePath == null)
			encodedBasePath = ".";
		else
			encodedBasePath = Util.encodePath(basePath.getAbsolutePath())
					.toString();
		context.put("bonesBasePath", encodedBasePath);
		StringWriter writer = new StringWriter();
		template.merge(context, writer);

		indexContent = writer.toString();

		return null;
	}

	protected String getIndexContent() {
		return indexContent;
	}

	public String getIndexPath() {
		return indexPath;
	}

	protected Object getDataAsJSON(List<Bone> data) {
		return new JSONArray(CollectionUtils.collect(data, new Transformer() {
			@Override
			public Object transform(Object obj) {
				return new JSONObject(obj);
			}
		}));
	}

	protected void deployResources(File dir) throws FileNotFoundException,
			IOException {
		deployResource("jquery-2.0.2.min.js", dir);
		deployResource("jquery-ui.min.js", dir);
		deployResource("jquery-ui.css", dir);
		deployResource("styles.css", dir);
		deployResource("caliboro.js", dir);
		deployResource("dot.png", dir);

		createDirectory(dir, "images");

		deployResource("images/ui-icons_222222_256x240.png", dir);
	}

	protected void exportFiles(List<Bone> data, File exportPath, File basePath)
			throws IOException {
		process(data, basePath);
		deployResources(exportPath);
		indexPath = deployIndex(exportPath);
	}

	private void createDirectory(File dir, String dirName) {
		File newDir = new File(dir, dirName);
		if (!newDir.isDirectory() && !newDir.mkdir())
			throw new RuntimeException("Error creando directorio: " + dirName);
	}

	private void deployResource(String name, File dir)
			throws FileNotFoundException, IOException {
		File path = new File(dir, name);
		IOUtils.copy(Previewer.class.getResourceAsStream("/templates/" + name),
				new FileOutputStream(path));
	}

	protected String deployIndex(File dir) throws FileNotFoundException,
			IOException {
		File path = new File(dir, "index.html");
		IOUtils.write(indexContent, new FileOutputStream(path));
		return path.getAbsolutePath();
	}

}
