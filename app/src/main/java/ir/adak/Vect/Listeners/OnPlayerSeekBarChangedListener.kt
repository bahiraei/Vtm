package ir.adak.Vect.Listeners

import ir.adak.Vect.Data.Models.PeigiriItem.PeigiriItem

interface OnPlayerSeekBarChangedListener {
    fun onPlayerSeekBarChangedListener(peigiriItem: PeigiriItem , progress: Long)
}