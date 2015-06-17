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
import com.badlogic.gdx.utils.Array;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public abstract class GenerateScales {

	public static final String PREFIX = "scale";

	protected String outputExtension;

	private Array<String> validExtensions;

	public GenerateScales(String outputExtension, String... validExtensions) {
		this.outputExtension = outputExtension;
		this.validExtensions = new Array<String>(validExtensions);
	}

	public boolean execute(File inputDir, File outputDir, String[] scales) {
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
				scaleFolder(inputDir, scaleFolder, scale);
			}
		}

		if (update(inputDir, outputDir, fscales)) {
			modified = true;
		}

		return modified;
	}

	public void scaleFolder(File inputDir, File scaleFolder, float scale) {
		for (File source : inputDir.listFiles()) {
			FileHandle fh = new FileHandle(source);
			if (validExtensions.contains(fh.extension().toLowerCase(), false)) {
				String outputName = fh.nameWithoutExtension() + outputExtension;
				File output = new File(scaleFolder, outputName);
				System.out.println(source.getAbsolutePath() + " --> "
						+ output.getAbsolutePath());
				scale(source, output, scale);
			}
		}
	}

	public abstract void scale(File sourceFile, File outputFile, float scale);

	public boolean update(File inputDir, File outputDir, Float[] scales) {
		LastModified imagesModified = new LastModified(inputDir);

		if (!inputDir.exists()) {
			inputDir.mkdirs();
		}

		for (File image : inputDir.listFiles()) {
			if (validExtensions.contains(new FileHandle(image).extension()
					.toLowerCase(), false)
					&& imagesModified.isModified(image)) {
				String pngName = new FileHandle(image).nameWithoutExtension()
						+ outputExtension;
				for (float scale : scales) {
					File output = new File(outputDir, PREFIX + scale);
					File png = new File(output, pngName);
					System.out.println(image.getAbsolutePath() + " --> "
							+ png.getAbsolutePath());
					scale(image, png, scale);
				}
			}
		}

		for (float scale : scales) {
			File output = new File(outputDir, PREFIX + scale);
			for (String name : imagesModified.getRemoved()) {
				String fileName = name.substring(0, name.lastIndexOf('.'))
						+ outputExtension;
				File file = new File(output, fileName);
				if (file.exists()) {
					file.delete();
					System.out.println(file.getAbsolutePath() + " deleted");
				}
			}
		}

		return imagesModified.commit();
	}
}
