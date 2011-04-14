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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class MapAdapter extends XmlAdapter<HashMapType, Map<String, String>> {

	@Override
	public HashMapType marshal(Map<String, String> arg0) throws Exception {
		Set<Entry<String,String>> entrySet = arg0.entrySet();
		HashMapType hashMapType = new HashMapType();
		hashMapType.entry = new ArrayList<HashMapEntryType>();
		
		for (Entry<String, String> entry : entrySet) {
			HashMapEntryType entryType = new HashMapEntryType();
			entryType.key = entry.getKey();
			entryType.value = entry.getValue();
			hashMapType.entry.add(entryType);
		}
		return hashMapType;
	}

	@Override
	public Map<String, String> unmarshal(HashMapType arg0) throws Exception {
		Map<String, String> r = new HashMap<String, String>();
		
		for (HashMapEntryType mapelement : arg0.entry){
			r.put(mapelement.key, mapelement.value);
		}
		
		return r;
	}
}
