package ir.adak.Vect.Listeners

import ir.adak.Vect.Data.Models.GetMeetingResponse

interface OnGetMeetingsCompleted {
    fun onSuccess(getMeetingResponse: GetMeetingResponse?)
    fun onError(message: String?)
}