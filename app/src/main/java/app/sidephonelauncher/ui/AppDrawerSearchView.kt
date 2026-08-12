package app.sidephonelauncher.ui

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import androidx.appcompat.widget.SearchView

class AppDrawerSearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.searchViewStyle,
) : SearchView(context, attrs, defStyleAttr) {

    var onBackPressed: (() -> Boolean)? = null

    override fun dispatchKeyEventPreIme(event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_BACK) {
            val handled = when (event.action) {
                KeyEvent.ACTION_DOWN -> true
                KeyEvent.ACTION_UP -> onBackPressed?.invoke() == true
                else -> false
            }
            if (handled) return true
        }
        return super.dispatchKeyEventPreIme(event)
    }
}
