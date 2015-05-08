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

import com.badlogic.gdx.files.FileHandle;
import es.eucm.maven.plugins.piel.svg.BatikNinePatch;
import es.eucm.maven.plugins.piel.svg.BatikPNG;
import es.eucm.maven.plugins.piel.svg.SVGtoPNG;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class GeneratePNGs {

	public static final String PREFIX = "scale";

	private BatikPNG png = new BatikPNG();

	private BatikNinePatch ninePatch = new BatikNinePatch();

	private File svgDir;

	private File ninePatchDir;

	private File outputDir;

	private String[] scales;

	public GeneratePNGs(String svgDir, String ninePatchDir, String outpuDir,
			String[] scales) {
		this(new File(svgDir), new File(ninePatchDir), new File(outpuDir),
				scales);
	}

	public GeneratePNGs(File svgDir, File ninePatchDir, File outputDir,
			String[] scales) {
		this.svgDir = svgDir;
		this.ninePatchDir = ninePatchDir;
		this.outputDir = outputDir;
		this.scales = scales;
	}

	public boolean execute() {
		boolean modified = false;
		Float fscales[] = new Float[scales.length];
		int i = 0;
		for (String scaleString : scales) {
			fscales[i++] = Float.parseFloat(scaleString);
		}

		// Remove old scales
		List<Float> list = Arrays.asList(fscales);
		if (outputDir.exists()) {
			for (File file : outputDir.listFiles()) {
				try {
					Float scale = Float.parseFloat(file.getName().substring(
							PREFIX.length()));
					if (file.isDirectory() && !list.contains(scale)) {
						System.out.println("Removing scale "
								+ file.getAbsolutePath());
						new FileHandle(file).deleteDirectory();
					}
				} catch (NumberFormatException e) {
				}
			}
		}

		// Generate new scales
		for (float scale : fscales) {
			File scaleFolder = new File(outputDir, PREFIX + scale);
			if (!scaleFolder.exists()) {
				modified = true;
				scaleFolder.mkdirs();
				generatePNGs(png, svgDir, scaleFolder, scale, ".png");
				generatePNGs(ninePatch, ninePatchDir, scaleFolder, scale,
						".9.png");
			}
		}

		if (svgDir != null) {
			if (updatePNGs(png, svgDir, outputDir, fscales, ".png")) {
				modified = true;
			}
		}

		if (ninePatchDir != null) {
			if (updatePNGs(ninePatch, ninePatchDir, outputDir, fscales,
					".9.png")) {
				modified = true;
			}
		}

		return modified;
	}

	private void generatePNGs(SVGtoPNG svgToPng, File inputDir,
			File scaleFolder, float scale, String suffix) {
		for (File svg : inputDir.listFiles()) {
			if (!svg.isDirectory() && svg.getAbsolutePath().endsWith(".svg")) {
				String pngName = new FileHandle(svg).nameWithoutExtension()
						+ suffix;
				File png = new File(scaleFolder, pngName);
				System.out.println(svg.getAbsolutePath() + " --> "
						+ png.getAbsolutePath());
				svgToPng.convert(svg.getAbsolutePath(), png.getAbsolutePath(),
						scale);
			}
		}
	}

	private boolean updatePNGs(SVGtoPNG svgToPng, File inputDir,
			File outputDir, Float[] scales, String suffix) {
		LastModified svgModified = new LastModified(inputDir);

		for (File svg : inputDir.listFiles()) {
			if (!svg.isDirectory() && svg.getAbsolutePath().endsWith(".svg")
					&& svgModified.isModified(svg)) {
				String pngName = new FileHandle(svg).nameWithoutExtension()
						+ suffix;
				for (float scale : scales) {
					File output = new File(outputDir, PREFIX + scale);
					File png = new File(output, pngName);
					System.out.println(svg.getAbsolutePath() + " --> "
							+ png.getAbsolutePath());
					svgToPng.convert(svg.getAbsolutePath(),
							png.getAbsolutePath(), scale);
				}
			}
		}

		for (float scale : scales) {
			File output = new File(outputDir, PREFIX + scale);
			for (String name : svgModified.getRemoved()) {
				String fileName = name.substring(0, name.lastIndexOf('.'))
						+ suffix;
				File file = new File(output, fileName);
				if (file.exists()) {
					file.delete();
					System.out.println(file.getAbsolutePath() + " deleted");
				}
			}
		}

		return svgModified.commit();
	}
}
