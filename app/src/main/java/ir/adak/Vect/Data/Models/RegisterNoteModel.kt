package ir.adak.Vect.Data.Models

data class RegisterNoteModel(
    var id: String? = null,
    var title: String?,
    var description: String?,
    var dateReminder: String?,
    var timeReminder: String?
)