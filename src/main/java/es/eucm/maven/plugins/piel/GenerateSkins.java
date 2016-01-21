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
import java.util.Map.Entry;
import java.util.Properties;

public class GenerateSkins {

	private File svgDir;

	private File ninePatchDir;

	private File outputPngDir;

	private File imagesDir;

	private String[] scales;

	private Properties ttfs;

	private TextureFilter filter;

	private Integer size;

	private Integer maxSize;

	private String atlasName;

	private File outputDir;

	public GenerateSkins(String imagesDir, String svgDir, String ninePatchDir,
			String outputPngDir, String[] scales, Properties ttfs,
			TextureFilter filter, Integer size, String atlasName,
			String outputDir) {
		this(imagesDir == null ? null : new File(imagesDir),
				svgDir == null ? null : new File(svgDir),
				ninePatchDir == null ? null : new File(ninePatchDir),
				outputPngDir == null ? null : new File(outputPngDir), scales,
				ttfs, filter, size, null, atlasName, new File(outputDir));
	}

	public GenerateSkins(File imagesDir, File svgDir, File ninePatchDir,
			File outputPngDir, String[] scales, Properties ttfs,
			TextureFilter filter, Integer size, Integer maxSize,
			String atlasName, File outputDir) {
		this.imagesDir = imagesDir;
		this.svgDir = svgDir;
		this.ninePatchDir = ninePatchDir;
		this.outputPngDir = outputPngDir;
		this.scales = scales;
		this.ttfs = ttfs;
		this.filter = filter;
		this.size = size;
		this.atlasName = atlasName;
		this.outputDir = outputDir;
		this.maxSize = maxSize;
	}

	public void execute() {
		boolean updated = false;
		if (svgDir != null || ninePatchDir != null) {
			updated = new GeneratePNGs().execute(svgDir, ninePatchDir,
					outputPngDir, scales);
		}

		if (imagesDir != null) {
			if (new GenerateImages().execute(imagesDir, outputPngDir, scales)) {
				updated = true;
			}
		}

		if (ttfs != null) {
			FileHandle fonts = new FileHandle(new File(outputPngDir,
					".ttffonts"));
			String generated = "";
		if (ttfs != null) {
			for (Entry<Object, Object> e : ttfs.entrySet()) {
				generated += e.getKey().toString() + ";"
						+ e.getValue().toString();
			}
			}

			for (String scale : scales) {
				generated += scale + ";";
			}

			generated += size;

		if (ttfs != null
				&& (!fonts.exists() || !generated.equals(fonts.readString()))) {
				System.out.println("Generating fonts from TTFs");
				new GenerateFonts(ttfs, outputPngDir, scales, size / 4)
						.execute();
				fonts.writeString(generated, false);
				updated = true;
			}
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
