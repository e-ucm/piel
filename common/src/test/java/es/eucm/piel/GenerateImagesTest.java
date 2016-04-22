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

public class GenerateImagesTest {

	private static GenerateImages generateImages;
	private FileHandle input, output;
	private static float[] scales;

	@BeforeClass
	public static void setUpClass() {
		generateImages = new GenerateImages();
		scales = new float[] { 0.5f, 1, 2 };
	}

	@Before
	public void setUp() throws IOException {
		FileHandle temp = FileHandle.tempDirectory("piel");
		input = temp.child("input");
		input.mkdirs();
		new LwjglFileHandle("ship.svg", FileType.Classpath).copyTo(input);
		new LwjglFileHandle("textbox.9.svg", FileType.Classpath).copyTo(input);

		output = temp.child("output");
	}

	@Test
	public void testImages() {
		generateImages.generate(input.file(), output.file(), scales);
		assertEquals(scales.length, output.list().length);
		for (float scale : scales) {
			testScale(output.child(Float.toString(scale)), scale);
		}
	}

	private void testScale(FileHandle folder, float scale) {
		testSize(folder.child("textbox.9.png").file(), (int) (66 * scale) + 2,
				(int) (66 * scale) + 2);
		testSize(folder.child("ship.png").file(), (int) (66 * scale),
				(int) (66 * scale));
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
