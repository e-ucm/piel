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
import es.eucm.piel.GenerateScales.ScalesConfig;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GeneratePNGsTest {

	private static GeneratePNGs generatePNGs;
	private FileHandle svg, ninePatch, output;
	private static ScalesConfig scalesConfig;

	@BeforeClass
	public static void setUpClass() {
		generatePNGs = new GeneratePNGs();
		scalesConfig = new ScalesConfig();
		scalesConfig.scales = new float[] { 1, 2 };
	}

	@Before
	public void setUp() throws IOException {
		FileHandle temp = FileHandle.tempDirectory("piel");
		svg = temp.child("svg");
		svg.mkdirs();
		new LwjglFileHandle("ship.svg", FileType.Classpath).copyTo(svg);

		ninePatch = temp.child("9patch");
		ninePatch.mkdirs();
		new LwjglFileHandle("textbox.svg", FileType.Classpath)
				.copyTo(ninePatch);

		output = temp.child("output");
	}

	@Test
	public void testNullDoesNotFail() {
		generatePNGs.execute(null, null, null, scalesConfig);
		generatePNGs.execute(svg.file(), null, null, scalesConfig);
		generatePNGs.execute(null, ninePatch.file(), null, scalesConfig);
		generatePNGs.execute(null, null, output.file(), scalesConfig);
		assertEquals(output.list().length, 0);
	}

	@Test
	public void testSvg() {
		generatePNGs.execute(svg.file(), null, output.file(), scalesConfig);
		assertEquals(output.list().length, scalesConfig.scales.length);
		for (FileHandle folder : output.list()) {
			assertTrue(folder.child("ship.png").exists());
		}
	}

	@Test
	public void test9Patch() {
		generatePNGs.execute(null, ninePatch.file(), output.file(),
				scalesConfig);
		assertEquals(output.list().length, scalesConfig.scales.length);
		for (FileHandle folder : output.list()) {
			assertTrue(folder.child("textbox.9.png").exists());
		}
	}

	@Test
	public void testSvg9Patch() {
		generatePNGs.execute(svg.file(), ninePatch.file(), output.file(),
				scalesConfig);
		assertEquals(output.list().length, scalesConfig.scales.length);
		for (FileHandle folder : output.list()) {
			assertTrue(folder.child("textbox.9.png").exists());
			assertTrue(folder.child("ship.png").exists());
		}
	}
}
