package ir.adak.Vect.Listeners

import ir.adak.Vect.Data.Models.GetProjectDetailsResponse

interface OnGetProjectDetailsCompleted {
    fun onSuccess(getProjectDetailsResponse: GetProjectDetailsResponse?)
    fun onError(message: String?)
}