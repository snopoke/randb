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
