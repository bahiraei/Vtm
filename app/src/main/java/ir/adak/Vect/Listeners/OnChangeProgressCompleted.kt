package ir.adak.Vect.Listeners

interface OnChangeProgressCompleted {
    fun onSuccess()
    fun onError(message: String?)
}
