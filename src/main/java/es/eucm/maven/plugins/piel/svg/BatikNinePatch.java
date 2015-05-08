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
package es.eucm.maven.plugins.piel.svg;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import org.apache.batik.transcoder.image.PNGTranscoder;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BatikNinePatch extends BatikPNG {

	private static final int TRANSPARENT = Color.toIntBits(0, 0, 0, 0);

	private static final int BLACK = Color.toIntBits(0, 0, 0, 255);

	@Override
	public void convert(String svg, String png, float scale) {
		Vector2 size = size(new File(svg));

		File bordersFile = new File(png + ".borders.png");
		super.convert(svg, bordersFile.getAbsolutePath(), 1.0f);

		File centerFile = new File(png + ".tmp");

		Rectangle rectangle = new Rectangle();
		rectangle.x = rectangle.y = 1;
		rectangle.width = (int) (size.x - 2);
		rectangle.height = (int) (size.y - 2);
		pngTranscoder.addTranscodingHint(PNGTranscoder.KEY_AOI, rectangle);
		convertSize(svg, centerFile.getAbsolutePath(),
				(int) (scale * rectangle.width),
				(int) (scale * rectangle.height));
		pngTranscoder.removeTranscodingHint(PNGTranscoder.KEY_AOI);
		try {
			size.x = (size.x - 2) * scale + 2;
			size.y = (size.y - 2) * scale + 2;

			BufferedImage ninePatch = new BufferedImage((int) size.x,
					(int) size.y, BufferedImage.TYPE_4BYTE_ABGR);

			BufferedImage center = ImageIO.read(centerFile);
			BufferedImage bordersSource = ImageIO.read(bordersFile);

			Graphics2D g = (Graphics2D) ninePatch.getGraphics();
			g.drawImage(center, 1, 1, null);
			// Draw borders from original image scaled to the new image
			// Left border
			g.drawImage(bordersSource, 0, 0, 1, (int) size.y, 0, 0, 1,
					bordersSource.getHeight(), null);
			// Top border
			g.drawImage(bordersSource, 0, 0, (int) size.x, 1, 0, 0,
					bordersSource.getWidth(), 1, null);
			// Right border
			g.drawImage(bordersSource, (int) (size.x - 1), 0, (int) size.x,
					(int) size.y, bordersSource.getWidth() - 1, 0,
					bordersSource.getWidth(), bordersSource.getHeight(), null);
			// Bottom border
			g.drawImage(bordersSource, 0, (int) size.y, (int) size.x,
					(int) size.y - 1, 0, bordersSource.getHeight(),
					bordersSource.getWidth(), bordersSource.getHeight() - 1,
					null);

			// Remove gray areas
			removeGraysHorizontal(ninePatch);
			removeGraysVertical(ninePatch);

			ImageIO.write(ninePatch, "png", new File(png));

			centerFile.delete();
			bordersFile.delete();
			center.flush();
			bordersSource.flush();
			ninePatch.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void removeGraysHorizontal(BufferedImage image) {
		for (int x = 0; x < image.getWidth(); x++) {
			int color = image.getRGB(x, 0);
			if (color != BLACK) {
				image.setRGB(x, 0, TRANSPARENT);
			}

			color = image.getRGB(x, image.getHeight() - 1);
			if (color != BLACK) {
				image.setRGB(x, image.getHeight() - 1, TRANSPARENT);
			}
		}
	}

	private void removeGraysVertical(BufferedImage image) {
		for (int y = 0; y < image.getHeight(); y++) {
			int color = image.getRGB(0, y);
			if (color != BLACK) {
				image.setRGB(0, y, TRANSPARENT);
			}

			color = image.getRGB(image.getWidth() - 1, y);
			if (color != BLACK) {
				image.setRGB(image.getWidth() - 1, y, TRANSPARENT);
			}
		}
	}
}
