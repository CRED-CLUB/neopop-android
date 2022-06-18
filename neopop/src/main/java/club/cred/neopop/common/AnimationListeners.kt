package club.cred.neopop.common

import club.cred.neopop.tiltButton.NeoPopAnimationState

interface NeoButtonDrawableInteractor {
    fun onAnimate(dx: Float, dy: Float)
    fun onStrokeWidthAnimate(strokeWidthValue: Float)
    fun onShimmerAnimation(animatedValue: Float, animatedPercentage: Float) {}
}
