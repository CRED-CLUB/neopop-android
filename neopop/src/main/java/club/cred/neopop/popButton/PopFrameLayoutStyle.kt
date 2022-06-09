package club.cred.neopop.popButton

import android.graphics.Color
import androidx.core.graphics.ColorUtils
import club.cred.neopop.common.SHIMMER_ANIMATION_DURATION
import club.cred.neopop.common.dp
import kotlin.math.roundToInt

internal data class PopFrameLayoutStyle(
    val centerSurfaceColor: Int = 0,
    val topSurfaceColor: Int = Color.TRANSPARENT,
    val bottomSurfaceColor: Int = Color.TRANSPARENT,
    val rightSurfaceColor: Int = Color.TRANSPARENT,
    val leftSurfaceColor: Int = Color.TRANSPARENT,
    val isLeftSurfaceVisible: Boolean = false,
    val isTopSurfaceVisible: Boolean = false,
    val isRightSurfaceVisible: Boolean = true,
    val isBottomSurfaceVisible: Boolean = true,
    val shadowWidth: Float = 8f.dp,
    val buttonPosition: Int = -1,
    val isBottomSheetShadow: Boolean = false,
    val surfaceStrokeColors: SurfaceStrokeColorData? = null,
    val isStrokedButton: Boolean = true,
    val buttonOnLeft: Int = Int.MIN_VALUE,
    val buttonOnRight: Int = Int.MIN_VALUE,
    val buttonOnTop: Int = Int.MIN_VALUE,
    val buttonOnBottom: Int = Int.MIN_VALUE,
    val hasShimmer: Boolean = false,
    val shimmerWidth: Float = 0f.dp,
    val shimmerColor: Int = Color.TRANSPARENT,
    val shimmerDuration: Long = SHIMMER_ANIMATION_DURATION,
    val strokeWidth: Int = NeoPopGeometry.DEFAULT_STROKE_WIDTH.roundToInt(),
    val drawFullWidth: Boolean = false,
    val drawFullHeight: Boolean = false,
    val disabledOpacity: Int = PopFrameLayoutStyle.disabledOpacity,
    val disabledCardColor: Int = PopFrameLayoutStyle.disabledCardColor,
    val disabledBottomShadowColor: Int = PopFrameLayoutStyle.disabledBottomShadowColor,
    val disabledRightShadowColor: Int = PopFrameLayoutStyle.disabledRightShadowColor,
    val shimmerStartDelay: Long = 0L,
    val shimmerRepeatDelay: Long = 0L,
    val parentViewColor: Int = Int.MIN_VALUE,
    val grandParentViewColor: Int = Int.MIN_VALUE,
) {

    companion object {
        const val top = 0x001
        const val right = 0x002
        const val bottom = 0x004
        const val left = 0x008
        const val center = 0x010
        const val top_right = top.or(right)
        const val bottom_right = bottom.or(right)
        const val bottom_left = bottom.or(left)
        const val top_left = top.or(left)
        const val disabledOpacity = 0x4D
        const val disabledCardColor = 0xff8A8A8A.toInt()
        const val disabledRightShadowColor = 0xffB6B6B6.toInt()
        const val disabledBottomShadowColor = 0xff3D3D3D.toInt()
        private const val BOTTOM_SHIMMER_ALPHA = 0x1A

        const val HUE = 0
        const val SATURATION = 1
        const val LIGHTNESS = 2

        // shadow rgb to hsl  ->  if l>=30 rightShadow = l-10 bottom=l-20
        // else rightShadow = l+20 bottomSHdaow = l+10
        fun getHorizontalShadowColor(cardColor: Int): Int {
            val hsl = FloatArray(3)
            ColorUtils.colorToHSL(cardColor, hsl)
            return if (hsl[LIGHTNESS] >= 0.3f) {
                val rightShadowColorHSL = hsl.copyOf().apply {
                    this[LIGHTNESS] = hsl[LIGHTNESS] - 0.1f
                }
                ColorUtils.HSLToColor(rightShadowColorHSL)
            } else {
                val rightShadowColorHSL = hsl.copyOf().apply {
                    this[LIGHTNESS] = hsl[LIGHTNESS] + 0.2f
                }
                ColorUtils.HSLToColor(rightShadowColorHSL)
            }
        }

        fun getVerticalShadowColor(cardColor: Int): Int {
            val hsl = FloatArray(3)
            ColorUtils.colorToHSL(cardColor, hsl)
            return if (hsl[LIGHTNESS] >= 0.3f) {
                val bottomShadowColorHSL = hsl.copyOf().apply {
                    this[LIGHTNESS] = hsl[LIGHTNESS] - 0.2f
                }
                ColorUtils.HSLToColor(bottomShadowColorHSL)
            } else {
                val bottomShadowColorHSL = hsl.copyOf().apply {
                    this[LIGHTNESS] = hsl[LIGHTNESS] + 0.1f
                }
                ColorUtils.HSLToColor(bottomShadowColorHSL)
            }
        }

        fun getBottomShimmer(topSimmerColor: Int): Int =
            ColorUtils.setAlphaComponent(topSimmerColor, BOTTOM_SHIMMER_ALPHA)
    }
}

data class SurfaceStrokeColorData(
    val topSurfaceStrokeColors: SurfaceStrokeColors? = null,
    val rightSurfaceStrokeColors: SurfaceStrokeColors? = null,
    val bottomSurfaceStrokeColors: SurfaceStrokeColors? = null,
    val leftSurfaceStrokeColors: SurfaceStrokeColors? = null,
    val centerSurfaceStrokeColors: SurfaceStrokeColors? = null
)

data class SurfaceStrokeColors(
    val bottomColor: Int? = null,
    val topColor: Int? = null,
    val leftColor: Int? = null,
    val rightColor: Int? = null,
)

data class Colors(
    val bottomColor: Int? = null,
    val topColor: Int? = null,
    val leftColor: Int? = null,
    val rightColor: Int? = null,
)
