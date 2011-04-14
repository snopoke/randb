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
		index.addBlockGroup(key, value);
		jaxbUtil.write(index, file);
		
		index = (Index) jaxbUtil.read(file);
		Assert.assertTrue(index.getIndexMap().containsKey(key));
		Assert.assertEquals(value, index.getIndexMap().get(key));
	}
	
	@Test
	public void testIndex() throws RandbException, FileNotFoundException, IOException{
		File file = new File(TARGET_TEST_DATA_INDEX_XML);
		Index index = new Index();
		String key = "name";
		String value = "filename";
		index.addBlockGroup(key, value);
		jaxbUtil.write(index, file);
		
		Assert.assertTrue(file.exists());
		String indexString = IOUtils.toString(new FileReader(file));
		Assert.assertTrue(indexString.contains(key));
		Assert.assertTrue(indexString.contains(value));
	}

}

