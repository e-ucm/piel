/**
 * Copyright (C) 2015 e-UCM Research Group (e-adventure-dev@e-ucm.es)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.eucm.piel;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglFileHandle;
import com.badlogic.gdx.files.FileHandle;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ImagesGeneratorTest {

	private static ImagesGenerator imagesGenerator;
	private FileHandle input, output;
	private static float[] scales;
	private static String[] extensions = new String[] { "svg", "9.svg", "bmp",
			"gif", "jpeg", "jpg", "PNG" };

	@BeforeClass
	public static void setUpClass() {
		imagesGenerator = new ImagesGenerator();
		scales = new float[] { 0.5f, 1, 2 };
	}

	@Before
	public void setUp() throws IOException {
		FileHandle temp = FileHandle.tempDirectory("piel");
		input = temp.child("input");
		input.mkdirs();
		for (String extension : extensions) {
			new LwjglFileHandle("image." + extension, FileType.Classpath)
					.copyTo(input);
		}

		output = temp.child("output");
	}

	@Test
	public void testImages() {
		imagesGenerator.generate(input.file(), output.file(), scales);
		assertEquals(scales.length, output.list().length);
		for (float scale : scales) {
			for (String extension : extensions) {
				int extra = "9.svg".equals(extension) ? 2 : 0;
				String ext = "9.svg".equals(extension) ? "9.png" : "svg"
						.equals(extension) ? "png" : extension;
				testSize(
						output.child(Float.toString(scale))
								.child("image." + ext).file(),
						(int) (20 * scale) + extra, (int) (20 * scale) + extra);
			}
		}
	}

	private void testSize(File file, int width, int height) {
		assertTrue(file.exists());
		try {
			BufferedImage image = ImageIO.read(file);
			assertEquals(image.getWidth(), width);
			assertEquals(image.getHeight(), height);
		} catch (IOException e) {
			fail();
			e.printStackTrace();
		}
	}
}
