package ir.adak.Vect.Listeners

import ir.adak.Vect.Data.Models.PeigiriItem.PeigiriItem

interface OnCancelUploadClicked {
    fun onCancelUploadClicked(
        peigiriItem: PeigiriItem
    )
}