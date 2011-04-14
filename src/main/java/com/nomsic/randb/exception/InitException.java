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
package com.nomsic.randb.exception;

public class InitException extends RandbRuntimeException {

	private static final long serialVersionUID = 4195930385826072954L;

	public InitException() {
		super();
	}

	public InitException(String message, Throwable cause) {
		super(message, cause);
	}

	public InitException(String message) {
		super(message);
	}

	public InitException(Throwable cause) {
		super(cause);
	}
	
}
