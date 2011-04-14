package com.nomsic.randb.persistence.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import com.nomsic.randb.exception.RandbException;
import com.nomsic.randb.persistence.xml.Index;
import com.nomsic.randb.persistence.xml.JaxbUtil;

public class JaxbIndexTest {
	
	private static final String TARGET_TEST_DATA_INDEX_XML = "target/index-test.xml";
	private JaxbUtil jaxbUtil;

	@Before
	public void setup() throws JAXBException{
		jaxbUtil = new JaxbUtil(Index.class);
		File file = new File(TARGET_TEST_DATA_INDEX_XML);
		file.delete();
	}
	
	@Test
	public void testWrite() throws RandbException{
		File file = new File(TARGET_TEST_DATA_INDEX_XML);
		jaxbUtil.write(new Index(), file);
		Assert.assertTrue(file.exists());
	}
	
	@Test
	public void testRead() throws RandbException{
		File file = new File(TARGET_TEST_DATA_INDEX_XML);
		Index index = new Index();
		String key = "name";
		String value = "filename";
		index.getBlockGroups().put(key, value);
		jaxbUtil.write(index, file);
		
		index = (Index) jaxbUtil.read(file);
		Assert.assertTrue(index.getBlockGroups().containsKey(key));
		Assert.assertEquals(value, index.getBlockGroups().get(key));
	}
	
	@Test
	public void testIndex() throws RandbException, FileNotFoundException, IOException{
		File file = new File(TARGET_TEST_DATA_INDEX_XML);
		Index index = new Index();
		String key = "name";
		String value = "filename";
		index.getBlockGroups().put(key, value);
		jaxbUtil.write(index, file);
		
		Assert.assertTrue(file.exists());
		String indexString = IOUtils.toString(new FileReader(file));
		Assert.assertTrue(indexString.contains(key));
		Assert.assertTrue(indexString.contains(value));
	}

}

