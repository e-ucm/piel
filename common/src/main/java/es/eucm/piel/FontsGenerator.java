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

import com.badlogic.gdx.backends.lwjgl.LwjglNativesLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeBitmapFontData;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.tools.bmfont.BitmapFontWriter;
import com.badlogic.gdx.tools.bmfont.BitmapFontWriter.OutputFormat;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Properties;

public class FontsGenerator extends Generator {

	private static final FilenameFilter ttfFilter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".ttf");
		}
	};

	public FontsGenerator() {
		LwjglNativesLoader.load();
	}

	public void generate(File input, File output) {
		generate(input, output, 1.0f);
	}

	public void generate(File input, File output, float scale) {
		for (FileHandle ttf : new FileHandle(input).list(ttfFilter)) {
			FileHandle propertiesFile = ttf.parent().child(
					ttf.nameWithoutExtension() + ".properties");
			boolean error = true;
			if (propertiesFile.exists()) {
				Properties properties = new Properties();
				try {
					properties.load(propertiesFile.reader());
					if (properties.containsKey("sizes")) {
						int[] sizes = Utils.toInt(properties.getProperty(
								"sizes").split(","));
						if (sizes.length > 0) {
							error = false;
						}

						int atlasSize = properties.containsKey("atlasSize") ? Integer
								.parseInt(properties.getProperty("atlasSize"))
								: 256;

						String characters = properties.getProperty(
								"characters",
								FreeTypeFontGenerator.DEFAULT_CHARS);

						for (int size : sizes) {
							generateFont(ttf, new FileHandle(output).child(ttf
									.nameWithoutExtension()
									+ "-"
									+ size
									+ ".fnt"), (int) (size * scale), atlasSize,
									characters);
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (error) {
				throw new RuntimeException(
						propertiesFile.name()
								+ " should exist and define, at least, the font sizes like\n"
								+ "sizes = 12,16,18");
			}
		}
	}

	private void generateFont(FileHandle ttf, FileHandle output, int size,
			int atlasSize, String characters) {

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(ttf);

		PixmapPacker packer = new PixmapPacker(atlasSize, atlasSize,
				Pixmap.Format.RGBA8888, 2, false);

		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.packer = packer;
		parameter.characters = characters;
		parameter.size = size;
		parameter.flip = false;

		FreeTypeBitmapFontData data = generator.generateData(parameter);

		BitmapFontWriter.setOutputFormat(OutputFormat.Text);
		String[] pageRefs = BitmapFontWriter.writePixmaps(
				parameter.packer.getPages(), output.parent(),
				output.nameWithoutExtension());

		BitmapFontWriter.writeFont(data, pageRefs, output,
				new BitmapFontWriter.FontInfo(output.nameWithoutExtension(),
						size), 1, 1);
	}
}
