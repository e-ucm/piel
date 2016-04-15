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
import com.badlogic.gdx.utils.Array;

import java.io.File;
import java.util.ArrayList;

public abstract class GenerateScales {

	public static class ScalesConfig {

		/**
		 * Each scale folder will be name ${scalePrefix}${scale}
		 */
		public String scalePrefix = "scale";

		public float[] scales;

		/**
		 * Forces the regeneration of the output even if there are no changes
		 */
		public boolean force;

	}

	protected String outputExtension;

	private Array<String> validExtensions;

	public GenerateScales(String outputExtension, String... validExtensions) {
		this.outputExtension = outputExtension;
		this.validExtensions = new Array<String>(validExtensions);
	}

	/**
	 * @return true if any file was generated or updated
	 */
	public boolean execute(File inputDir, File outputDir,
			ScalesConfig configuration) {

		if (inputDir == null || !inputDir.exists() || outputDir == null) {
			return false;
		}

		boolean modified = false;

		// Remove old scales
		ArrayList<Float> list = new ArrayList<Float>();
		for (float scale : configuration.scales) {
			list.add(scale);
		}
		if (outputDir.exists()) {
			for (File file : outputDir.listFiles()) {
				try {
					Float scale = Float.parseFloat(file.getName().substring(
							configuration.scalePrefix.length()));
					if (file.isDirectory() && !list.contains(scale)) {
						System.out.println("Scale " + scale + " was removed");
						new FileHandle(file).deleteDirectory();
					}
				} catch (NumberFormatException e) {
				}
			}
		} else {
			if (!outputDir.mkdirs()) {
				throw new RuntimeException(
						"Impossible to create output directory");
			}
			modified = true;
		}

		// Generate new scales
		for (float scale : configuration.scales) {
			File scaledFolder = new File(outputDir, configuration.scalePrefix
					+ scale);
			if (!scaledFolder.exists()) {
				System.out.println("New scale found: " + scale
						+ " Generating...");
				modified = true;
				if (scaledFolder.mkdir()) {
					generateScaled(inputDir, scaledFolder, scale);
				} else {
					System.err.println("Error creating output folder "
							+ scaledFolder);
				}
			}
		}

		// Check updates in input files
		if (update(inputDir, outputDir, configuration)) {
			modified = true;
		}

		return modified;
	}

	private void generateScaled(File inputDir, File outputDir, float scale) {
		for (File source : inputDir.listFiles()) {
			FileHandle fh = new FileHandle(source);
			if (validExtensions.contains(fh.extension().toLowerCase(), false)) {
				String outputName = fh.nameWithoutExtension() + outputExtension;
				File output = new File(outputDir, outputName);
				System.out.println(source.getAbsolutePath() + " --> "
						+ output.getAbsolutePath());
				scale(source, output, scale);
			}
		}
	}

	private boolean update(File inputDir, File outputDir, ScalesConfig conf) {
		LastModified imagesModified = new LastModified(inputDir, conf.force);
		for (File image : inputDir.listFiles()) {
			if (validExtensions.contains(new FileHandle(image).extension()
					.toLowerCase(), false)
					&& imagesModified.isModified(image)) {
				String pngName = new FileHandle(image).nameWithoutExtension()
						+ outputExtension;
				for (float scale : conf.scales) {
					File output = new File(outputDir, conf.scalePrefix + scale);
					File png = new File(output, pngName);
					System.out.println(image.getAbsolutePath()
							+ " was updated --> " + png.getAbsolutePath());
					scale(image, png, scale);
				}
			}
		}

		for (float scale : conf.scales) {
			File output = new File(outputDir, conf.scalePrefix + scale);
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

	/**
	 * Scales the content of sourceFile and writes to outputFile
	 */
	public abstract void scale(File sourceFile, File outputFile, float scale);
}
