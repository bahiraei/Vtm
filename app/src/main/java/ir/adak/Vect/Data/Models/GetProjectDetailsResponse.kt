package ir.adak.Vect.Data.Models

data class GetProjectDetailsResponse(
    val followupFileCount: List<FollowupFileCountDto>,
    val meetingCount: Int,
    val memberLog: List<MemberLogDto>,
    val members: List<UserModel>,
    val projectAmarCount: ArrayList<CreateProjectTypeCountDto>,
    val projectInfo: Project,
    val projectParent: projectParent
)