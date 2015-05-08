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

import es.eucm.maven.plugins.piel.fonts.TTFtoFNT;

import java.io.File;

public class GenerateFonts {
	private File[] ttfs;

	private File outputDir;

	private String[] scales;

	private String[] sizes;

	private Integer atlasSize;

	public GenerateFonts(File[] ttfs, File outputDir, String[] scales,
			String[] sizes, Integer atlasSize) {
		this.ttfs = ttfs;
		this.outputDir = outputDir;
		this.scales = scales;
		this.sizes = sizes;
		this.atlasSize = atlasSize;
	}

	public void execute() {
		for (File ttf : ttfs) {
			TTFtoFNT toFNT = new TTFtoFNT(ttf);
			for (String scaleString : scales) {
				float scale = Float.parseFloat(scaleString);
				File output = new File(outputDir, GeneratePNGs.PREFIX + scale);
				output.mkdirs();
				for (String sizeString : sizes) {
					int size = Integer.parseInt(sizeString);
					toFNT.toFnt(size, scale, output, atlasSize);
				}
			}
		}
	}
}
