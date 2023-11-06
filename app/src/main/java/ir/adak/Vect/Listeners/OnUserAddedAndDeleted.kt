package ir.adak.Vect.Listeners

import ir.adak.Vect.Data.Models.UserModel

interface OnUserAddedAndDeleted {
    fun OnUserAdded(userModel: UserModel)
    fun OnUserDeleted(userModel: UserModel)
}