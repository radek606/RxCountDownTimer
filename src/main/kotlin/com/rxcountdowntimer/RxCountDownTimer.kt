package com.rxcountdowntimer

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Scheduler
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.util.concurrent.TimeUnit

abstract class RxCountDownTimer(
    private val count: Long,
    private val period: Long,
    private val unit: TimeUnit,
    private val observeOn: Scheduler,
) {

    private var subscription: Subscription? = null
    private var cancelled: Boolean = false

    abstract fun onTick(tickValue: Long)
    abstract fun onFinish()

    @Synchronized
    fun start(): RxCountDownTimer {
        cancelled = false
        if (count <= 0) {
            onFinish()
            return this
        }

        //start = 1 and count + 1 shifts counting so first tick = count - 1 and last tick before onComplete() = 0
        Flowable.intervalRange(1, count + 1, 0, period, unit)
            .map { count - it }
            .onBackpressureDrop()
            //buffer size set to 1 along with onBackpressureDrop() allows skipping subsequent values
            //if processing of single one takes longer than emit period.
            .observeOn(observeOn, false, 1)
            .subscribe(object : Subscriber<Long> {
                override fun onSubscribe(s: Subscription) {
                    subscription = s
                    s.request(1)
                }

                override fun onNext(t: Long) {
                    //condition 't >= 0' allows for whole 'period' between last onTick() and onFinish()
                    if (!cancelled && t >= 0) {
                        onTick(t)
                        subscription?.request(1)
                    }
                }

                override fun onError(t: Throwable) {
                    throw t
                }

                override fun onComplete() {
                    onFinish()
                }
            })

        return this
    }

    @Synchronized
    fun cancel() {
        cancelled = true
        subscription?.cancel()
    }

}
