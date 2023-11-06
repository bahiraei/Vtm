package ir.adak.Vect.Listeners

import ir.adak.Vect.Data.Models.Note

interface OnGetNotesCompleted {
    fun onSuccess(notes: List<Note>)
    fun onError(message: String?)
}