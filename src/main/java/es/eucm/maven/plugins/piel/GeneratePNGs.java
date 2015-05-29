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

import es.eucm.maven.plugins.piel.svg.BatikNinePatch;
import es.eucm.maven.plugins.piel.svg.BatikPNG;
import es.eucm.maven.plugins.piel.svg.SVGtoPNG;

import java.io.File;

public class GeneratePNGs extends GenerateScales {

	private BatikPNG png = new BatikPNG();

	private BatikNinePatch ninePatch = new BatikNinePatch();

	private SVGtoPNG svGtoPNG;

	public GeneratePNGs() {
		super(".png", "svg");
	}

	public boolean execute(File svgDir, File ninePatchDir, File outputDir,
			String[] scales) {
		boolean modified = false;
		svGtoPNG = png;
		outputExtension = ".png";
		if (super.execute(svgDir, outputDir, scales)) {
			modified = true;
		}
		svGtoPNG = ninePatch;
		outputExtension = ".9.png";
		if (super.execute(ninePatchDir, outputDir, scales)) {
			modified = true;
		}
		return modified;
	}

	@Override
	public void scale(File sourceFile, File outputFile, float scale) {
		svGtoPNG.convert(sourceFile.getAbsolutePath(),
				outputFile.getAbsolutePath(), scale);
	}
}
