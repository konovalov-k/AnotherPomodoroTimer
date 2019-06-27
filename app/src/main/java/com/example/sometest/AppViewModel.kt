package com.example.sometest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.os.CountDownTimer

private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 1000)
private val NO_BUZZ_PATTERN = longArrayOf(0)

class AppViewModel: ViewModel() {

    enum class BuzzType(val pattern: LongArray) {
        CORRECT(CORRECT_BUZZ_PATTERN),
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }

    companion object{
        //Total time of the session
        private const val COUNTDOWN_TIME = 25000L//60000L*25

        //Milliseconds in one second
        private const val ONE_SECOND = 1000L

        // This is when the session is over
        private const val DONE = 0L
    }
    //Our timer
    private val timer: CountDownTimer

    //LiveData and encapsulation
    //Time
    private val _currentTime=MutableLiveData<Long>()
    val currentTime:LiveData<Long>
            get()=_currentTime

    //Event which triggers end of the countdown
    private val _eventCountDownFinish = MutableLiveData<Boolean>()
    val eventCountDownFinish: LiveData<Boolean>
        get() = _eventCountDownFinish

    private val _eventBuzz = MutableLiveData<BuzzType>()
    val eventBuzz: LiveData<BuzzType>
        get() = _eventBuzz

    fun onCountDownFinish(){
        _eventCountDownFinish.value=false
    }
    init {
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value=(millisUntilFinished/ ONE_SECOND)
            }

            override fun onFinish() {
                _currentTime.value = DONE
                _eventBuzz.value = BuzzType.GAME_OVER
                _eventCountDownFinish.value=true
            }
        }
        timer.start()
    }

    fun onBuzzComplete() {
        _eventBuzz.value = BuzzType.NO_BUZZ
    }
}