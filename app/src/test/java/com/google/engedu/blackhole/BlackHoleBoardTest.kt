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

package com.google.engedu.blackhole;

import android.util.Pair;

import org.junit.Test;
import static org.junit.Assert.*;

public class BlackHoleBoardTest {
    @Test
    public void testCoordsToIndex() {
        BlackHoleBoard b = new BlackHoleBoard();
        assertEquals(0, b.coordsToIndex(0, 0));
        assertEquals(1, b.coordsToIndex(0, 1));
        assertEquals(2, b.coordsToIndex(1, 1));
        assertEquals(3, b.coordsToIndex(0, 2));
        assertEquals(4, b.coordsToIndex(1, 2));
        assertEquals(5, b.coordsToIndex(2, 2));
    }

    @Test
    public void testIndexToCoords() {
        BlackHoleBoard b = new BlackHoleBoard();
        Coordinates coords = b.indexToCoords(0);
        assert coords != null;
        assertEquals(0, coords.getX());
        assertEquals(0, coords.getY());
        coords = b.indexToCoords(1);
        assert coords != null;
        assertEquals(0, coords.getX());
        assertEquals(1, coords.getY());
        for (int i = 0; i < BlackHoleBoard.BOARD_SIZE; i++) {
            coords = b.indexToCoords(i);
            assert coords != null;
            assertEquals(i, b.coordsToIndex(coords.getX(), coords.getY()));
        }
    }

    @Test
    public void testGetScore() {
        // TODO: Implement this test to verify that your getScore method is working.
    }
}
