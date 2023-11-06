package ir.adak.Vect.Data.Models


data class GetMembersAndGroupsResponseModel(
    val groups: MutableList<Group>,
    val partners: MutableList<UserModel>
)