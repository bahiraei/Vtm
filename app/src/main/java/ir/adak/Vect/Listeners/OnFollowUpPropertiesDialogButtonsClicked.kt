package ir.adak.Vect.Listeners

import ir.adak.Vect.Data.Models.PeigiriItem.PeigiriItem


interface OnFollowUpPropertiesDialogButtonsClicked {
    fun onBtnReplyFollowUpClicked(peigiriItem: PeigiriItem?)
    fun onBtnPinFollowUpClicked(peigiriItem: PeigiriItem?)
    fun onBtnEditFollowUpClicked(peigiriItem: PeigiriItem?)
    fun onBtnCopyFollowUpTextClicked(peigiriItem: PeigiriItem?)
    fun onBtnDeleteFollowUpClicked(peigiriItem: PeigiriItem?)
}