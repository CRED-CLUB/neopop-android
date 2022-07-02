/*
 *
 *  * Copyright 2022 Dreamplug Technologies Private Limited
 *  * Licensed under the Apache License, Version 2.0 (the “License”);
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and limitations under the License.
 *
 */

package club.cred.neopop.popButton

import android.graphics.Color
import androidx.core.graphics.ColorUtils

object NeoPopHelper {

    internal fun PopFrameLayoutStyle.calculateButtonStyles() =
        getCalculatedButtonStyles(
            centerSurfaceColor,
            buttonPosition,
            buttonOnRight,
            buttonOnLeft,
            buttonOnTop,
            buttonOnBottom,
            depth,
            isStrokedButton,
            parentViewColor,
            grandParentViewColor,
            this
        )

    internal fun getCalculatedButtonStyles(
        mainCardColor: Int,
        buttonPosition: Int,
        buttonOnRight: Int,
        buttonOnLeft: Int,
        buttonOnTop: Int,
        buttonOnBottom: Int,
        depth: Float,
        isStrokedButton: Boolean,
        parentViewColor: Int,
        grandParentViewColor: Int,
        style: PopFrameLayoutStyle = PopFrameLayoutStyle(),
    ): PopFrameLayoutStyle {

        var calculatedTopShadowVisibility = true
        var calculatedRightShadowVisibility = true
        var calculatedBottomShadowVisibility = true
        var calculatedLeftShadowVisibility = true
        var calculatedTopShadowColor = Color.TRANSPARENT
        var calculatedLeftShadowColor = Color.TRANSPARENT
        val horizontalShadowColor = PopFrameLayoutStyle.getHorizontalSurfaceColor(mainCardColor)
        val verticalShadowColor = PopFrameLayoutStyle.getVerticalSurfaceColor(mainCardColor)
        var calculatedRightShadowColor = horizontalShadowColor
        var calculatedBottomShadowColor = verticalShadowColor
        when (buttonPosition) {
            PopFrameLayoutStyle.top -> {
                calculatedTopShadowVisibility = true
                calculatedRightShadowVisibility = false
                calculatedBottomShadowVisibility = false
                calculatedLeftShadowVisibility = true
                calculatedLeftShadowColor = getOrDefault(
                    parentViewColor != Int.MIN_VALUE,
                    PopFrameLayoutStyle.getHorizontalSurfaceColor(parentViewColor),
                    horizontalShadowColor
                )
                calculatedTopShadowColor = getOrDefault(
                    grandParentViewColor != Int.MIN_VALUE,
                    grandParentViewColor,
                    verticalShadowColor
                )

                if (buttonOnRight != Int.MIN_VALUE) {
                    calculatedRightShadowVisibility = true
                    calculatedRightShadowColor = horizontalShadowColor
                }

                if (buttonOnLeft != Int.MIN_VALUE) {
                    calculatedLeftShadowColor = Color.TRANSPARENT
                }
                if (buttonOnBottom != Int.MIN_VALUE) {
                    calculatedBottomShadowVisibility = true
                    calculatedBottomShadowColor = verticalShadowColor
                }
            }
            PopFrameLayoutStyle.top_right -> {
                calculatedRightShadowVisibility = true
                calculatedTopShadowVisibility = true
                calculatedLeftShadowVisibility = true
                calculatedBottomShadowVisibility = false
                calculatedRightShadowColor = horizontalShadowColor
                calculatedTopShadowColor = if (grandParentViewColor != Int.MIN_VALUE) {
                    grandParentViewColor
                } else {
                    Color.TRANSPARENT
                }
                calculatedLeftShadowColor = if (parentViewColor != Int.MIN_VALUE) {
                    PopFrameLayoutStyle.getHorizontalSurfaceColor(parentViewColor)
                } else {
                    horizontalShadowColor
                }
                if (buttonOnLeft != Int.MIN_VALUE) {
                    calculatedLeftShadowColor = Color.TRANSPARENT
                }
                if (buttonOnBottom != Int.MIN_VALUE) {
                    calculatedBottomShadowVisibility = true
                    calculatedBottomShadowColor = verticalShadowColor
                }
            }
            PopFrameLayoutStyle.right -> {
                calculatedRightShadowVisibility = true
                calculatedTopShadowVisibility = true
                calculatedLeftShadowVisibility = true
                calculatedBottomShadowVisibility = false
                calculatedRightShadowColor = horizontalShadowColor

                calculatedTopShadowColor = if (parentViewColor != Int.MIN_VALUE) {
                    PopFrameLayoutStyle.getVerticalSurfaceColor(parentViewColor)
                } else {
                    verticalShadowColor
                }
                calculatedLeftShadowColor = if (parentViewColor != Int.MIN_VALUE) {
                    PopFrameLayoutStyle.getHorizontalSurfaceColor(parentViewColor)
                } else {
                    horizontalShadowColor
                }

                if (buttonOnBottom != Int.MIN_VALUE) {
                    calculatedBottomShadowVisibility = true
                    calculatedBottomShadowColor = verticalShadowColor
                }
                if (buttonOnTop != Int.MIN_VALUE) {
                    calculatedTopShadowColor = Color.TRANSPARENT
                }
                if (buttonOnLeft != Int.MIN_VALUE) {
                    calculatedLeftShadowColor = Color.TRANSPARENT
                }
            }
            PopFrameLayoutStyle.bottom_right -> {
                calculatedRightShadowVisibility = true
                calculatedTopShadowVisibility = true
                calculatedLeftShadowVisibility = true
                calculatedBottomShadowVisibility = true
                calculatedRightShadowColor = horizontalShadowColor
                calculatedBottomShadowColor = verticalShadowColor

                calculatedTopShadowColor = if (parentViewColor != Int.MIN_VALUE) {
                    PopFrameLayoutStyle.getVerticalSurfaceColor(parentViewColor)
                } else {
                    verticalShadowColor
                }
                calculatedLeftShadowColor = if (parentViewColor != Int.MIN_VALUE) {
                    PopFrameLayoutStyle.getHorizontalSurfaceColor(parentViewColor)
                } else {
                    horizontalShadowColor
                }

                if (buttonOnLeft != Int.MIN_VALUE) {
                    calculatedLeftShadowColor = Color.TRANSPARENT
                }
                if (buttonOnTop != Int.MIN_VALUE) {
                    calculatedTopShadowColor = Color.TRANSPARENT
                }
            }
            PopFrameLayoutStyle.bottom -> {
                calculatedRightShadowVisibility = false
                calculatedTopShadowVisibility = true
                calculatedLeftShadowVisibility = true
                calculatedBottomShadowVisibility = true
                calculatedBottomShadowColor = verticalShadowColor

                calculatedTopShadowColor = if (parentViewColor != Int.MIN_VALUE) {
                    PopFrameLayoutStyle.getVerticalSurfaceColor(parentViewColor)
                } else {
                    verticalShadowColor
                }
                calculatedLeftShadowColor = if (parentViewColor != Int.MIN_VALUE) {
                    PopFrameLayoutStyle.getHorizontalSurfaceColor(parentViewColor)
                } else {
                    horizontalShadowColor
                }

                if (buttonOnRight != Int.MIN_VALUE) {
                    calculatedRightShadowVisibility = true
                    calculatedRightShadowColor = horizontalShadowColor
                }
                if (buttonOnTop != Int.MIN_VALUE) {
                    calculatedTopShadowColor = Color.TRANSPARENT
                }
                if (buttonOnLeft != Int.MIN_VALUE) {
                    calculatedLeftShadowColor = Color.TRANSPARENT
                }
            }
            PopFrameLayoutStyle.bottom_left -> {
                calculatedRightShadowVisibility = false
                calculatedTopShadowVisibility = true
                calculatedLeftShadowVisibility = true
                calculatedBottomShadowVisibility = true
                calculatedBottomShadowColor = verticalShadowColor

                calculatedTopShadowColor = if (parentViewColor != Int.MIN_VALUE) {
                    PopFrameLayoutStyle.getVerticalSurfaceColor(parentViewColor)
                } else {
                    verticalShadowColor
                }
                calculatedLeftShadowColor = if (grandParentViewColor != Int.MIN_VALUE) {
                    grandParentViewColor
                } else {
                    horizontalShadowColor
                }

                if (buttonOnRight != Int.MIN_VALUE) {
                    calculatedRightShadowVisibility = true
                    calculatedRightShadowColor = horizontalShadowColor
                }
                if (buttonOnTop != Int.MIN_VALUE) {
                    calculatedTopShadowColor = Color.TRANSPARENT
                }
            }
            PopFrameLayoutStyle.left -> {
                calculatedRightShadowVisibility = false
                calculatedTopShadowVisibility = true
                calculatedLeftShadowVisibility = true
                calculatedBottomShadowVisibility = false

                calculatedTopShadowColor = if (parentViewColor != Int.MIN_VALUE) {
                    PopFrameLayoutStyle.getVerticalSurfaceColor(parentViewColor)
                } else {
                    verticalShadowColor
                }
                calculatedLeftShadowColor = if (grandParentViewColor != Int.MIN_VALUE) {
                    grandParentViewColor
                } else {
                    horizontalShadowColor
                }

                if (buttonOnTop != Int.MIN_VALUE) {
                    calculatedTopShadowColor = Color.TRANSPARENT
                }
                if (buttonOnBottom != Int.MIN_VALUE) {
                    calculatedBottomShadowVisibility = true
                    calculatedBottomShadowColor = verticalShadowColor
                }
                if (buttonOnRight != Int.MIN_VALUE) {
                    calculatedRightShadowVisibility = true
                    calculatedRightShadowColor = horizontalShadowColor
                }
            }
            PopFrameLayoutStyle.top_left -> {
                calculatedRightShadowVisibility = false
                calculatedTopShadowVisibility = true
                calculatedLeftShadowVisibility = true
                calculatedBottomShadowVisibility = false
                calculatedTopShadowColor = if (grandParentViewColor != Int.MIN_VALUE) {
                    grandParentViewColor
                } else {
                    verticalShadowColor
                }
                calculatedLeftShadowColor = if (grandParentViewColor != Int.MIN_VALUE) {
                    grandParentViewColor
                } else {
                    horizontalShadowColor
                }
                if (buttonOnBottom != Int.MIN_VALUE) {
                    calculatedBottomShadowVisibility = true
                    calculatedBottomShadowColor = verticalShadowColor
                }
                if (buttonOnRight != Int.MIN_VALUE) {
                    calculatedRightShadowVisibility = true
                    calculatedRightShadowColor = horizontalShadowColor
                }
            }
            PopFrameLayoutStyle.center -> {
                calculatedRightShadowVisibility = false
                calculatedTopShadowVisibility = true
                calculatedLeftShadowVisibility = true
                calculatedBottomShadowVisibility = false

                calculatedTopShadowColor = if (parentViewColor != Int.MIN_VALUE) {
                    PopFrameLayoutStyle.getVerticalSurfaceColor(parentViewColor)
                } else {
                    verticalShadowColor
                }
                calculatedLeftShadowColor = if (parentViewColor != Int.MIN_VALUE) {
                    PopFrameLayoutStyle.getHorizontalSurfaceColor(parentViewColor)
                } else {
                    horizontalShadowColor
                }
                if (buttonOnRight != Int.MIN_VALUE) {
                    calculatedRightShadowVisibility = true
                    calculatedRightShadowColor = horizontalShadowColor
                }
                if (buttonOnBottom != Int.MIN_VALUE) {
                    calculatedBottomShadowVisibility = true
                    calculatedBottomShadowColor = verticalShadowColor
                }
                if (buttonOnTop != Int.MIN_VALUE) {
                    calculatedTopShadowColor = Color.TRANSPARENT
                }
                if (buttonOnLeft != Int.MIN_VALUE) {
                    calculatedLeftShadowColor = Color.TRANSPARENT
                }
            }
        }
        return style.copy(
            centerSurfaceColor = mainCardColor,
            topSurfaceColor = calculatedTopShadowColor,
            bottomSurfaceColor = calculatedBottomShadowColor,
            rightSurfaceColor = calculatedRightShadowColor,
            leftSurfaceColor = calculatedLeftShadowColor,
            isLeftSurfaceVisible = calculatedLeftShadowVisibility,
            isRightSurfaceVisible = calculatedRightShadowVisibility,
            isBottomSurfaceVisible = calculatedBottomShadowVisibility,
            isTopSurfaceVisible = calculatedTopShadowVisibility,
            buttonPosition = buttonPosition,
            depth = depth,
            isStrokedButton = isStrokedButton
        )
    }

    internal fun getDisabledStateStyle(popStyle: PopFrameLayoutStyle): PopFrameLayoutStyle {
        val hasStrokes = popStyle.surfaceStrokeColors != null
        var disabledStrokeColors: SurfaceStrokeColorData? = null
        if (hasStrokes) {
            disabledStrokeColors =
                getDisabledStrokeColor(popStyle, popStyle.surfaceStrokeColors)
        }
        return popStyle.copy(
            surfaceStrokeColors = getOrDefault(
                hasStrokes,
                disabledStrokeColors,
                popStyle.surfaceStrokeColors
            ),
            centerSurfaceColor = getOrDefault(
                hasStrokes,
                popStyle.centerSurfaceColor,
                popStyle.disabledCardColor
            ),
            rightSurfaceColor = getOrDefault(
                hasStrokes,
                popStyle.rightSurfaceColor,
                popStyle.disabledRightSurfaceColor
            ),
            bottomSurfaceColor = getOrDefault(
                hasStrokes,
                popStyle.bottomSurfaceColor,
                popStyle.disabledBottomSurfaceColor
            ),
        )
    }

    private fun getDisabledStrokeColor(
        popStyle: PopFrameLayoutStyle,
        strokeColor: SurfaceStrokeColorData?
    ): SurfaceStrokeColorData {
        return SurfaceStrokeColorData(
            centerSurfaceStrokeColors = SurfaceStrokeColors(
                topColor = strokeColor?.centerSurfaceStrokeColors?.topColor?.let {
                    ColorUtils.setAlphaComponent(
                        strokeColor.centerSurfaceStrokeColors.topColor,
                        popStyle.disabledOpacity
                    )
                },
                rightColor = strokeColor?.centerSurfaceStrokeColors?.rightColor?.let {
                    ColorUtils.setAlphaComponent(
                        strokeColor.centerSurfaceStrokeColors.rightColor,
                        popStyle.disabledOpacity
                    )
                },
                bottomColor = strokeColor?.centerSurfaceStrokeColors?.bottomColor?.let {
                    ColorUtils.setAlphaComponent(
                        strokeColor.centerSurfaceStrokeColors.bottomColor,
                        popStyle.disabledOpacity
                    )
                },
                leftColor = strokeColor?.centerSurfaceStrokeColors?.leftColor?.let {
                    ColorUtils.setAlphaComponent(
                        strokeColor.centerSurfaceStrokeColors.leftColor,
                        popStyle.disabledOpacity
                    )
                }
            )
        )
    }

    private fun <T> getOrDefault(condition: Boolean, value: T, defaultValue: T): T {
        return if (condition) value else defaultValue
    }
}
