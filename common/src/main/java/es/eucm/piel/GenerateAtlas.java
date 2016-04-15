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
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.badlogic.gdx.utils.Array;

import java.io.File;

public class GenerateAtlas {

	public static class AtlasConfig {

		public TextureFilter filter = TextureFilter.Linear;

		public String atlasName = "atlas";

		public int size = 1024;
	}

	public void execute(File inputDir, File outputDir, AtlasConfig config) {
		Settings settings = new Settings();
		settings.limitMemory = false;
		settings.filterMag = settings.filterMin = config.filter;

		Array<File> imageFolders = new Array<File>();
		for (File child : inputDir.listFiles()) {
			if (child.isDirectory()) {
				imageFolders.add(child);
			}
		}

		if (imageFolders.size == 0) {
			imageFolders.add(inputDir);
		}

		for (File folder : imageFolders) {
			String outputPath = outputDir.getAbsolutePath()
					+ "/"
					+ folder.getAbsolutePath().substring(
							inputDir.getAbsolutePath().length());
			settings.maxWidth = settings.maxHeight = config.size;
			TexturePacker.process(settings, folder.getAbsolutePath(),
					outputPath, config.atlasName);
		}

	}
}
