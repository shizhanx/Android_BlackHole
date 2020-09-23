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

import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    // The main board instance.
    private var board: BlackHoleBoard? = null

    // Initialize the board on launch.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        board = BlackHoleBoard()
        onReset(null)
    }

    /* Shared handler for all the game buttons. When the user takes a turn we mark the button as
     * having been clicked and let the computer take a turn.
     */
    fun onClickHandler(view: View) {
        val clicked = view as Button
        if (clicked.isEnabled) {
            markButtonAsClicked(clicked)
            computerTurn()
        }
    }

    // Change the button that was clicked and update the board accordingly.
    private fun markButtonAsClicked(clicked: Button?) {
        clicked!!.isEnabled = false
        clicked.text = board!!.currentPlayerValue.toString()
//        clicked.background.setColorFilter(
//                COLORS[board!!.currentPlayer], PorterDuff.Mode.MULTIPLY)
        clicked.background.colorFilter = PorterDuffColorFilter(COLORS[board!!.currentPlayer], PorterDuff.Mode.MULTIPLY)
        val buttonLabel = resources.getResourceEntryName(clicked.id)
        board!!.setValue(buttonLabel.substring(6).toInt())
        if (board!!.gameOver()) {
            handleEndOfGame()
        }
    }

    // When the game is over, declare a winner.
    private fun handleEndOfGame() {
        disableAllButtons()
        val score = board!!.score
        var message: String? = null
        if (score > 0) {
            message = "You win by $score"
        } else if (score < 0) {
            message = "You lose by " + -score
        }
        if (message != null) {
            val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
            toast.show()
            Log.i("BlackHole", message)
        }
    }

    // When the game is over disable all buttons (really just the one button that is left).
    private fun disableAllButtons() {
        for (i in 0 until BlackHoleBoard.BOARD_SIZE) {
            val id = resources.getIdentifier("button$i", "id", packageName)
            val b = findViewById<View>(id) as Button
            b.isEnabled = false
        }
    }

    // Let the computer take a turn.
    private fun computerTurn() {
        val position = board!!.pickMove()
        val id = resources.getIdentifier("button$position", "id", packageName)
        val b = findViewById<View>(id) as Button
        if (b == null) {
            Log.i("Blackhole", "Couldn't find button $position with id $id")
        }
        markButtonAsClicked(b)
    }

    // Handler for the reset button. Resets both the board and the game buttons.
    fun onReset(view: View?) {
        board!!.reset()
        for (i in 0 until BlackHoleBoard.BOARD_SIZE) {
            val id = resources.getIdentifier("button$i", "id", packageName)
            val b = findViewById<View>(id) as Button
            b.isEnabled = true
            b.text = "?"
            b.background.colorFilter = null
        }
    }

    companion object {
        // Colors used to differentiate human player from computer player.
        private val COLORS = intArrayOf(Color.rgb(255, 128, 128), Color.rgb(128, 128, 255))
    }
}