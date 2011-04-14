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
package com.nomsic.randb.persistence;

import com.nomsic.randb.exception.RandbException;
import com.nomsic.randb.model.BlockGroup;

public interface RandbPersistenceProvider {

	public abstract BlockGroup load(String name) throws RandbException;

	public abstract void save(BlockGroup bg) throws RandbException;

	public abstract boolean checkExists(String name);

}