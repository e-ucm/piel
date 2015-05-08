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
package es.eucm.maven.plugins.piel;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

import java.io.File;

public class GenerateSkins {

	private File svgDir;

	private File ninePatchDir;

	private File outputPngDir;

	private String[] scales;

	private File[] ttfs;

	private String[] fontSizes;

	private TextureFilter filter;

	private Integer size;

	private Integer maxSize;

	private String atlasName;

	private File outputDir;

	public GenerateSkins(String svgDir, String ninePatchDir,
			String outputPngDir, String[] scales, File[] ttfs,
			String[] fontSizes, TextureFilter filter, Integer size,
			String atlasName, String outputDir) {
		this(new File(svgDir), new File(ninePatchDir), new File(outputPngDir),
				scales, ttfs, fontSizes, filter, size, null, atlasName,
				new File(outputDir));
	}

	public GenerateSkins(File svgDir, File ninePatchDir, File outputPngDir,
			String[] scales, File[] ttfs, String[] fontSizes,
			TextureFilter filter, Integer size, Integer maxSize,
			String atlasName, File outputDir) {
		this.svgDir = svgDir;
		this.ninePatchDir = ninePatchDir;
		this.outputPngDir = outputPngDir;
		this.scales = scales;
		this.ttfs = ttfs;
		this.fontSizes = fontSizes;
		this.filter = filter;
		this.size = size;
		this.atlasName = atlasName;
		this.outputDir = outputDir;
		this.maxSize = maxSize;
	}

	public void execute() {
		boolean updated = new GeneratePNGs(svgDir, ninePatchDir, outputPngDir,
				scales).execute();

		FileHandle fonts = new FileHandle(new File(outputPngDir, ".ttffonts"));
		String generated = "";
		for (File file : ttfs) {
			generated += file.getAbsolutePath() + ";";
		}
		for (String scale : scales) {
			generated += scale + ";";
		}
		for (String fontSize : fontSizes) {
			generated += fontSize + ";";
		}
		generated += size;

		if (!fonts.exists() || !generated.equals(fonts.readString())) {
			System.out.println("Generating fonts from TTFs");
			new GenerateFonts(ttfs, outputPngDir, scales, fontSizes, size / 4)
					.execute();
			fonts.writeString(generated, false);
			updated = true;
		}

		if (updated) {
			new GenerateAtlas(outputPngDir, outputDir, filter, size, maxSize,
					atlasName, true).execute();

			// Extra tasks
			// Copy .fnt
			for (FileHandle folder : new FileHandle(outputPngDir).list()) {
				if (folder.isDirectory()) {
					for (FileHandle fnt : folder.list(".fnt")) {
						fnt.copyTo(new FileHandle(new File(outputDir, folder
								.name() + "/" + fnt.name())));
					}
				}
			}
		}
	}
}
