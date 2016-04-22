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

import com.badlogic.gdx.files.FileHandle;
import es.eucm.piel.GenerateAtlas.AtlasConfig;
import es.eucm.piel.GenerateFonts.FontConfig;
import es.eucm.piel.GenerateFonts.FontsConfig;

import java.io.File;

public class GenerateSkins {

	public static class SkinsConfig {

		/**
		 * Folder where images are stored
		 */
		public String images = "images";

		/**
		 * Folder to store temporal pngs cache
		 */
		public String png = ".png";

	}

	public void execute(File inputDir, File outputDir, SkinsConfig skinsConfig,
			FontsConfig fontsConfig, AtlasConfig atlasConfig) {
		boolean updated = false;

		File pngOutput = new File(inputDir, skinsConfig.png);
		/*
		 * if (new GeneratePNGs() .execute(inputDir, pngOutput, fontsConfig)) {
		 * updated = true; }
		 */

		/*
		 * if (new GenerateImages().generate( new File(inputDir,
		 * skinsConfig.images), pngOutput, fontsConfig)) { updated = true; }
		 */

		if (fontsConfig.fonts != null) {
			FileHandle fontsCached = new FileHandle(new File(pngOutput,
					".ttffonts"));
			// TODO remove cached font and not present in the given
			// configuration
			String generated = "";
			for (FontConfig font : fontsConfig.fonts) {
				generated += font.file;
				generated += font.characters;
				for (int size : font.sizes) {
					generated += size + ";";
				}
			}

			/*
			 * for (float scale : fontsConfig.scales) { generated += scale +
			 * ";"; }
			 */

			generated += fontsConfig.atlasSize;

			/*
			 * if (fontsConfig.force || (!fontsCached.exists() ||
			 * !generated.equals(fontsCached .readString()))) {
			 * System.out.println("Generating fonts from TTFs"); new
			 * GenerateFonts().execute(pngOutput, fontsConfig);
			 * fontsCached.writeString(generated, false); updated = true; } else
			 * { System.out.println("No TTF fonts updates."); }
			 */
		}

		if (updated) {
			System.out.println("Resources were updated. Regenerating skins...");
			new GenerateAtlas().execute(pngOutput, outputDir, atlasConfig);

			// Extra tasks
			// Copy .fnt
			for (FileHandle folder : new FileHandle(pngOutput).list()) {
				if (folder.isDirectory()) {
					for (FileHandle fnt : folder.list(".fnt")) {
						fnt.moveTo(new FileHandle(new File(outputDir, folder
								.name() + "/" + fnt.name())));
					}
				}
			}
		} else {
			System.out.println("No updates were found.");
		}

		// Copy skin.json to all scales
		FileHandle skin = new FileHandle(new File(outputDir, "skin.json"));
		if (skin.exists()) {
			/*
			 * for (float scale : fontsConfig.scales) {
			 * skin.copyTo(skin.parent().child(Float.toString(scale))
			 * .child("skin.json")); }
			 */
		}
	}

}
