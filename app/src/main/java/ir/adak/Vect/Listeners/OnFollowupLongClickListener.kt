package ir.adak.Vect.Listeners

import ir.adak.Vect.Data.Models.PeigiriItem.PeigiriItem

interface OnFollowupLongClickListener {
    fun onFollowupLongClickListener(peigiriItem: PeigiriItem, isMine: Boolean)
}