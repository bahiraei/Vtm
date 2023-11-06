package ir.adak.Vect.Listeners

interface OnPinFollowUpCompleted {
    fun onSuccess()
    fun onError(message: String?)
}
