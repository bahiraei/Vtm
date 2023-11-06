package ir.adak.Vect.Listeners

interface OnChangeMembersCompleted {

    fun onSuccess(count: Int)
    fun onError(message: String?)
}