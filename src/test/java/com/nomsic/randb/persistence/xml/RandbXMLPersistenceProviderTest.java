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
import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import com.nomsic.randb.exception.RandbException;
import com.nomsic.randb.model.BlockGroup;
import com.nomsic.randb.test.TestUtils;

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
		provider.save(BlockGroup.generate(name, 2, new int[]{2}, new String[]{"A","B"}));
		
		File file = new File(TEST_DATA_FOLDER + File.separator + "index.xml");
		Assert.assertTrue(file.exists());
		String indexString = TestUtils.getFileAsString(file);
		Assert.assertTrue(indexString.contains(name));
		
		provider = new RandbXMLPersistenceProvider(TEST_DATA_FOLDER);
		Index index = provider.getIndex();
		Assert.assertTrue(index.blockGroups.containsKey(name));
	}

	@Test
	public void testSave() throws RandbException, FileNotFoundException, IOException{
		String name = "testName";
		provider.save(BlockGroup.generate(name, 2, new int[]{2}, new String[]{"A","B"}));
		
		File file = new File(TEST_DATA_FOLDER + File.separator + "index.xml");
		String indexString = TestUtils.getFileAsString(file);
		Assert.assertTrue(indexString.contains(name));
		
		String blockGroupFilename = provider.getIndex().getBlockGroupFilename(name);
		file = new File(TEST_DATA_FOLDER + File.separator + blockGroupFilename);
		Assert.assertTrue(file.getAbsolutePath(),file.exists());
		String bgString = TestUtils.getFileAsString(file);
		Assert.assertTrue(bgString.contains(name));
	}
	
	@Test
	public void testLoad() throws RandbException, FileNotFoundException, IOException{
		String name = "testName";
		provider.save(BlockGroup.generate(name, 2, new int[]{2}, new String[]{"A","B"}));
		
		provider = new RandbXMLPersistenceProvider(TEST_DATA_FOLDER);
		BlockGroup load = provider.load(name);
		Assert.assertNotNull(load);
		Assert.assertEquals(name, load.getName());
	}
}
