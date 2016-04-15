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
package es.eucm.piel.svg;

import com.badlogic.gdx.math.Vector2;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class BatikPNG extends DefaultHandler implements SVGtoPNG {

	private SAXParser saxParser;

	private float width;

	private float height;

	private String viewBox;

	private Map<String, Vector2> sizes = new HashMap<String, Vector2>();

	protected PNGTranscoder pngTranscoder;

	public BatikPNG() {
		pngTranscoder = new PNGTranscoder();

		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			saxParser = factory.newSAXParser();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void convert(String svg, String png, float scale) {
		Vector2 size = size(new File(svg));
		convertSize(svg, png, size.x * scale, size.y * scale);
	}

	protected void convertSize(String svg, String png, float width, float height) {
		try {
			pngTranscoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, width);
			pngTranscoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, height);

			TranscoderInput input = new TranscoderInput(new File(svg).toURL()
					.toString());
			OutputStream os = new FileOutputStream(new File(png));
			TranscoderOutput output = new TranscoderOutput(os);
			pngTranscoder.transcode(input, output);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected Vector2 size(File svg) {
		Vector2 size = sizes.get(svg.getAbsolutePath());
		if (size == null) {
			sizes.put(svg.getAbsolutePath(), size = new Vector2());
			try {
				try {
					width = height = 0;
					saxParser.parse(svg, this);
				} catch (Exception e) {

				}
				size.x = width;
				size.y = height;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new Vector2(size);
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		String width = attributes.getValue("width");
		String height = attributes.getValue("height");
		this.width = Float.parseFloat(width.replaceAll("([^0-9\\.])", ""));
		this.height = Float.parseFloat(height.replaceAll("([^0-9\\.])", ""));

		viewBox = attributes.getValue("viewBox");
		float viewBoxWidth = 1.0f;
		float viewBoxHeight = 1.0f;

		if (viewBox != null) {
			String[] parts = viewBox.split(" ");
			viewBoxWidth = Float.parseFloat(parts[2])
					- Float.parseFloat(parts[0]);
			viewBoxHeight = Float.parseFloat(parts[3])
					- Float.parseFloat(parts[1]);
		}

		if (width.endsWith("%")) {
			this.width = (this.width / 100.0f) * viewBoxWidth;
		}

		if (height.endsWith("%")) {
			this.height = (this.height / 100.0f) * viewBoxHeight;
		}
		throw new SAXException();
	}
}
