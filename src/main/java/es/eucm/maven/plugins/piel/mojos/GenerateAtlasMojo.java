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
package es.eucm.maven.plugins.piel.mojos;

import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.math.MathUtils;
import es.eucm.maven.plugins.piel.GenerateAtlas;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

@Mojo(name = "atlas", requiresProject = false, inheritByDefault = false)
public class GenerateAtlasMojo extends AbstractMojo {

	/**
	 * Folder with images to generate the atlas. If this folder contains other
	 * folders, it generates an atlas per folder
	 */
	@Parameter(property = "atlas.sourceDir")
	private File sourceDir;

	/** Output folder for the atlas */
	@Parameter(property = "atlas.outputDir")
	private File outputDir;

	/** Filter for the texture for the atlas **/
	@Parameter(property = "atlas.filter", defaultValue = "Nearest")
	private TextureFilter filter;

	/** Size for the atlas pages, use for width and height. Must be a power of 2 **/
	@Parameter(property = "atlas.size", defaultValue = "1024")
	private Integer size;

	/** Name for the atlas name **/
	@Parameter(property = "atlas.name", defaultValue = "atlas")
	private String atlasName;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		if (sourceDir == null || !sourceDir.exists()) {
			throw new MojoExecutionException("Invalid source directory: "
					+ sourceDir);
		}

		if (outputDir == null) {
			throw new MojoExecutionException("Invalid output directory: "
					+ sourceDir);
		}

		if (!outputDir.exists()) {
			if (!outputDir.mkdir()) {
				throw new MojoExecutionException(
						"Unable to create output directory: " + outputDir);
			}
		}

		if (!MathUtils.isPowerOfTwo(size)) {
			throw new MojoExecutionException("size should be a power of 2");
		}

		new GenerateAtlas(sourceDir, outputDir, filter, size, null, atlasName,
				false).execute();
	}
}
