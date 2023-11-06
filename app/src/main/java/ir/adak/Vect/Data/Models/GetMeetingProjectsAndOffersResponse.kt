package ir.adak.Vect.Data.Models

import ir.adak.Vect.meeting


data class GetMeetingProjectsAndOffersResponse(
    var offers: List<Offer>? = null,
    var projects: List<Project>? = null,
    var members: List<UserModel>? = null,
    var meeting: meeting? = null
)