package ir.adak.Vect.Data.Models

data class MemberChangedDto(
    var members: MutableList<MemberDto>,
    var projectID: String
)