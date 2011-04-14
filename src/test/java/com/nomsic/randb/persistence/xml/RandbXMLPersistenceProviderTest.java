package com.nomsic.randb.persistence.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import com.nomsic.randb.BlockGroup;
import com.nomsic.randb.exception.RandbException;
import com.nomsic.randb.persistence.xml.Index;
import com.nomsic.randb.persistence.xml.RandbXMLPersistenceProvider;
import com.nomsic.randb.test.TestUtils;

public class RandbXMLPersistenceProviderTest {

	private RandbXMLPersistenceProvider provider;

	@Before
	public void setup(){
		TestUtils.prepare();
		provider = new RandbXMLPersistenceProvider(TestUtils.TEST_DATA_FOLDER);
		
	}
	
	@Test
	public void testInit(){
		File file = new File(TestUtils.TEST_DATA_FOLDER);
		Assert.assertTrue(file.exists());
		Assert.assertTrue(file.isDirectory());
		
		file = new File(TestUtils.TEST_DATA_FOLDER + File.separator + "index.xml");
		Assert.assertTrue(file.exists());
	}
	
	@Test
	public void testInitExisting() throws RandbException, FileNotFoundException, IOException{
		String name = "testName";
		provider.save(BlockGroup.generateArm(name, 2, new int[]{2}, new String[]{"A","B"}));
		
		File file = new File(TestUtils.TEST_DATA_FOLDER + File.separator + "index.xml");
		Assert.assertTrue(file.exists());
		String indexString = IOUtils.toString(new FileReader(file));
		Assert.assertTrue(indexString.contains(name));
		
		provider = new RandbXMLPersistenceProvider(TestUtils.TEST_DATA_FOLDER);
		Index index = provider.getIndex();
		Assert.assertTrue(index.blockGroups.containsKey(name));
	}
	
	@Test
	public void testSave() throws RandbException, FileNotFoundException, IOException{
		String name = "testName";
		provider.save(BlockGroup.generateArm(name, 2, new int[]{2}, new String[]{"A","B"}));
		
		File file = new File(TestUtils.TEST_DATA_FOLDER + File.separator + "index.xml");
		Assert.assertTrue(file.exists());
		String indexString = IOUtils.toString(new FileReader(file));
		Assert.assertTrue(indexString.contains(name));
		
		String blockGroupFilename = provider.getIndex().getBlockGroupFilename(name);
		file = new File(TestUtils.TEST_DATA_FOLDER + File.separator + blockGroupFilename);
		Assert.assertTrue(file.exists());
		String bgString = IOUtils.toString(new FileReader(file));
		Assert.assertTrue(bgString.contains(name));
	}
	
	@Test
	public void testLoad() throws RandbException, FileNotFoundException, IOException{
		String name = "testName";
		provider.save(BlockGroup.generateArm(name, 2, new int[]{2}, new String[]{"A","B"}));
		
		provider = new RandbXMLPersistenceProvider(TestUtils.TEST_DATA_FOLDER);
		BlockGroup load = provider.load(name);
		Assert.assertNotNull(load);
		Assert.assertEquals(name, load.getName());
	}
}
