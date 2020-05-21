

package id.aflah.timefighter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var score = 0

    private var isGameStarted = false

    private lateinit var countDownTimer: CountDownTimer

    private val initCountDownTime = 10_000L
    private val countDownInterval = 1_000L
    private var timeLeft = 10_000L

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private val SCORE_KEY = "SCORE"
        private val TIME_LEFT_KEY = "TIME_LEFT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate calle. Score is: $score ")

        btnTapMe.setOnClickListener {
            incrementScore()
        }

        tvGameScore.text = getString(R.string.yourScore, score)

        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeft = savedInstanceState.getLong(TIME_LEFT_KEY)
            restoreGame()
        } else {
            resetGame()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy called")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(SCORE_KEY, score)
        outState.putLong(TIME_LEFT_KEY, timeLeft)
        countDownTimer.cancel()

        Log.d(TAG, "onSaveInstanceState: Saving score $score & time left $timeLeft")
    }

    private fun incrementScore () {
        if (!isGameStarted) {
            countDownTimer.start()
            isGameStarted = true
        }

        score++

        val newScore = getString(R.string.yourScore, score)
        tvGameScore.text = newScore
    }

    private fun endedGame () {
        Toast.makeText(this, getString(R.string.messageTimeUp, score), Toast.LENGTH_SHORT).show()
        resetGame()
    }

    private fun resetGame() {
        score = 0
        tvGameScore.text = getString(R.string.yourScore, score)

        val initTimeLeft = initCountDownTime / 1000
        tvTimeLeft.text = getString(R.string.timeLeft, initTimeLeft)

        countDownTimer = object : CountDownTimer(initCountDownTime, countDownInterval) {
            override fun onFinish() {
                endedGame()
            }

            override fun onTick(p0: Long) {
                timeLeft = p0
                val timeleft = p0 / 1000
                tvTimeLeft.text = getString(R.string.timeLeft, timeleft)
            }
        }

        isGameStarted = false
    }

    private fun restoreGame () {
        tvGameScore.text = getString(R.string.yourScore, score)

        val restoredTime = timeLeft / 1000
        tvTimeLeft.text = getString(R.string.timeLeft, restoredTime)

        countDownTimer = object : CountDownTimer(initCountDownTime, countDownInterval) {
            override fun onFinish() {
                endedGame()
            }

            override fun onTick(p0: Long) {
                timeLeft = p0
                val newTimeLeft = p0 / 1000
                tvTimeLeft.text = getString(R.string.timeLeft, newTimeLeft)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.actionbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == R.id.menu_info) {
            showInfoMessage(getString(R.string.infoTitle), getString(R.string.infoMessage))
        }
        return true
    }

    private fun showInfoMessage (title:String, message:String) {
        val builder = AlertDialog.Builder(this)

        builder.setTitle(title)
        builder.setMessage(message)
        builder.create().show()
    }
}
