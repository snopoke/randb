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
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.nomsic.randb.persistence.xml.SetCsvAdapter;

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
	@XmlJavaTypeAdapter(value=SetCsvAdapter.class)
	private Set<Integer> blockSizes = new HashSet<Integer>();
	
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
	public Set<Integer> getBlockSizes() {
		return Collections.unmodifiableSet(blockSizes);
	}
	
	public void addBlock(Block block){
		if (blocks == null){
			blocks = new ArrayList<Block>();
		}
		blockSizes.add(block.size());
		blocks.add(block);
	}
	
	public void addBlock(Collection<Block> blocks){
		if (blocks == null){
			blocks = new ArrayList<Block>();
		}
		for (Block b : blocks) {
			blockSizes.add(b.size());
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
					c.markUsed();	
					if (i == block.size()-1){
						block.markUsed();
					}
					return clone;
				}
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Group [name=").append(name).append(", blocks=")
				.append(blocks).append("]");
		return builder.toString();
	}

	public static BlockGroup generate(String name,int blocks, int[] blockSizes, String[] groups){
		Random random = new Random();
		BlockGroup bg = new BlockGroup();
		bg.name = name;
		for (int i = 0; i < blocks; i++) {
			int index = random.nextInt(blockSizes.length);
			int blockSize = blockSizes[index];
			bg.blockSizes.add(blockSize);
			Block block = Block.generateBlock(blockSize,groups);
			bg.addBlock(block);
		}
		return bg;
	}
}
