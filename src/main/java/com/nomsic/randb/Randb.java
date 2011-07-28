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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nomsic.randb.exception.RandbException;
import com.nomsic.randb.model.Block;
import com.nomsic.randb.model.BlockGroup;
import com.nomsic.randb.model.Cell;
import com.nomsic.randb.persistence.RandbPersistenceProvider;

public class Randb {

	private static final Logger log = LoggerFactory
			.getLogger(Randb.class);

	private RandbPersistenceProvider persistenceProvider;
	private int autogenerateNum = 5;
	private boolean autogenerate = true;

	public Randb(RandbPersistenceProvider persistenceProvider) {
		this.persistenceProvider = persistenceProvider;
	}

	public void setAutogenerate(boolean autogenerate) {
		this.autogenerate = autogenerate;
	}

	public void setAutogenerateNum(int autogenerateNum) {
		this.autogenerateNum = autogenerateNum;
	}

	public BlockGroup createBlockGroup(String name, int blocks,
			List<Integer> blockSizes, List<String> groupNames) throws RandbException {
		BlockGroup blockGroup = getBlockGroup(name);
		if (blockGroup != null) {
			throw new IllegalArgumentException("BlockGroup with name '" + name
					+ "' already exists");
		}
		BlockGroup bg = BlockGroup.generate(name, blocks, blockSizes,
				groupNames);
		persistenceProvider.save(bg);
		return bg;
	}

	public boolean blockGroupExists(String name) {
		return persistenceProvider.checkExists(name);
	}

	/**
	 * @param name
	 * @return BlockGroup with name or null if not found
	 */
	public BlockGroup getBlockGroup(String name) {
		try {
			return persistenceProvider.load(name);
		} catch (RandbException e) {
			return null;
		}
	}

	public void saveBlockGroup(BlockGroup bg) throws RandbException {
		persistenceProvider.save(bg);
	}

	/**
	 * Not thread safe.
	 * 
	 * @param blockGroupName
	 * @return
	 * @throws RandbException
	 */
	public Cell getNextCell(String blockGroupName) throws RandbException {
		BlockGroup bg = persistenceProvider.load(blockGroupName);
		Cell nextUnused = bg.getNextUnused();
		if (nextUnused == null) {
			log.info("All cells used for BlockGroup [name={}]", blockGroupName);
			if (autogenerate) {
				appendBlocks(bg);
				nextUnused = bg.getNextUnused();
			}
		}
		return nextUnused;
	}
	
	public Cell getCell(String blockGroupName, String cellId) throws RandbException{
		BlockGroup bg = persistenceProvider.load(blockGroupName);
		return bg.getCell(cellId);
	}
	
	public void markAsUsed(String blockGroupName, Cell cell) throws RandbException{
		BlockGroup bg = persistenceProvider.load(blockGroupName);
		bg.markAsUsed(cell);
		persistenceProvider.save(bg);
	}

	private void appendBlocks(BlockGroup bg) throws RandbException {
		log.info("Autogenerating [{}] blocks for BlockGroup [name={}]",
				autogenerateNum, bg.getName());

		List<Integer> blockSizes = bg.getBlockSizes();

		List<Block> blocks = bg.getBlocks();
		if (blocks.isEmpty()) {
			throw new RandbException("No blocks exist in blockgroup:"
					+ bg.getName());
		}
		Block block = blocks.get(0);
		List<Cell> cells = block.getCells();
		if (cells.isEmpty()) {
			throw new RandbException("No cells exist in block");
		}
		List<String> groups = new ArrayList<String>();
		for (Cell cell : cells) {
			if (!groups.contains(cell.getGroup())){
				groups.add(cell.getGroup());
			}
		}

		if (log.isTraceEnabled()) {
			log.trace("Generator config [groups={}] [sizes={}]", groups,
					blockSizes);
		}

		BlockGroup generate = BlockGroup.generate("", autogenerateNum, blockSizes, groups);
		generate.getBlocks();
		bg.addBlocks(generate.getBlocks());
	}

}
