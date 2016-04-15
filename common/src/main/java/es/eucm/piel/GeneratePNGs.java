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

import es.eucm.piel.svg.BatikNinePatch;
import es.eucm.piel.svg.BatikPNG;
import es.eucm.piel.svg.SVGtoPNG;

import java.io.File;

public class GeneratePNGs extends GenerateScales {

	private BatikPNG png = new BatikPNG();

	private BatikNinePatch ninePatch = new BatikNinePatch();

	private SVGtoPNG svGtoPNG;

	public GeneratePNGs() {
		super(".png", "svg");
	}

	public boolean execute(File svgDir, File ninePatchDir, File outputDir,
			ScalesConfig conf) {
		boolean modified = false;
		svGtoPNG = png;
		outputExtension = ".png";
		if (svgDir != null && super.execute(svgDir, outputDir, conf)) {
			modified = true;
		}
		svGtoPNG = ninePatch;
		outputExtension = ".9.png";
		if (ninePatchDir != null
				&& super.execute(ninePatchDir, outputDir, conf)) {
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
