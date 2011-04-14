package com.nomsic.randb;

import com.nomsic.randb.exception.RandbException;
import com.nomsic.randb.persistence.RandbPersistenceProvider;

public class StudyManager {
	
	private RandbPersistenceProvider persistenceProvider;

	public StudyManager(RandbPersistenceProvider persistenceProvider) {
		this.persistenceProvider = persistenceProvider;
	}
	
	public BlockGroup createBlockGroup(String name, int blocks, int[] blockSizes, String[] groupNames) throws RandbException{
		BlockGroup bg = BlockGroup.generateArm(name, blocks, blockSizes, groupNames);
		persistenceProvider.save(bg);
		return bg;
	}
	
	public BlockGroup getBlockGroup(String name) throws RandbException {
		return persistenceProvider.load(name);
	}
	
	public Cell getNextCell(String blockGroupName) throws RandbException{
		BlockGroup bg = persistenceProvider.load(blockGroupName);
		return bg.getNextUnused();
	}

	
}
