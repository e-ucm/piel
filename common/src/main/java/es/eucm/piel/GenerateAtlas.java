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

import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.badlogic.gdx.utils.Array;

import java.io.File;

public class GenerateAtlas {

	private File sourceDir;

	private File outputDir;

	private TextureFilter filter;

	private Integer size;

	private String atlasName;

	private boolean readScales;

	private Integer maxSize;

	public GenerateAtlas(String sourceDir, String outputDir,
			TextureFilter filter, int atlasSize, String atlasName) {
		this(new File(sourceDir), new File(outputDir), filter, atlasSize, null,
				atlasName, false);
	}

	public GenerateAtlas(File sourceDir, File outputDir, TextureFilter filter,
			Integer size, Integer maxSize, String atlasName, boolean readScales) {
		this.sourceDir = sourceDir;
		this.outputDir = outputDir;
		this.filter = filter;
		this.size = size;
		this.atlasName = atlasName;
		this.readScales = readScales;
		this.maxSize = maxSize;
	}

	public void execute() {
		Settings settings = new Settings();
		settings.limitMemory = false;
		settings.filterMag = settings.filterMin = filter;

		Array<File> imageFolders = new Array<File>();
		for (File child : sourceDir.listFiles()) {
			if (child.isDirectory()) {
				imageFolders.add(child);
			}
		}

		if (imageFolders.size == 0) {
			imageFolders.add(sourceDir);
		}

		for (File folder : imageFolders) {
			String outputPath = outputDir.getAbsolutePath()
					+ "/"
					+ folder.getAbsolutePath().substring(
							sourceDir.getAbsolutePath().length());
			if (readScales) {
				int scaledSize = MathUtils.nextPowerOfTwo((int) (Float
						.parseFloat(folder.getName()
								.replaceAll("[^0-9\\.]", "")) * size));
				settings.maxWidth = settings.maxHeight = maxSize == null ? scaledSize
						: Math.min(scaledSize, maxSize);
			} else {
				settings.maxWidth = settings.maxHeight = size;
			}
			TexturePacker.process(settings, folder.getAbsolutePath(),
					outputPath, atlasName);
		}

	}
}
