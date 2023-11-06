package ir.adak.Vect.UI.Activities

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ir.adak.Vect.Data.Models.*

class AddUsersViewModel : ViewModel() {

    val selectedUsers = MutableLiveData<MutableList<UserModel>>()
    val tempSelectedUsers = MutableLiveData<MutableList<UserModel>>()
    var tempPersonels: MutableList<UserModel>? = mutableListOf<UserModel>()
    var selectedPersonel: MutableList<UserModel>? = mutableListOf<UserModel>()
    var tempPartners = mutableListOf<UserModel>()
    var selectedPartners = mutableListOf<UserModel>()
    var tempGroups = mutableListOf<GroupHeaderItem>()
    var selectedGroups = mutableListOf<GroupHeaderItem>()

    init {
        selectedUsers.value = mutableListOf()
        tempSelectedUsers.value = mutableListOf()
    }


}