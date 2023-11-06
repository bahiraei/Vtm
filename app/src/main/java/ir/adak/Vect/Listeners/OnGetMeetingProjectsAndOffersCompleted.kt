package ir.adak.Vect.Listeners

import ir.adak.Vect.Data.Models.GetMeetingProjectsAndOffersResponse

interface OnGetMeetingProjectsAndOffersCompleted {
    fun onSuccess(getMeetingProjectsAndOffersResponse: GetMeetingProjectsAndOffersResponse?)
    fun onError(message: String?)
}