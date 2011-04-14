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
package com.nomsic.randb.persistence.xml;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class BooleanAdapter extends XmlAdapter<Integer, Boolean>
{
    @Override
    public Boolean unmarshal( Integer s )
    {
        return s == null ? null : s == 1;
    }

    @Override
    public Integer marshal( Boolean c )
    {
        return c == null ? null : c ? 1 : 0;
    }
}