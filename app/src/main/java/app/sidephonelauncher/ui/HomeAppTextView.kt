package app.sidephonelauncher.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.TextView
import app.sidephonelauncher.helper.dpToPx

class HomeAppTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle,
) : TextView(context, attrs, defStyleAttr) {

    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        alpha = 220
        style = Paint.Style.FILL
    }

    private val dotRadiusPx = 3.dpToPx().toFloat()
    private val reservedSideSpacePx = 14.dpToPx()

    private var showNotificationDot = false

    init {
        setPaddingRelative(
            paddingStart + reservedSideSpacePx,
            paddingTop,
            paddingEnd + reservedSideSpacePx,
            paddingBottom,
        )
    }

    fun setShowNotificationDot(show: Boolean) {
        if (showNotificationDot == show) return
        showNotificationDot = show
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!showNotificationDot) return

        val cx = width - reservedSideSpacePx / 2f
        val cy = height / 2f
        canvas.drawCircle(cx, cy, dotRadiusPx, dotPaint)
    }
}
