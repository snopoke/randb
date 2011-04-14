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
package com.nomsic.randb.model;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.nomsic.randb.persistence.xml.BooleanAdapter;

/**
 * A cell has a UUID, a group that it represents and can be used
 * or unused.
 * 
 * @author Simon Kelly
 */
@XmlRootElement(name = "cell")
public class Cell implements Serializable, Cloneable{
	
	private static final long serialVersionUID = -8248046543423462002L;
	
	private static final String SHORT_FORMAT = "{0}({1},{2})";
	private static final String UNUSED_TEXT = "0";
	private static final String USED_TEXT = "1";
	
	@XmlAttribute(name="id")
	private UUID uuid;
	
	@XmlAttribute(name="used")
	@XmlJavaTypeAdapter(type=Integer.class, value=BooleanAdapter.class)
	private Boolean used = false;
	
	@XmlAttribute(name="gp")
	private String group;

	public Cell() {
		uuid = UUID.randomUUID();
	}
	
	public Cell(boolean used, String group) {
		super();
		uuid = UUID.randomUUID();
		this.used = used;
		this.group = group;
	}

	public boolean isUsed() {
		return used;
	}
	
	public String getGroup() {
		return group;
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	@Override
	public String toString() {
		String string = MessageFormat.format(SHORT_FORMAT, 
				group,
				used ? USED_TEXT : UNUSED_TEXT,
				uuid);
		return string;
	}

	/*package private*/ void markUsed() {
		used = true;
	}
	
	@Override
	public Cell clone() {
		try {
			Cell cloned = (Cell) super.clone();
			return cloned;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}