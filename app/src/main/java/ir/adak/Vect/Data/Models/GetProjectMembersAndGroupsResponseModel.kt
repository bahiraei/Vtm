package ir.adak.Vect.Data.Models


data class GetProjectMembersAndGroupsResponseModel(
    val groups: MutableList<Group>,
    val partners: MutableList<UserModel>,
    val currentMembers: MutableList<UserModel>?
)