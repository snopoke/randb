package com.nomsic.randb.persistence.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class HashMapEntryType {
	@XmlAttribute
	public String key;

	@XmlValue
	public String value;
}
