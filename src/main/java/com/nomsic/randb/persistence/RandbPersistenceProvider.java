package com.nomsic.randb.persistence;

import com.nomsic.randb.BlockGroup;
import com.nomsic.randb.exception.RandbException;

public interface RandbPersistenceProvider {

	public abstract BlockGroup load(String name) throws RandbException;

	public abstract void save(BlockGroup bg) throws RandbException;

}