package ar.com.ktulu.editorHuesos;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.json.JSONArray;
import org.json.JSONObject;

import ar.com.ktulu.editorHuesos.model.Bone;

public class AppTest extends TestCase {
	private Previewer previewer;

	public AppTest(String testName) {
		super(testName);
		previewer = new Previewer();
	}

	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	public void testPreviewerNull() throws URISyntaxException {
		try {
			previewer.process(null, null);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}

	public void testPreviewerEmptyList() throws URISyntaxException {
		List<Bone> bones = new ArrayList<Bone>();
		previewer.process(bones, new File("/"));
		String expected = String.format("Caliboro.data = [];");
		assertTrue(previewer.getIndexContent().contains(expected));
	}

	public void testPreviewerList() throws URISyntaxException {
		List<Bone> bones = new ArrayList<Bone>();
		bones.add(new Bone("nombre1"));
		Bone bone = new Bone("nombre2");
		bone.addImage("/xxyyzz");
		bones.add(bone);
		previewer.process(bones, new File("/"));
		String expected = String.format("Caliboro.data = %s;", new JSONArray(
				CollectionUtils.collect(bones, new Transformer() {
					@Override
					public Object transform(Object obj) {
						return new JSONObject(obj);
					}
				})));
		assertTrue(previewer.getIndexContent().contains(expected));
	}
}
