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

import es.eucm.piel.GenerateScales.ScalesConfig;
import es.eucm.piel.fonts.TTFtoFNT;

import java.io.File;

public class GenerateFonts {

	public static class FontsConfig extends ScalesConfig {

		public FontConfig[] fonts;

		public int atlasSize;

	}

	public static class FontConfig {
		public String file;

		public int[] sizes;

		public String characters;
	}

	public void execute(File outputDir, FontsConfig fontsConfig) {
		if (!outputDir.exists()) {
			outputDir.mkdirs();
		}

		for (FontConfig font : fontsConfig.fonts) {
			File fontFile = new File(font.file);
			TTFtoFNT toFNT = new TTFtoFNT(fontFile);
			System.out.println("Generating font " + fontFile.getName());
			for (float scale : fontsConfig.scales) {
				File output = new File(outputDir, fontsConfig.scalePrefix
						+ scale);
				output.mkdir();
				System.out.println("Generating scale " + scale);
				for (int size : font.sizes) {
					System.out.println("Generating size " + size);
					toFNT.toFnt(size, scale, fontsConfig.atlasSize,
							font.characters, output);
				}
			}
		}
	}
}
