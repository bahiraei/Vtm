package ir.adak.Vect.Data.Models

import ir.adak.Vect.Data.Enums.JobType

data class JobModel(
    var jobType: JobType,
    var title: String,
    var username: String,
    var semat: String,
    var avatarImageResource: Int,
    var startDate: String,
    var enddate: String,
    var progress: Float,
    val active : Boolean
)