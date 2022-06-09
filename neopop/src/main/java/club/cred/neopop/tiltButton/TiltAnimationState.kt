package club.cred.neopop.tiltButton

class TiltAnimationState(
    /**
     * 0f -> button is on top
     * 1f -> button is on bottom
     */
    var currentProgress: Float = 0f,
    var currentState: State = State.Floating,
    var isButtonGoingUp: Boolean = false,
) {

    val isButtonAtBottom: Boolean
        get() = currentProgress == 1f

    val isButtonAtTop: Boolean
        get() = currentProgress == 0f

    var isButtonGoingDown: Boolean
        get() = !isButtonGoingUp
        set(value) {
            isButtonGoingUp = !value
        }

    fun setNextType(onSpace: Boolean) {
        if (onSpace) {
            isButtonGoingUp = !isButtonGoingUp
        }
    }

    sealed class State {
        object Floating : State()
        object Enabling : State()
        object Touch : State()
    }
}
