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

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GenerateImages extends GenerateScales {

	public GenerateImages() {
		super(".png", "png", "jpg", "jpeg");
	}

	@Override
	public void scale(File sourceFile, File outputFile, float scale) {
		try {
			BufferedImage inImage = ImageIO.read(sourceFile);
			BufferedImage outImage = Scalr.resize(inImage,
					Math.round(inImage.getWidth() * scale),
					Math.round(inImage.getHeight() * scale));
			ImageIO.write(outImage, "png", outputFile);
			inImage.flush();
			outImage.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
