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
package es.eucm.maven.plugins.piel.fonts;

import com.badlogic.gdx.backends.lwjgl.LwjglNativesLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeBitmapFontData;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.tools.bmfont.BitmapFontWriter;

import java.io.File;

public class TTFtoFNT {

	private final FileHandle ttfFh;

	private String ttfName;

	private FreeTypeFontGenerator generator;

	public TTFtoFNT(File ttf) {
		LwjglNativesLoader.load();
		ttfFh = new FileHandle(ttf);
		generator = new FreeTypeFontGenerator(ttfFh);
		ttfName = ttfFh.nameWithoutExtension();
	}

	public void toFnt(int size, float scale, File dst, int atlasSize) {
		int fontSize = (int) (size * scale);
		PixmapPacker packer = new PixmapPacker(atlasSize, atlasSize,
				Pixmap.Format.RGBA8888, 2, false);

		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.packer = packer;
		parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		parameter.size = fontSize;
		parameter.flip = false;

		FreeTypeBitmapFontData data = generator.generateData(parameter);

		String fontName = ttfName + "-" + size;

		BitmapFontWriter.setOutputFormat(BitmapFontWriter.OutputFormat.Text);
		String[] pageRefs = BitmapFontWriter.writePixmaps(
				parameter.packer.getPages(), new FileHandle(dst), fontName);

		BitmapFontWriter.writeFont(data, pageRefs, new FileHandle(new File(dst,
				fontName + ".fnt")), new BitmapFontWriter.FontInfo(fontName,
				size), 1, 1);
	}
}
