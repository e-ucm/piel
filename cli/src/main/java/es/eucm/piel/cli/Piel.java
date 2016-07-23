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
package es.eucm.piel.cli;

import org.apache.commons.cli.Options;

public class Piel {

	public static void main(String[] args) {

		Options options = new Options();

		options.addOption("png", false, "Converts SVG graphics into PNG images");

		options.addOption("i", true, "Input folder");
		options.addOption("o", true, "Output folder");
		options.addOption("s", true, "Scales");

	}
}
