/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.engedu.blackhole

import junit.framework.TestCase.assertEquals
import org.junit.Assert
import org.junit.Test

class BlackHoleBoardTest {
    private val b = BlackHoleBoard()
    @Test
    fun testCoordsToIndex() {
        assertEquals(0, b.coordsToIndex(0, 0))
        assertEquals(1, b.coordsToIndex(0, 1))
        assertEquals(2, b.coordsToIndex(1, 1))
        assertEquals(3, b.coordsToIndex(0, 2))
        assertEquals(4, b.coordsToIndex(1, 2))
        assertEquals(5, b.coordsToIndex(2, 2))
    }

    @Test
    fun testIndexToCoords() {
        var coordinate = b.indexToCoords(0)!!
        Assert.assertEquals(0, coordinate.x.toLong())
        Assert.assertEquals(0, coordinate.y.toLong())
        coordinate = b.indexToCoords(1)!!
        Assert.assertEquals(0, coordinate.x.toLong())
        Assert.assertEquals(1, coordinate.y.toLong())
        for (i in 0 until BlackHoleBoard.BOARD_SIZE) {
            coordinate = b.indexToCoords(i)!!
            Assert.assertEquals(i.toLong(), b.coordsToIndex(coordinate.x, coordinate.y).toLong())
        }
    }

    @Test
    fun testGetScore() {

    }
}