package app.sidephonelauncher.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.TextView
import app.sidephonelauncher.R
import app.sidephonelauncher.data.Constants
import app.sidephonelauncher.data.Prefs
import app.sidephonelauncher.helper.dpToPx
import app.sidephonelauncher.helper.getColorFromAttr

class HomeAppTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle,
) : TextView(context, attrs, defStyleAttr) {

    private val prefs = Prefs(context)
    private val inactiveTextColor = context.getColorFromAttr(R.attr.primaryColor)
    private val inactiveHintColor = context.getColorFromAttr(R.attr.primaryColorTrans80)
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
        setBackgroundResource(
            if (isPillStyle()) R.drawable.bg_focus_inverted_selector else android.R.color.transparent
        )
        applyFocusAppearance()
    }

    fun setShowNotificationDot(show: Boolean) {
        if (showNotificationDot == show) return
        showNotificationDot = show
        invalidate()
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        applyFocusAppearance(hasFocus() || isPressed)
        if (showNotificationDot) invalidate()
    }

    private fun isPillStyle(): Boolean {
        return prefs.focusIndicatorStyle == Constants.FocusIndicator.PILL
    }

    private fun applyFocusAppearance(active: Boolean = hasFocus() || isPressed) {
        if (isPillStyle()) {
            super.setTextColor(if (active) context.getColor(R.color.black) else inactiveTextColor)
            setHintTextColor(if (active) context.getColor(R.color.black) else inactiveHintColor)
            paintFlags = paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
        } else {
            super.setTextColor(inactiveTextColor)
            setHintTextColor(inactiveHintColor)
            paintFlags = if (active) {
                paintFlags or Paint.UNDERLINE_TEXT_FLAG
            } else {
                paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
            }
        }
        setShadowLayer(0f, 0f, 0f, Color.TRANSPARENT)
    }

    override fun onDraw(canvas: Canvas) {
        val active = hasFocus() || isPressed
        applyFocusAppearance(active)
        super.onDraw(canvas)
        if (!showNotificationDot) return

        dotPaint.color = if (isPillStyle() && active) Color.BLACK else Color.WHITE
        val cx = width - reservedSideSpacePx / 2f
        val cy = height / 2f
        canvas.drawCircle(cx, cy, dotRadiusPx, dotPaint)
    }
}
