package com.nomsic.randb.persistence.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import com.nomsic.randb.BlockGroup;
import com.nomsic.randb.exception.RandbException;

public class RandbXMLPersistenceProviderTest {

	private static final String TEST_DATA_FOLDER = "target/testDataFolder";
	private RandbXMLPersistenceProvider provider;

	@Before
	public void setup() throws IOException{
		FileUtils.deleteDirectory(new File(TEST_DATA_FOLDER));
		provider = new RandbXMLPersistenceProvider(TEST_DATA_FOLDER);
	}
	
	@Test
	public void testInit(){
		File file = new File(TEST_DATA_FOLDER);
		Assert.assertTrue(file.exists());
		Assert.assertTrue(file.isDirectory());
		
		file = new File(TEST_DATA_FOLDER + File.separator + "index.xml");
		Assert.assertTrue(file.exists());
	}
	
	@Test
	public void testInitExisting() throws RandbException, FileNotFoundException, IOException{
		String name = "testName";
		provider.save(BlockGroup.generateArm(name, 2, new int[]{2}, new String[]{"A","B"}));
		
		File file = new File(TEST_DATA_FOLDER + File.separator + "index.xml");
		Assert.assertTrue(file.exists());
		String indexString = getFileAsString(file);
		Assert.assertTrue(indexString.contains(name));
		
		provider = new RandbXMLPersistenceProvider(TEST_DATA_FOLDER);
		Index index = provider.getIndex();
		Assert.assertTrue(index.blockGroups.containsKey(name));
	}

	private String getFileAsString(File file) throws FileNotFoundException,
			IOException {
		FileReader fileReader = new FileReader(file);
		String indexString = IOUtils.toString(fileReader);
		fileReader.close();
		return indexString;
	}
	
	@Test
	public void testSave() throws RandbException, FileNotFoundException, IOException{
		String name = "testName";
		provider.save(BlockGroup.generateArm(name, 2, new int[]{2}, new String[]{"A","B"}));
		
		File file = new File(TEST_DATA_FOLDER + File.separator + "index.xml");
		String indexString = getFileAsString(file);
		Assert.assertTrue(indexString.contains(name));
		
		String blockGroupFilename = provider.getIndex().getBlockGroupFilename(name);
		file = new File(TEST_DATA_FOLDER + File.separator + blockGroupFilename);
		Assert.assertTrue(file.getAbsolutePath(),file.exists());
		String bgString = getFileAsString(file);
		Assert.assertTrue(bgString.contains(name));
	}
	
	@Test
	public void testLoad() throws RandbException, FileNotFoundException, IOException{
		String name = "testName";
		provider.save(BlockGroup.generateArm(name, 2, new int[]{2}, new String[]{"A","B"}));
		
		provider = new RandbXMLPersistenceProvider(TEST_DATA_FOLDER);
		BlockGroup load = provider.load(name);
		Assert.assertNotNull(load);
		Assert.assertEquals(name, load.getName());
	}
}
