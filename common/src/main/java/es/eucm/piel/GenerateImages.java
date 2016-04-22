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
import es.eucm.piel.svg.SVGPatchToPNG;
import es.eucm.piel.svg.SVGtoPNG;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GenerateImages {

	private SVGtoPNG png = new SVGtoPNG();

	private SVGPatchToPNG ninePatch = new SVGPatchToPNG();

	/**
	 * Scales all images contained in input
	 */
	public void generate(File input, File output, float... scales) {

		if (input == null || !input.exists() || input.listFiles() == null) {
			throw new RuntimeException("Input folder must exist an be a folder");
		}
		if (output == null) {
			throw new RuntimeException("Output folder cannot be null");
		}

		if (!output.exists()) {
			if (!output.mkdirs()) {
				throw new RuntimeException("Unable to create output folder");
			}
		}

		for (float scale : scales) {
			File scaledFolder = new File(output, Float.toString(scale));
			if (!scaledFolder.exists()) {
				if (!scaledFolder.mkdir()) {
					throw new RuntimeException("Error creating output folder "
							+ scaledFolder);
				}
			}

			for (File source : input.listFiles()) {
				scale(source, scaledFolder, scale);
			}
		}
	}

	/**
	 * Scales the content of sourceFile and writes it to outputFile
	 */
	public void scale(File sourceFile, File outputFolder, float scale) {
		FileHandle src = new FileHandle(sourceFile);
		if ("svg".equals(src.extension())) {
			SVGtoPNG svGtoPNG = sourceFile.getName().endsWith(".9.svg") ? ninePatch
					: png;
			svGtoPNG.convert(sourceFile.getAbsolutePath(), new File(
					outputFolder, src.nameWithoutExtension() + ".png")
					.getAbsolutePath(), scale);
		} else if (src.extension().toLowerCase()
				.matches("png|jpg|jpeg|bmp|gif")) {
			try {
				BufferedImage inImage = ImageIO.read(sourceFile);
				BufferedImage outImage = Scalr.resize(inImage,
						Math.round(inImage.getWidth() * scale),
						Math.round(inImage.getHeight() * scale));
				ImageIO.write(outImage, src.extension().toLowerCase(),
						new File(outputFolder, sourceFile.getName()));
				inImage.flush();
				outImage.flush();
			} catch (IOException e) {
				System.err.println("Error scaling " + sourceFile);
				e.printStackTrace();
			}
		}
	}
}
