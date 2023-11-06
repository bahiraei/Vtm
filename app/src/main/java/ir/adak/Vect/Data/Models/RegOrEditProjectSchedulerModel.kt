package ir.adak.Vect.Data.Models

data class RegOrEditProjectSchedulerModel(
    var id: String ,
    var title: String,
    var description: String,
    var persianStartDate: String,
    var letterDatefa: String,
    var letterNumber: String,
    var startTime: String,
    var priority: Int,
    var projectTypeId: Int,
    var schedulerType: Int,
    var endDateType: Int,
    var endProjectNumber: Int,
    var daysOfWeekOrMonth: List<Int>,
    var PrimaryCredit: Int,
    var members: List<MemberDto>

)