package ir.adak.Vect.Listeners

import ir.adak.Vect.Data.Models.GetProjectsResponseModel

interface OnGetProjectCompleted {
    fun onSuccess(getProjectsResponseModel: GetProjectsResponseModel?)
    fun onError(message: String?)
}