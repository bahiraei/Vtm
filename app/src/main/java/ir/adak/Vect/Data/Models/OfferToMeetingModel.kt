package ir.adak.Vect.Data.Models

data class OfferToMeetingModel(
    var OfferId: String,
    var MeetingId: String?  // null == پیشنهاد از جلسه خارج می شود
)