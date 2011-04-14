package com.nomsic.randb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="block")
public class Block {
	
	@XmlElement(name = "cell")
	private List<Cell> cells;
	
	@XmlAttribute(name="used")
	private Boolean used;
	
	public Block() {
	}
	
	public Block(List<Cell> cells) {
		super();
		this.cells = cells;
	}

	public List<Cell> getCells() {
		return cells;
	}
	
	public boolean isUsed(){
		return used != null && used.equals(Boolean.TRUE);
	}
	
	public void markUsed(){
		used = true;
	}
	
	public int size() {
		return cells.size();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Block [cells=")
		.append(cells).append("]");
		return builder.toString();
	}

	public static Block generateBlock(int size, String[] groups){
		List<Cell> c = new ArrayList<Cell>();
		for (int i = 0; i < size; i++) {
			int j = i%groups.length;
			String gp = groups[j];
			c.add(new Cell(false, gp));
		}
		
		Collections.shuffle(c);
		return new Block(c);
	}

	public Cell getCell(int i) {
		return cells.get(i);
	}
}
