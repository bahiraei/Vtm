package ir.adak.Vect.Listeners

import ir.adak.Vect.Data.Models.GetProjectMembersAndGroupsResponseModel

interface OnGetProjectMembersAndGroupsCompleted {
    fun onSuccess(getProjectMembersAndGroupsResponseModel: GetProjectMembersAndGroupsResponseModel?)
    fun onError(message: String?)
}