package com.nomsic.randb.persistence.xml;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nomsic.randb.BlockGroup;
import com.nomsic.randb.exception.InitException;
import com.nomsic.randb.exception.RandbException;
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

	private Map<String, BlockGroup> bgmap = new HashMap<String, BlockGroup>();

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
				e.printStackTrace();
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
			e.printStackTrace();
		}
	}

	private File getStudyFile(String name) {
		String indexpath = dataFolder + File.separator + name;
		File indexFile = new File(indexpath);
		return indexFile;
	}

	@Override
	public BlockGroup load(String name) throws RandbException{
		if (bgmap.containsKey(name)){
			log.debug("BlockGroup [name={}] loaded from cache", name);
			return bgmap.get(name);
		} else {
			String file = index.getBlockGroupFilename(name);
			log.debug("Loading BlockGroup [name={}] from file [file={}]", name, file);
			BlockGroup bg = (BlockGroup) jaxb.read(getStudyFile(file));
			bgmap.put(name, bg);
			return bg;
		}
	}
	
	@Override
	public void save(BlockGroup bg) throws RandbException{
		String name = bg.getName();
		String fileName = index.getBlockGroupFilename(name);
		if (fileName != null){
			log.debug("Writing BlockGroup [name={}] to file [file={}]", name, fileName);
			jaxb.write(bg, new File(fileName));
		} else {
			fileName = new Date().getTime() + RANDB_XML;
			log.debug("Writing BlockGroup [name={}] to file [file={}]", name, fileName);
			jaxb.write(bg, new File(dataFolder + File.separator + fileName));
			
			log.debug("Adding BlockGroup [name={}] to index", name);
			index.addBlockGroup(name, fileName);
			writeIndex();
		}
		bgmap.put(name, bg);
	}
	
	/*package private*/ Index getIndex() {
		return index;
	}

}
