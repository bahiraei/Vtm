package ir.adak.Vect.Listeners

import ir.adak.Vect.Data.Models.UserModel

interface OnSearchInPersonelCompleted {
    fun OnSuccess(personel: List<UserModel>?)
    fun OnFailure(errorMessage: String?)
}