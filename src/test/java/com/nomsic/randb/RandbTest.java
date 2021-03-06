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
package com.nomsic.randb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import com.nomsic.randb.exception.RandbException;
import com.nomsic.randb.model.Block;
import com.nomsic.randb.model.BlockGroup;
import com.nomsic.randb.model.Cell;
import com.nomsic.randb.persistence.xml.RandbXMLPersistenceProvider;
import com.nomsic.randb.test.TestUtils;

public class RandbTest {

	private static final String TEST_DATA_FOLDER = "target/testDataFolder";
	private Randb manager;
	private RandbXMLPersistenceProvider provider;
	
	@Before
	public void setup() throws IOException {
		FileUtils.deleteDirectory(new File(TEST_DATA_FOLDER));
		provider = new RandbXMLPersistenceProvider(TEST_DATA_FOLDER);
		manager = new Randb(provider);
	}

	@Test
	public void testCreateBlockGroup() throws RandbException, FileNotFoundException, IOException{
		String name = "TEST";
		manager.createBlockGroup(name, 2,  Arrays.asList(new Integer[]{2,4}),
				Arrays.asList(new String[]{"A","B"}));
		
		String filename = provider.getIndexMap().get(name);
		File file = new File(TEST_DATA_FOLDER + File.separator + filename);
		Assert.assertTrue(file.exists());
		String fileString = TestUtils.getFileAsString(file);
		Assert.assertTrue(fileString.contains(name));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCreateBlockGroup_twice() throws RandbException, FileNotFoundException, IOException{
		String name = "TEST";
		manager.createBlockGroup(name, 2, Arrays.asList(new Integer[]{2,4}),
				Arrays.asList(new String[]{"A","B"}));
		manager.createBlockGroup(name, 2, Arrays.asList(new Integer[]{2,4}),
				Arrays.asList(new String[]{"A","B"}));
	}
	
	@Test
	public void testGetNextCell() throws RandbException, FileNotFoundException, IOException{
		String name = "TEST";
		manager.createBlockGroup(name, 2,  Arrays.asList(new Integer[]{2,4}),
				Arrays.asList(new String[]{"A","B"}));
		Cell nextCell = manager.getNextCell(name);
		
		provider.clearCache();
		
		BlockGroup bg = manager.getBlockGroup(name);
		Cell cell = bg.getBlocks().get(0).getCell(0);
		Assert.assertFalse(cell.isUsed());
		Assert.assertEquals(cell.getUuid(), nextCell.getUuid());
	}
	
	@Test
	public void testGetNextCell_end() throws RandbException, FileNotFoundException, IOException{
		String name = "TEST";
		manager.setAutogenerate(true);
		int autogenerateNum = 10;
		manager.setAutogenerateNum(autogenerateNum);
		manager.createBlockGroup(name, 2,  Arrays.asList(new Integer[]{2,4}),
				Arrays.asList(new String[]{"A","B"}));
		BlockGroup bg = manager.getBlockGroup(name);
		Cell cell = null;
		do{
			cell = bg.getNextUnused();
			if (cell != null)
				bg.markAsUsed(cell);
		} while (cell != null);

		int initialSize = bg.getBlocks().size();
		cell = manager.getNextCell(name);
		Assert.assertNotNull(cell);
		
		int sizeAfter = manager.getBlockGroup(name).getBlocks().size();
		Assert.assertEquals(initialSize + autogenerateNum, sizeAfter);
	}
	
	@Test
	public void testMarkUsed() throws RandbException{
		String name = "TEST";
		manager.createBlockGroup(name , 3,  Arrays.asList(new Integer[]{2,4}),
				Arrays.asList(new String[]{"A","B"}));
		
		Cell c1 = manager.getNextCell(name);
		manager.markAsUsed(name, c1);
		
		Cell c2 = manager.getNextCell(name);
		Assert.assertFalse(c1.getUuid().equals(c2.getUuid()));
		
		provider.clearCache();
		
		Cell c1a = manager.getCell(name, c1.getUuid().toString());
		Assert.assertTrue(c1a.isUsed());
	}

	@Test
	public void testMarkUsed_endOfBlock() throws RandbException{
		String name = "TEST";
		BlockGroup bg = manager.createBlockGroup(name , 3,  Arrays.asList(new Integer[]{2,4}),
				Arrays.asList(new String[]{"A","B"}));
		
		Block block = bg.getBlocks().get(0);
		List<Cell> cells = block.getCells();
		for (int i = 0; i < cells.size(); i++) {
			Cell c1 = manager.getNextCell(name);
			manager.markAsUsed(name, c1);
		}
		
		provider.clearCache();
		
		Block block2 = manager.getBlockGroup(name).getBlocks().get(0);
		Assert.assertTrue(block2.isUsed());
	}
}
