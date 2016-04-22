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
package es.eucm.piel.maven;

import com.badlogic.gdx.graphics.Texture.TextureFilter;
import es.eucm.piel.GenerateAtlas.AtlasConfig;
import es.eucm.piel.GenerateFonts.FontsConfig;
import es.eucm.piel.GenerateSkins;
import es.eucm.piel.GenerateSkins.SkinsConfig;
import es.eucm.piel.maven.GenerateFontsMojo.FontParameter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

@Mojo(name = "skins", requiresProject = false, inheritByDefault = false)
public class GenerateSkinsMojo extends AbstractMojo {

	/** Output folder for the atlas */
	@Parameter(property = "skin.input")
	private File input;

	@Parameter(property = "skin.scales")
	private String[] scales;

	@Parameter(property = "skin.ttfs")
	private FontParameter[] ttfs;

	/** Filter for the texture for the atlas **/
	@Parameter(property = "skin.filter", defaultValue = "Nearest")
	private TextureFilter filter;

	/** Size for the atlas pages, use for width and height. Must be a power of 2 **/
	@Parameter(property = "skin.atlas.size", defaultValue = "1024")
	private Integer atlasSize;

	@Parameter(property = "skin.fonts.atlasSize", defaultValue = "1024")
	private Integer fontsAtlasSize;

	/** Size for the atlas pages, use for width and height. Must be a power of 2 **/
	@Parameter(property = "skin.atlas.size", defaultValue = "2048")
	private Integer maxSize;

	/** Name for the atlas name **/
	@Parameter(property = "skin.atlas.name", defaultValue = "atlas")
	private String atlasName;

	@Parameter(property = "skin.force", defaultValue = "false")
	private Boolean force;

	@Parameter(property = "skin.output")
	private File output;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		AtlasConfig atlasConfig = new AtlasConfig();
		atlasConfig.size = atlasSize;
		atlasConfig.atlasName = atlasName;
		atlasConfig.filter = filter;
		if (fontsAtlasSize == null) {
			fontsAtlasSize = atlasSize / 2;
		}
		FontsConfig fontsConfig = Utils
				.fontConfig(scales, ttfs, fontsAtlasSize);
		new GenerateSkins().execute(input, output, new SkinsConfig(),
				fontsConfig, atlasConfig);

	}
}
