package com.example.myclock.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.myclock.R
import timber.log.Timber
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class MyClock(context: Context, attrs: AttributeSet, ): View(context, attrs)
{
    private val arr = context.obtainStyledAttributes(attrs, R.styleable.MyClock)
    private val strokeWidth = arr.getDimension(R.styleable.MyClock_strokeWidth, 2f)

    private val n = 0.1f

    private var cx = 0f
    private var cy = 0f
    private var radius = 0f

    private var paint: Paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = this@MyClock.strokeWidth.toFloat()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Timber.i("""width: $widthMeasureSpec
            |height: $heightMeasureSpec
        """.trimMargin())
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Timber.i(
            """left: $left
                |top: $top
                |right: $right
                |bottom: $bottom
            """.trimMargin()
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            cx = (left + right).toFloat() / 2
            cy = (top + bottom).toFloat() / 2
            radius = minOf(bottom - top, right - left).toFloat() / 2 - strokeWidth
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
                Timber.i("sin(a) = ${sin(a)}")
                a += (PI/6).toFloat()
                Timber.i("a: $a")
            }
        }
    }
}