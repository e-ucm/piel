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

import java.io.File;

public class AtlasGenerator extends Generator {

	public static class AtlasConfig {

		public TextureFilter filter = TextureFilter.Linear;

		public String atlasName = "atlas";

		public int size = 1024;
	}

	public void generate(File input, File output, float scale) {
		generate(input, output, new AtlasConfig());
	}

	public void generate(File input, File output, AtlasConfig config) {
		Settings settings = new Settings();
		settings.limitMemory = false;
		settings.filterMag = settings.filterMin = config.filter;
		settings.maxWidth = settings.maxHeight = config.size;
		TexturePacker.process(settings, input.getAbsolutePath(),
				output.getAbsolutePath(), config.atlasName);
	}
}
