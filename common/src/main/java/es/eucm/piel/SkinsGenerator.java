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

import java.io.File;

public class SkinsGenerator extends Generator {

	public void generate(File input, File output, float scale) {

		File pngOutput = new File(output, ".png");
		pngOutput.mkdirs();

		ImagesGenerator imagesGenerator = new ImagesGenerator();
		imagesGenerator.generate(input, pngOutput, scale);

		FontsGenerator fontsGenerator = new FontsGenerator();
		fontsGenerator.generate(input, pngOutput, scale);

		AtlasGenerator atlasGenerator = new AtlasGenerator();
		atlasGenerator.generate(pngOutput, output, scale);

		new FileHandle(pngOutput).deleteDirectory();
	}
}
