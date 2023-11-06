package ir.adak.Vect.Data.Models

import android.os.Parcelable
import ir.adak.Vect.Data.Enums.FilterProjectRegisterType
import ir.adak.Vect.Data.Enums.HeldFilterEnum
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilterMeetingModel(
    var Title: String? = null,
    var DateFrom: String? = null,
    var DateTo: String? = null,
    var RegisterType: Int = FilterProjectRegisterType.NoFilter.value,
    var HeldFilter: Int = HeldFilterEnum.NoFilter.value
) : Parcelable