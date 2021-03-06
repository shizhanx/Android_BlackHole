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

import android.util.Log
import java.util.*
import kotlin.random.Random

/* Class that represent the state of the game.
 * Note that the buttons on screen are not updated by this class.
 */
class BlackHoleBoard {
    // The tiles for this board.
    var tiles: Array<BlackHoleTile?>

    // Getter for the number of the current player.
    // The number of the current player. 0 for user, 1 for computer.
    var currentPlayer = 0
        private set

    // The value to assign to the next move of each player.
    private var nextMove = intArrayOf(1, 1)

    // Copy board state from another board. Usually you would use a copy constructor instead but
    // object allocation is expensive on Android so we'll reuse a board instead.
    fun copyBoardState(other: BlackHoleBoard) {
        tiles = other.tiles.clone()
        currentPlayer = other.currentPlayer
        nextMove = other.nextMove.clone()
    }

    // Reset this board to its default state.
    fun reset() {
        currentPlayer = 0
        nextMove[0] = 1
        nextMove[1] = 1
        for (i in 0 until BOARD_SIZE) {
            tiles[i] = null
        }
    }

    // Translates column and row coordinates to a location in the array that we use to store the
    // board.
    fun coordsToIndex(col: Int, row: Int): Int {
        return col + row * (row + 1) / 2
    }

    // This is the inverse of the method above.
    fun indexToCoords(i: Int): Coordinates? {
        if (i < 0 || i >= BOARD_SIZE) return null
        var count = 0
        var row = 1
        while (count + row < i + 1) {
            count += row
            row++
        }
        return Coordinates(i - count, row - 1)
    }

    // Getter for the number of the player's next move.
    val currentPlayerValue: Int
        get() = nextMove[currentPlayer]

    // Check whether the current game is over (only one blank tile).
    fun gameOver(): Boolean {
        var empty = -1
        for (i in 0 until BOARD_SIZE) {
            if (tiles[i] == null) {
                empty = if (empty == -1) {
                    i
                } else {
                    return false
                }
            }
        }
        return true
    }

    // Pick a random valid move on the board. Returns the array index of the position to play.
    fun pickRandomMove(): Int {
        val possibleMoves = mutableListOf<Int>()
        for (i in 0 until BOARD_SIZE) {
            if (tiles[i] == null) {
                possibleMoves.add(i)
            }
        }
        return possibleMoves.random()
    }

    // Pick a good move for the computer to make. Returns the array index of the position to play.
    fun pickMove(): Int {
        val map = mutableMapOf<Int, MutableList<Int>>()
        for (simulation in 1..NUM_GAMES_TO_SIMULATE) {
            val simulate = BlackHoleBoard().apply {
                copyBoardState(this@BlackHoleBoard)
            }
            var firstMove = -1
            while (!simulate.gameOver()) {
                val move = simulate.pickRandomMove()
                firstMove = if (firstMove == -1) move else firstMove
                simulate.setValue(move)
            }
            map.putIfAbsent(firstMove, mutableListOf())
            map[firstMove]!!.add(simulate.score)
//        Log.d("sb", simulate.tiles.filterNotNull().size.toString())
        }
        return map.minByOrNull {
            it.value.average()
        }!!.key
//        return pickRandomMove()
    }

    // Makes the next move on the board at position i. Automatically updates the current player.
    fun setValue(i: Int) {
        tiles[i] = BlackHoleTile(currentPlayer, nextMove[currentPlayer])
        nextMove[currentPlayer]++
        currentPlayer++
        currentPlayer %= 2
    }

    val score: Int
        get() {
            return if (gameOver()) {
                var ans = 0
                for (i in tiles.indices) {
                    if (tiles[i] == null) {
                        val neighbours = getNeighbors(indexToCoords(i)!!)
                        for (tile in neighbours) {
                            if (tile.player == 0) ans -= tile.value else ans += tile.value
                        }
                    }
                }
                ans
            } else 0
        }

    // Helper for getScore that finds all the tiles around the given coordinates.
    fun getNeighbors(coords: Coordinates): List<BlackHoleTile> {
        val result = mutableListOf<BlackHoleTile>()
        for (pair in NEIGHBORS) {
            val n = safeGetTile(coords.x + pair.x, coords.y + pair.y)
            if (n != null) {
                result.add(n)
            }
        }
        return result.toList()
    }

    // Helper for getNeighbors that gets a tile at the given column and row but protects against
    // array over/underflow.
    private fun safeGetTile(col: Int, row: Int): BlackHoleTile? {
        if (row < 0 || col < 0 || col > row) {
            return null
        }
        val index = coordsToIndex(col, row)
        return if (index >= BOARD_SIZE) {
            null
        } else tiles[index]
    }

    companion object {
        // The number of turns each player will take.
        const val NUM_TURNS = 10

        // Size of the game board. Each player needs to take 10 turns and leave one empty tile.
        const val BOARD_SIZE = NUM_TURNS * 2 + 1

        // Relative position of the neighbors of each tile. This is a little tricky because of the
        // triangular shape of the board.
        val NEIGHBORS = arrayOf(Coordinates(-1, -1), Coordinates(0, -1), Coordinates(-1, 0), Coordinates(1, 0), Coordinates(0, 1), Coordinates(1, 1))

        // When we get to the Monte Carlo method, this will be the number of games to simulate.
        private const val NUM_GAMES_TO_SIMULATE = 2000
    }

    // Constructor. Nothing to see here.
    init {
        tiles = arrayOfNulls(BOARD_SIZE)
        reset()
    }
}