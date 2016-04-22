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

import es.eucm.piel.GeneratePNGs;
import es.eucm.piel.GenerateScales.ScalesConfig;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

@Mojo(name = "pngs", requiresProject = false, inheritByDefault = false)
public class GeneratePNGsMojo extends AbstractMojo {

	/**
	 * Folder with images to generate the atlas. If this folder contains other
	 * folders, it generates an atlas per folder
	 */
	@Parameter(property = "png.svg")
	private File svg;

	@Parameter(property = "png.ninePatch")
	private File ninePatch;

	/** Output folder for the atlas */
	@Parameter(property = "png.output")
	private File output;

	@Parameter(property = "png.scales")
	private String[] scales;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("PNGs generation for " + scales.length + " scales");
		ScalesConfig config = new ScalesConfig();
		config.scales = Utils.toFloat(scales);
		new GeneratePNGs().execute(svg, ninePatch, output, config);
	}
}
