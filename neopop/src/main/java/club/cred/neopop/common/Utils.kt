package club.cred.neopop.common

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.provider.Settings
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import kotlin.math.tan
import kotlin.properties.ObservableProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

const val SHIMMER_ANIMATION_DURATION = 1500L
private const val EXIT_FADE_DURATION = 400

internal inline fun <T> View.dynamicAttr(
    initialValue: T,
    crossinline onChange: (T) -> Unit = {}
): ReadWriteProperty<Any?, T> {
    return object : ObservableProperty<T>(initialValue) {

        var initDone = false

        override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) {
            if (!initDone) {
                initDone = true
                return
            }
            onChange(newValue)
            invalidate()
            requestLayout()
        }
    }
}

internal inline fun <T> onFieldChange(
    initialValue: T,
    crossinline onChange: (T) -> Unit = {}
): ReadWriteProperty<Any?, T> {
    return object : ObservableProperty<T>(initialValue) {
        override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) {
            if (oldValue != newValue) {
                onChange(newValue)
            }
        }
    }
}

internal fun createCheckableSelector(
    selectedDrawable: Drawable,
    unselectedDrawable: Drawable
): StateListDrawable {
    val res = StateListDrawable()
    res.setExitFadeDuration(EXIT_FADE_DURATION)
    res.addState(intArrayOf(android.R.attr.state_checked), selectedDrawable)
    res.addState(intArrayOf(-android.R.attr.state_checked), unselectedDrawable)
    return res
}

internal fun getFillPaint(paintColor: Int) =
    Paint().apply {
        style = Paint.Style.FILL
        this.color = paintColor
        isDither = true
    }

internal fun getMovePath(angle: Double, length: Int): Float {
    val theta = tan(Math.toRadians(angle))
    return (length * theta).toFloat()
}

internal val Float.dp: Float
    get() {
        val displayMetrics = Resources.getSystem().displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, displayMetrics)
    }

internal val Int.dp: Float
    get() {
        val displayMetrics = Resources.getSystem().displayMetrics
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            displayMetrics
        )
    }

fun Context.areSystemAnimationsEnabled(): Boolean =
    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
        getSystemAnimatorScale() != 0f
    } else {
        ValueAnimator.areAnimatorsEnabled()
    }

fun Context.getSystemAnimatorScale(): Float =
    Settings.Global.getFloat(contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f)

fun Animator.resumeOrStart() {
    if (isPaused) resume() else start()
}
