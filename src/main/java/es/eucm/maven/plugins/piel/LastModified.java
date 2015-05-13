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
package es.eucm.maven.plugins.piel;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class LastModified {

	public static final String FILE = ".lastmodified";

	private Map<String, Long> lastModified = new HashMap<String, Long>();

	private Array<String> removed = new Array<String>();

	private FileHandle fh;

	private boolean updated = false;

	public LastModified(File folder) {
		fh = new FileHandle(new File(folder, FILE));
		if (fh.exists()) {
			for (String line : fh.readString().replace("\r", "").split("\n")) {
				String[] parts = line.split(";");
				if (parts.length == 2) {
					lastModified.put(parts[0], Long.parseLong(parts[1]));
				}
			}
		}

		for (String file : lastModified.keySet()) {
			if (!fh.parent().child(file).exists()) {
				removed.add(file);
			}
		}

		if (removed.size > 0) {
			updated = true;
			for (String name : removed) {
				lastModified.remove(name);
			}
		}
	}

	public boolean isModified(File file) {
		Long modified = lastModified.get(file.getName());
		boolean result = modified == null || modified < file.lastModified();
		if (result) {
			lastModified.put(file.getName(), file.lastModified());
			updated = true;
		}
		return result;
	}

	public Array<String> getRemoved() {
		return removed;
	}

	public boolean commit() {
		if (updated) {
			updated = false;
			System.out.println("Updating " + FILE);
			String result = "";
			for (Entry<String, Long> entry : lastModified.entrySet()) {
				if (!removed.contains(entry.getKey(), true)) {
					result += entry.getKey() + ";" + entry.getValue() + "\n";
				}
			}
			fh.writeString(result, false);
			return true;
		} else {
			System.out.println("No SVG updates were found.");
			return false;
		}
	}
}
