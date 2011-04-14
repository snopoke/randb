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

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class SetCsvAdapter extends XmlAdapter<String, Set<Integer>> {

	@Override
	public Set<Integer> unmarshal(String v) throws Exception {
		String[] split = v.split(",");
		HashSet<Integer> set = new HashSet<Integer>();
		for (String i: split) {
			set.add(Integer.valueOf(i));
		}
		return set;
	}

	@Override
	public String marshal(Set<Integer> v) throws Exception {
		StringBuffer sb = new StringBuffer();
		for (Integer integer : v) {
			sb.append(integer).append(",");
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		return sb.toString();
	}

}
