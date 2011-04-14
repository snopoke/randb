package com.nomsic.randb.persistence.xml;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name="index")
public class Index {
	
	@XmlElement(name="bgs")
	@XmlJavaTypeAdapter(value=MapAdapter.class)
	HashMap<String,String> blockGroups = new HashMap<String, String>();
	
	public Map<String, String> getBlockGroups() {
		return blockGroups;
	}
	
	public void addBlockGroup(String name, String fileName){
		blockGroups.put(name, fileName);
	}
	
	public String getBlockGroupFilename(String name){
		return blockGroups.get(name);
	}

}
