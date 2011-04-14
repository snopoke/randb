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

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nomsic.randb.exception.InitException;
import com.nomsic.randb.exception.RandbException;
import com.nomsic.randb.model.BlockGroup;
import com.nomsic.randb.persistence.RandbPersistenceProvider;

public class RandbXMLPersistenceProvider implements RandbPersistenceProvider {
	
	private static final Logger log = LoggerFactory.getLogger(RandbXMLPersistenceProvider.class);
	
	private static final String INDEX_XML = "index.xml";

	private static final String RANDB_XML = "-randb.xml";
	
	private String dataFolder;
	private File dataFolderFile;

	private JaxbUtil jaxb;

	private JaxbUtil jaxbIndex;

	private Index index;

	private Map<String, BlockGroup> cache = new HashMap<String, BlockGroup>();

	public RandbXMLPersistenceProvider(String dataFolder) {
		log.debug("Initialising XML persistence [datafolder={}]", dataFolder);
		try {
			jaxb = new JaxbUtil(BlockGroup.class);
			jaxbIndex = new JaxbUtil(Index.class);
		} catch (JAXBException e) {
			log.error("Error creating JAXB contexts", e);
			throw new InitException(e);
		}
		this.dataFolder = dataFolder;
		initialise();
	}
	
	private void initialise(){
		dataFolderFile = new File(dataFolder);
		boolean mkdir = dataFolderFile.mkdir();
		log.debug(mkdir ? "Data folder created" : "Data folder already exists");
		loadIndex();
	}
	
	private void loadIndex() {
		File indexFile = getStudyFile(INDEX_XML);
		if (indexFile.exists()){
			log.debug("Loading index");
			try {
				index = (Index) jaxbIndex.read(indexFile);
			} catch (RandbException e) {
				log.error("Error loading index", e);
			}
		} else {
			writeIndex();
		}
	}

	private void writeIndex() {
		if (index == null){
			log.debug("Creating new index");
			index = new Index();
		}
		
		try {
			log.debug("Writing index to file");
			jaxbIndex.write(index, getStudyFile(INDEX_XML));
		} catch (RandbException e) {
			log.error("Error writing index", e);
		}
	}

	private File getStudyFile(String name) {
		String indexpath = dataFolder + File.separator + name;
		File indexFile = new File(indexpath);
		return indexFile;
	}

	@Override
	public BlockGroup load(String name) throws RandbException{
		if (cache.containsKey(name)){
			log.debug("BlockGroup [name={}] loaded from cache", name);
			return cache.get(name);
		} else {
			String file = index.getBlockGroupFilename(name);
			log.debug("Loading BlockGroup [name={}] from file [file={}]", name, file);
			BlockGroup bg = (BlockGroup) jaxb.read(getStudyFile(file));
			cache.put(name, bg);
			return bg;
		}
	}
	
	@Override
	public void save(BlockGroup bg) throws RandbException{
		String name = bg.getName();
		String fileName = index.getBlockGroupFilename(name);
		if (fileName != null){
			log.debug("Writing BlockGroup [name={}] to file [file={}]", name, fileName);
			jaxb.write(bg, getStudyFile(fileName));
		} else {
			fileName = new Date().getTime() + RANDB_XML;
			log.debug("Writing BlockGroup [name={}] to file [file={}]", name, fileName);
			jaxb.write(bg, getStudyFile(fileName));
			
			log.debug("Adding BlockGroup [name={}] to index", name);
			index.addBlockGroup(name, fileName);
			writeIndex();
		}
		cache.put(name, bg);
	}
	
	@Override
	public boolean checkExists(String name) {
		return index.contains(name);
	}
	
	public Map<String, String> getIndexMap(){
		return index.getIndexMap();
	}
	
	/*package private*/ Index getIndex() {
		return index;
	}

	public void clearCache() {
		cache.clear();
	}

}
