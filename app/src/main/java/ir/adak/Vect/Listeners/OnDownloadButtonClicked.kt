package ir.adak.Vect.Listeners

import ir.adak.Vect.Data.Models.PeigiriItem.PeigiriItem

interface OnDownloadButtonClicked {
    fun onDownloadButtonClicked(
        peigiriItem: PeigiriItem,
        cancel: Boolean
    )
}