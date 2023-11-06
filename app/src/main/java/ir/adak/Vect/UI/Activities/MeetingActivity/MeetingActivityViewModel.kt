package ir.adak.Vect.UI.Activities.MeetingActivity

import androidx.lifecycle.ViewModel
import ir.adak.Vect.Data.Models.*

class MeetingActivityViewModel : ViewModel() {

    var meetingProjects: MutableList<Project>? = null
    var meetingMembers: MutableList<UserModel>? = null
    var meetingOfferItems: MutableList<OfferItem>? = null
    var meetingOffers: MutableList<Offer>? = null

}