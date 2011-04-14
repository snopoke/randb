package com.nomsic.randb.persistence.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class HashMapType {
	@XmlElement
	List<HashMapEntryType> entry;
}
