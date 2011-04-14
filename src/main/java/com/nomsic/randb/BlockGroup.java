package com.nomsic.randb;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="group")
public class BlockGroup {

	@XmlAttribute(name="name")
	private String name;
	
	@XmlElement(name="block")
	private List<Block> blocks;
	
	public BlockGroup() {
	}

	public BlockGroup(String name, List<Block> blocks) {
		super();
		this.name = name;
		this.blocks = blocks;
	}
	
	public String getName() {
		return name;
	}
	
	public List<Block> getBlocks() {
		return blocks;
	}
	
	public void addBlock(Block block){
		if (blocks == null){
			blocks = new ArrayList<Block>();
		}
		blocks.add(block);
	}
	
	public Cell getNextUnused(){
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

	public static BlockGroup generateArm(String name,int blocks, int[] blockSizes, String[] groups){
		Random random = new Random();
		BlockGroup studyArm = new BlockGroup();
		studyArm.name = name;
		for (int i = 0; i < blocks; i++) {
			int index = random.nextInt(blockSizes.length);
			int blockSize = blockSizes[index];
			Block block = Block.generateBlock(blockSize,groups);
			studyArm.addBlock(block);
		}
		return studyArm;
	}
}
