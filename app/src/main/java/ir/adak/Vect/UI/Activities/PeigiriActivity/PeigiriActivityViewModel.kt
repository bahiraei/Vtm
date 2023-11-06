package ir.adak.Vect.UI.Activities.PeigiriActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ir.adak.Vect.Data.Models.*

class PeigiriActivityViewModel : ViewModel() {

    var currentFilesCount = MutableLiveData<Int>()
    var currentMediaCount = MutableLiveData<Int>()

    var NO_FILTER: Boolean? = false
    var MY_FOLLOWUPS: Boolean? = false
    var OTHERS_FOLLOWUPS: Boolean? = false
    var IMAGES_FOLLOWUPS: Boolean? = false
    var VIDEOS_FOLLOWUPS: Boolean? = false
    var AUDIO_FOLLOWUPS: Boolean? = false
    var FILES_FOLLOWUPS: Boolean? = false
//    var SORAT_JALASE_FOLLOWUPS: Boolean? = false
    var Jobs: Boolean? = false
    var Meeting: Boolean? = false
    var Action: Boolean? = false
    var currentSortItem: FollowUpSortItem? = null
    var DATE_SORT: Boolean = true
    var SOUDI_SORT: Boolean = true

}