package ir.adak.Vect.Listeners

interface OnPinCloseCompleted {
    fun onSuccess()
    fun onError(message: String?)
}
