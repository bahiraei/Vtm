package ir.adak.Vect.Data.Models

data class Group(
    val groupLevel: Int,
    val groupId: String,
    val members: List<UserModel>,
    val title: String,
    val type: Int
)