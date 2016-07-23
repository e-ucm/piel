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

public abstract class Generator {

	public void cleanOutputFolder(File output, float... scales) {
		for (float scale : scales) {
			File scaleOutput = new File(output, Float.toString(scale));
			if (scaleOutput.exists()) {
				if (scaleOutput.isDirectory()) {
					new FileHandle(scaleOutput).deleteDirectory();
				} else {
					if (!scaleOutput.delete()) {
						throw new RuntimeException(
								"Error cleaning output folder.");
					}
				}
			}
		}
	}

	public void generate(File input, File output, float... scales) {
		if (!input.isDirectory() || !input.exists()) {
			throw new RuntimeException(
					"Input folder must exist and be a directory");
		}

		cleanOutputFolder(output, scales);

		for (float scale : scales) {
			File scaleOutput = new File(output, Float.toString(scale));
			if (!scaleOutput.mkdirs()) {
				throw new RuntimeException(
						"Impossible to create output folder. Check you have enough permissions.");
			}
			generate(input, scaleOutput, scale);
		}
	}

	public abstract void generate(File input, File output, float scale);
}
