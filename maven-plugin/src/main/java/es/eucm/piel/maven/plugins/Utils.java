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
package es.eucm.piel.maven.plugins;

import es.eucm.piel.GenerateFonts.FontConfig;
import es.eucm.piel.GenerateFonts.FontsConfig;
import es.eucm.piel.maven.plugins.GenerateFontsMojo.FontParameter;

public class Utils {

	public static float[] toFloat(String[] s) {
		float[] result = new float[s.length];
		int i = 0;
		for (String string : s) {
			result[i++] = Float.parseFloat(string);
		}
		return result;
	}

	public static int[] toInt(String[] s) {
		int[] result = new int[s.length];
		int i = 0;
		for (String string : s) {
			result[i++] = Integer.parseInt(string);
		}
		return result;
	}

	public static FontsConfig fontConfig(String[] scales, FontParameter[] ttfs,
			int atlasSize) {
		FontsConfig config = new FontsConfig();
		config.scales = Utils.toFloat(scales);
		config.atlasSize = atlasSize;
		config.fonts = new FontConfig[ttfs.length];
		int i = 0;
		for (FontParameter fontParameter : ttfs) {
			FontConfig fontConfig = new FontConfig();
			fontConfig.file = fontParameter.file;
			fontConfig.characters = fontParameter.characters;
			fontConfig.sizes = Utils.toInt(fontParameter.sizes.split(";"));
			config.fonts[i++] = fontConfig;
		}
		return config;
	}

}
