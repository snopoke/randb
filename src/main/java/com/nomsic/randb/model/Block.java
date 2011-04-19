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
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A block consists of a number of cells. A block is used once
 * all the cells in the block are used.
 * 
 * @author Simon Kelly
 */
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
		this.cells = new ArrayList<Cell>(cells);
	}

	/**
	 * @return unmodifiable list of cells
	 */
	public List<Cell> getCells() {
		return Collections.unmodifiableList(cells);
	}
	
	public boolean isUsed(){
		return used != null && used.equals(Boolean.TRUE);
	}
	
	/*package private*/ void markUsed(){
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

	public static Block generateBlock(int size, List<String> groups){
		List<Cell> c = new ArrayList<Cell>();
		for (int i = 0; i < size; i++) {
			int j = i%groups.size();
			String gp = groups.get(j);
			c.add(new Cell(false, gp));
		}
		
		Collections.shuffle(c);
		return new Block(c);
	}

	public Cell getCell(int i) {
		return cells.get(i);
	}
}
