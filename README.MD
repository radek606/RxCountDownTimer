## RxCountDownTimer

RxJava3 version of Android's [CountDownTimer](https://developer.android.com/reference/kotlin/android/os/CountDownTimer).

Supports skipping subsequent ticks if processing of current tick takes longer than count period.

### Usage

    object : RxCountDownTimer(
        count = 10, 
        period = 1, 
        unit = TimeUnit.SECONDS, 
        observeOn = Schedulers.computation()
    ) {
        override fun onTick(tickValue: Long) {
            println("RxCountDownTimer tick $tickValue")
        }
    
        override fun onFinish() {
            println("RxCountDownTimer finished.")
        }
    }.start()
