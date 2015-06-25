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

import java.io.File;
import java.util.Properties;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.badlogic.gdx.graphics.Texture.TextureFilter;
import es.eucm.maven.plugins.piel.GenerateSkins;

@Mojo(name = "skins", requiresProject = false, inheritByDefault = false)
public class GenerateSkinsMojo extends AbstractMojo {

	/**
	 * Folder with images to generate the atlas. If this folder contains other
	 * folders, it generates an atlas per folder
	 */
	@Parameter(property = "skin.svg")
	private File svgDir;

	@Parameter(property = "skin.images")
	private File imageDir;

	@Parameter(property = "skin.ninePatch")
	private File ninePatchDir;

	/** Output folder for the atlas */
	@Parameter(property = "skin.png")
	private File outputPngDir;

	@Parameter(property = "skin.scales")
	private String[] scales;

	@Parameter(property = "skin.ttfs")
	private Properties ttfs;

	/** Filter for the texture for the atlas **/
	@Parameter(property = "skin.atlas.filter", defaultValue = "Nearest")
	private TextureFilter filter;

	/** Size for the atlas pages, use for width and height. Must be a power of 2 **/
	@Parameter(property = "skin.atlas.size", defaultValue = "1024")
	private Integer size;

	/** Size for the atlas pages, use for width and height. Must be a power of 2 **/
	@Parameter(property = "skin.atlas.size", defaultValue = "2048")
	private Integer maxSize;

	/** Name for the atlas name **/
	@Parameter(property = "skin.atlas.name", defaultValue = "atlas")
	private String atlasName;

	@Parameter(property = "skin.output")
	private File outputDir;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		new GenerateSkins(imageDir, svgDir, ninePatchDir, outputPngDir, scales,
				ttfs, filter, size, maxSize, atlasName, outputDir).execute();
	}
}
