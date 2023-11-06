package ir.adak.Vect.Listeners

import ir.adak.Vect.Data.Models.GetFollowUpResponse

interface OnGetFollowUpsCompleted {
    fun onSuccess(getFollowUpResponse: GetFollowUpResponse?)
    fun onError(message: String?)
}