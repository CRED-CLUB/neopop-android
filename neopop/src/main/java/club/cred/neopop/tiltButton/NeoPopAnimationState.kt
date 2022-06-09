package club.cred.neopop.tiltButton

class NeoPopAnimationState(
    var isCompleted: Boolean = false,
    var isFloating: Boolean = true,
    var isButtonGoingUp: Boolean = false
) {
    /**
     * 0f -> button is on top
     * 1f -> button is on bottom
     */
    var currentProgress: Float = 0f

    val isButtonAtBottom: Boolean
        get() = currentProgress == 1f

    val isButtonAtTop: Boolean
        get() = currentProgress == 0f

    val isButtonGoingDown: Boolean
        get() = !isButtonGoingUp

    fun setNextType(onSpace: Boolean) {
        if (onSpace) {
            when (isButtonGoingUp) {
                true -> {
                    isButtonGoingUp = false
                    isFloating = true
                }
                false -> {
                    isButtonGoingUp = true
                }
            }
        }
    }

    fun setButtonDown() {
        isButtonGoingUp = false
        isFloating = false
    }

    fun setButtonUp() {
        isButtonGoingUp = true
        isFloating = false
    }
}
