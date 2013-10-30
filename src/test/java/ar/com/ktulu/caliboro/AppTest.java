package ar.com.ktulu.caliboro;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import ar.com.ktulu.caliboro.model.Bone;
import ar.com.ktulu.caliboro.ui.Util;

public class AppTest extends TestCase {
	private Previewer previewer;

	public AppTest(String testName) {
		super(testName);
		previewer = new Previewer();
	}

	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	public void testPreviewerNull() throws URISyntaxException,
			IOException {
		try {
			previewer.process(null, null);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}

	public void testPreviewerEmptyList() throws URISyntaxException,
			IOException {
		List<Bone> bones = new ArrayList<Bone>();
		previewer.process(bones, new File("/"));
		String expected = String.format("Caliboro.data = [];");
		assertTrue(previewer.getIndexContent().contains(expected));
	}

	public void testEncodePath() throws IOException {
		assertEquals(new URL("file:/a%20b%20c"), Util.encodePath("/a b c"));
	}
}
