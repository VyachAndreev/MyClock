package com.example.myclock.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import com.example.myclock.R
import timber.log.Timber
import java.util.*
import kotlin.concurrent.thread
import kotlin.coroutines.CoroutineContext
import kotlin.math.*

class MyClock(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {
    private val arr = context.obtainStyledAttributes(attrs, R.styleable.MyClock)
    private val strokeWidth = arr.getDimension(R.styleable.MyClock_strokeWidth, 2f)

    private val n = 0.1f
    private var updating = false

    private var cx = 0f
    private var cy = 0f
    private var radius = 0f
    private var hourHandScaler = 0.5f
    private var minuteHandScaler = 0.7f
    private var secondHandScaler = 0.85f

    private lateinit var calendar: Calendar
    private var mCanvas: Canvas? = null

    private val paint: Paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = this@MyClock.strokeWidth
    }

    private val handPaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        Timber.i("OnLayout")
        cx = (left + right).toFloat() / 2
        cy = (top + bottom).toFloat() / 2
        radius = minOf(bottom - top, right - left).toFloat() / 2 - strokeWidth
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mCanvas = canvas
        canvas?.let {
            Timber.i(
                """cx: $cx
                    |cy: $cy
                    |radius: $radius
                """.trimMargin()
            )
            it.drawCircle(cx, cy, radius, paint)
            var a = 0f
            for (i in 0..11) {
                it.drawLine(
                    cx + cos(a) * radius,
                    cy + sin(a) * radius,
                    cx + cos(a) * radius * (1 - n),
                    cy + sin(a) * radius * (1 - n),
                    paint,
                )
                a += (PI / 6).toFloat()
            }
            it.drawCircle(cx, cy, 10f, paint.apply { style = Paint.Style.FILL })
            updateTime()
        }
    }

    fun setTime(calendar: Calendar) {
        this.calendar = calendar
        var seconds = calendar.get(Calendar.SECOND) + calendar.get(Calendar.MINUTE) * 60
        var angle = seconds.toDouble() / 3_600 * 2 * PI
        mCanvas?.drawLine(
            cx,
            cy,
            cx + radius * minuteHandScaler * sin(angle).toFloat(),
            cy - radius * minuteHandScaler * cos(angle).toFloat(),
            handPaint.apply {
                strokeWidth = 10f
                color = Color.BLACK
            },
        )
        seconds += calendar.get(Calendar.HOUR) * 3_600
        angle = seconds.toDouble() / 43_200 * 2 * PI
        mCanvas?.drawLine(
            cx,
            cy,
            cx + radius * hourHandScaler * sin(angle).toFloat(),
            cy - radius * hourHandScaler * cos(angle).toFloat(),
            handPaint.apply { strokeWidth = 20f },
        )
        seconds = calendar.get(Calendar.SECOND)
        angle = seconds.toDouble() / 60 * 2 * PI
        mCanvas?.drawLine(
            cx - 20f * sin(angle).toFloat(),
            cy + 20f * cos(angle).toFloat(),
            cx + radius * secondHandScaler * sin(angle).toFloat(),
            cy - radius * secondHandScaler * cos(angle).toFloat(),
            handPaint.apply {
                strokeWidth = 5f
                color = Color.RED
            },
        )
    }

    fun getTime(): Calendar = calendar

    fun updateTime() {
        setTime(Calendar.getInstance())
    }
}

class Hand(
    width: Int,
    length: Int,
    @ColorInt handColor: Int,
    context: Context,
) : View(context) {
    constructor(context: Context) : this(1, 1, Color.BLACK, context)

    private val paint = Paint().apply {
        color = handColor
        style = Paint.Style.FILL
    }

    init {
        Timber.i("length: $length")
        layoutParams = RelativeLayout.LayoutParams(width, length)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawRect(
            left.toFloat(),
            top.toFloat(),
            right.toFloat(),
            (bottom + top).toFloat() / 2,
            paint,
        )
    }
}