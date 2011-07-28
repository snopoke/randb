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
package com.nomsic.randb.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a group of blocks with a name
 * 
 * @author Simon Kelly
 */
@XmlRootElement(name="group")
public class BlockGroup {

	@XmlAttribute(name="name")
	private String name;
	
	@XmlElement(name="block")
	private List<Block> blocks;
	
	@XmlAttribute(name="sizes")
	private List<Integer> blockSizes = new ArrayList<Integer>();
	
	public BlockGroup() {
	}

	public BlockGroup(String name, List<Block> blocks) {
		super();
		this.name = name;
		this.blocks = new ArrayList<Block>(blocks);
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * @return @return unmodifiable list of Blocks
	 */
	public List<Block> getBlocks() {
		return Collections.unmodifiableList(blocks);
	}
	
	/**
	 * @return @return unmodifiable set of block sizes
	 */
	public List<Integer> getBlockSizes() {
		return Collections.unmodifiableList(blockSizes);
	}
	
	public void addBlock(Block block){
		if (blocks == null){
			blocks = new ArrayList<Block>();
		}
		if (!blockSizes.contains(block.size())){
			blockSizes.add(block.size());
		}
		blocks.add(block);
	}
	
	public void addBlocks(Collection<Block> newBlocks){
		if (blocks == null){
			blocks = new ArrayList<Block>();
		}
		for (Block b : newBlocks) {
			if (!blockSizes.contains(b.size())){
				blockSizes.add(b.size());
			}
			blocks.add(b);
		}
	}
	
	public Cell getNextUnused(){
		if (blocks == null){
			return null;
		}
		
		for (Block block : blocks) {
			if (block.isUsed()){
				continue;
			}
			
			for (int i = 0; i < block.size(); i++) {
				Cell c = block.getCell(i);
				if (!c.isUsed()){
					Cell clone = c.clone();
					return clone;
				}
			}
		}
		return null;
	}
	
	public Cell getCell(String cellId) {
		for (Block block : blocks) {
			for (Cell cell : block.getCells()) {
				if (cell.getUuid().toString().equals(cellId)){
					return cell;
				}
			}
		}
		return null;
	}
	
	public Block getBlockContainingCell(Cell cell){
		for (Block block : blocks) {
			if (cell.isUsed() != block.isUsed()){
				continue;
			}

			for (Cell c : block.getCells()) {
				if (cell.getUuid().equals(c.getUuid())){
					return block;
				}
			}
		}
		return null;
	}
	
	public void markAsUsed(Cell cell) {
		if (cell.isUsed())
			return;
		
		Block block = getBlockContainingCell(cell);
		
		for (int i = 0; i < block.size(); i++) {
			Cell c = block.getCell(i);
			if (c.getUuid().equals(cell.getUuid())){
				c.markUsed();	
				if (i == block.size()-1){
					block.markUsed();
				}
				return;
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Group [name=").append(name).append(", blocks=")
				.append(blocks).append("]");
		return builder.toString();
	}

	/**
	 * Generate a BlockGroup. 
	 * 
	 * Note that due to randomly selecting block sizes it is possible that not
	 * all block sizes will be used.
	 * 
	 * @param name 
	 * @param blocks the number of blocks to generate
	 * @param blockSizes an array of sizes to make the blocks
	 * @param groups an array of group names to use for the cells
	 * 
	 * @return
	 */
	public static BlockGroup generate(String name,int blocks, List<Integer> blockSizes, List<String> groups){
		Random random = new Random();
		BlockGroup bg = new BlockGroup();
		bg.name = name;
		
		for (int i = 0; i < blocks; i++) {
			int index = random.nextInt(blockSizes.size());
			int blockSize = blockSizes.get(index);
			Block block = Block.generateBlock(blockSize,groups);
			bg.addBlock(block);
		}
		return bg;
	}

}
