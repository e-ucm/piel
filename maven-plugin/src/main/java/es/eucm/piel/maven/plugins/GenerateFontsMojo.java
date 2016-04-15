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
package es.eucm.piel.maven.plugins;

import es.eucm.piel.GenerateFonts;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

@Mojo(name = "fonts", requiresProject = false, inheritByDefault = false)
public class GenerateFontsMojo extends AbstractMojo {

	@Parameter(property = "font.ttfs")
	private FontParameter[] ttfs;

	/** Output folder for the atlas */
	@Parameter(property = "font.outputDir")
	private File outputDir;

	@Parameter(property = "font.scales")
	private String[] scales;

	@Parameter(property = "font.atlasSize", defaultValue = "1024")
	private Integer atlasSize;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		new GenerateFonts().execute(outputDir,
				Utils.fontConfig(scales, ttfs, atlasSize));
	}

	public static class FontParameter {

		public String file;

		public String sizes;

		public String characters;
	}
}
