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

import es.eucm.piel.fonts.TTFtoFNT;

import java.io.File;
import java.util.Map.Entry;
import java.util.Properties;

public class GenerateFonts {
	private Properties ttfs;

	private File outputDir;

	private String[] scales;

	private Integer atlasSize;

	public GenerateFonts(Properties ttfs, File outputDir, String[] scales,
			Integer atlasSize) {
		this.ttfs = ttfs;
		this.outputDir = outputDir;
		this.scales = scales;
		this.atlasSize = atlasSize;
	}

	public void execute() {
		for (Entry<Object, Object> e : ttfs.entrySet()) {
			File fontFile = new File(e.getKey().toString());
			TTFtoFNT toFNT = new TTFtoFNT(fontFile);
			System.out.println("Generating font " + fontFile.getName());
			for (String scaleString : scales) {
				float scale = Float.parseFloat(scaleString);
				File output = new File(outputDir, GeneratePNGs.PREFIX + scale);
				output.mkdirs();
				System.out.println("Generating scale " + scale);
				for (String sizeString : e.getValue().toString().split(";")) {
					System.out.println("Generating size " + sizeString);
					int size = Integer.parseInt(sizeString);
					toFNT.toFnt(size, scale, output, atlasSize);
				}
			}
		}
	}
}
