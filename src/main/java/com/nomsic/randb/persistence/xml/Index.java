/*
 * This file is part of Randb.
 *
 * Randb is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * Randb is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Randb.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.nomsic.randb.persistence.xml;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Keeps a record of which files belong to which block group.
 * Format:
 * 		<name>:<filename>
 * @author Simon Kelly
 */
@XmlRootElement(name="index")
public class Index {
	
	@XmlElement(name="bgs")
	@XmlJavaTypeAdapter(value=MapAdapter.class)
	HashMap<String,String> blockGroups = new HashMap<String, String>();
	
	/**
	 * @return index map
	 */
	public Map<String, String> getIndexMap() {
		return Collections.unmodifiableMap(blockGroups);
	}
	
	public void addBlockGroup(String name, String fileName){
		blockGroups.put(name, fileName);
	}
	
	public String getBlockGroupFilename(String name){
		return blockGroups.get(name);
	}

	public boolean contains(String name) {
		return blockGroups.containsKey(name);
	}

}
