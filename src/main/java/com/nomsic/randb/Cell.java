package com.nomsic.randb;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

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
	
	@Override
	public String toString() {
		String string = MessageFormat.format(SHORT_FORMAT, 
				group,
				used ? USED_TEXT : UNUSED_TEXT,
				uuid);
		return string;
	}

	public void markUsed() {
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