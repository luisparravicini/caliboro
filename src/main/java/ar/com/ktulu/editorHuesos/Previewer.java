package ar.com.ktulu.editorHuesos;

import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.json.JSONObject;

import ar.com.ktulu.editorHuesos.model.StoreRootNode;

public class Previewer {

	private String indexContent;

	public String process(StoreRootNode storeRootNode) {
		if (storeRootNode == null)
			throw new IllegalArgumentException("Faltan datos");

		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class",
				ClasspathResourceLoader.class.getName());
		ve.init();
		Template template = ve.getTemplate("templates/index.html.vm", "utf-8");
		VelocityContext context = new VelocityContext();
		context.put("bonesData", new JSONObject(storeRootNode));
		context.put("bonesBasePath", "/");
		StringWriter writer = new StringWriter();
		template.merge(context, writer);

		indexContent = writer.toString();

		return null;
	}

	protected String getIndexContent() {
		return indexContent;
	}

}
