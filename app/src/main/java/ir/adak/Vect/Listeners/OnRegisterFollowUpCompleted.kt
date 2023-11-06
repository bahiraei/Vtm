package ir.adak.Vect.Listeners

import ir.adak.Vect.Data.Models.FollowUp

interface OnRegisterFollowUpCompleted {
    fun onSuccess(followUp: FollowUp?)
    fun onError(message: String?)
}