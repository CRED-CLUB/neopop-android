package club.cred.neopop.common

import club.cred.neopop.tiltButton.NeoPopAnimationState

interface NeoButtonDrawableInteractor {
    fun onAnimate(dx: Float, dy: Float)
    fun onStrokeWidthAnimate(strokeWidthValue: Float)
    fun onShimmerAnimation(animatedValue: Float, animatedPercentage: Float) {}
}

interface PopButtonAnimationListener {
    /** isReverse is true when button is coming Up after releasing
     the button and false when is bring pressed **/
    fun onAnimate(animationState: NeoPopAnimationState, progress: Float)
}
