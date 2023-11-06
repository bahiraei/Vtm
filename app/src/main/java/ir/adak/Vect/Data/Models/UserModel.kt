package ir.adak.Vect.Data.Models

import ir.adak.Vect.Data.Enums.AccessToProjectEnum

data class UserModel(
    val fullName: String?=null,
    val isChild: Boolean=false,
    val orgLevelId: Int=0,
    val orgLevelTitle: String?=null,
    var type: Int = 0,
    val access: Int = AccessToProjectEnum.Full.value,
    var profileImage: String? = null,
    var selected: Boolean=false
)