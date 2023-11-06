package ir.adak.Vect.Data.Retrofit

import ir.adak.Vect.Data.Models.*
import ir.adak.Vect.UI.Activities.AddActionActivity.RegisteroreditFollowUpModel_2
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface Api {
    @Multipart
    @POST("Account/Edit")
    fun EditUser(
        @Header("Authorization") token: String?,
        @Part("body") registerUserRequestModel: RegisterUserRequestModel
    ): Call<StandardServerResponse<Boolean>>


    @Multipart
    @POST("Account/ConfirmSms")
    fun confirmSms(
        @Part("body") confirmSmsRequestModel: ConfirmSmsRequestModel?
    ): Call<StandardServerResponse<ConfirmSmsResponseModel>>
    @Multipart
    @POST("FollowUp/ChangeStatusAction")
    fun Accept_Action_State(
        @Header("Authorization") token: String?,
        @Part("body") confirmSmsRequestModel: Data_Accept_Action_State?
    ): Call<StandardServerResponse<Boolean>>

    @Multipart
    @POST("Account/Login")
    fun login(
        @Part("body") loginRequestModel: LoginRequestModel?
    ): Call<StandardServerResponse<LoginResponseModel>>

    @Multipart
    @POST("Project/Get")
    fun getProjects(
        @Header("Authorization") token: String?,
        @Part("page") page: Int,
        @Part("body") filterProjectModel: FilterProjectModel?
    ): Call<StandardServerResponse<GetProjectsResponseModel>>

    @Multipart
    @POST("Project/Start")
    fun startProject(
        @Header("Authorization") token: String?,
        @Part("body") projectId: RequestBody
    ): Call<StandardServerResponse<Boolean>>

    @Multipart
    @POST("Project/Register")
    fun registerProject(
        @Header("Authorization") token: String?,
        @Part("body") registerProjectModel: RegisterProjectModel? = null
    ): Call<StandardServerResponse<RegisterProjectResponse>>

    @POST("MemberAndGroup/Get")
    fun getMembersAndGroups(
        @Header("Authorization") token: String?
    ): Call<StandardServerResponse<GetMembersAndGroupsResponseModel>>

    @Multipart
    @POST("MemberAndGroup/FindPersonel")
    fun searchInPersonel(
        @Header("Authorization") token: String?,
        @Part("body") text: RequestBody
    ): Call<StandardServerResponse<List<UserModel>>>

    @Multipart
    @POST("Followup/get")
    fun getFollowUps(
        @Header("Authorization") token: String?,
        @Part("body") projectId: RequestBody
    ): Call<StandardServerResponse<GetFollowUpResponse>>

    @Multipart
    @POST("Followup/PinFollowUp")
    fun pinFollowUp(
        @Header("Authorization") token: String?,
        @Part("body") followupId: RequestBody
    ): Call<StandardServerResponse<Boolean>>

    @Multipart
    @POST("Followup/PinReadOrClose")
    fun closePinFollowUp(
        @Header("Authorization") token: String?,
        @Part("body") projectId: RequestBody
    ): Call<StandardServerResponse<Boolean>>

    @Multipart
    @POST("FollowUp/RegOrEditAction")
    fun RegOrEditAction(
        @Header("Authorization") token: String?,
        @Part("body") registeroreditFollowUpModel: RegisteroreditFollowUpModel,
        @Part file: ArrayList<MultipartBody.Part>
    ): Call<StandardServerResponse<Data_Action>>
    @Multipart
    @POST("FollowUp/RegOrEditAction")
    fun RegOrEditAction_2(
        @Header("Authorization") token: String?,
        @Part("body") registeroreditFollowUpModel: RegisteroreditFollowUpModel_2
    ): Call<StandardServerResponse<Data_Action>>

    @Multipart
    @POST("FollowUp/Register")
    fun registerTextFollowUp(
        @Header("Authorization") token: String?,
        @Part("body") registerFollowUpModel: RegisterFollowUpModel
    ): Call<StandardServerResponse<FollowUp>>

    @Multipart
    @POST("FollowUp/Edit")
    fun editTextFollowUp(
        @Header("Authorization") token: String?,
        @Part("body") registerFollowUpModel: RegisterFollowUpModel
    ): Call<StandardServerResponse<FollowUp>>


    @POST("Amar/GetHomeAmar")
    fun GetHomeAmar(
        @Header("Authorization") token: String?
    ): Call<StandardServerResponse<data>>


    @Multipart
    @POST("FollowUp/Register")
    fun registerFileFollowUp(
        @Header("Authorization") token: String?,
        @Part("body") registerFollowUpModel: RegisterFollowUpModel,
        @Part file: MultipartBody.Part
    ): Call<StandardServerResponse<FollowUp>>
    @Multipart
    @POST("FollowUp/AddFileAction")
    fun AddFileAction(
        @Header("Authorization") token: String?,
        @Part("body")  projectId: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<StandardServerResponse<FollowUp>>

    @Multipart
    @POST("Project/GetDetails")
    fun getProjectDetails(
        @Header("Authorization") token: String?,
        @Part("body") projectId: RequestBody
    ): Call<StandardServerResponse<GetProjectDetailsResponse>>


    @Multipart
    @POST("Project/ActiveDeactive")
    fun activeDeactiveProject(
        @Header("Authorization") token: String?,
        @Part("body") projectId: RequestBody
    ): Call<StandardServerResponse<Boolean>>

    @Multipart
    @POST("Project/Close")
    fun closeProject(
        @Header("Authorization") token: String?,
        @Part("body") projectId: RequestBody
    ): Call<StandardServerResponse<Boolean>>

    @Multipart
    @POST("Project/Delete")
    fun deleteProject(
        @Header("Authorization") token: String?,
        @Part("body") projectId: RequestBody
    ): Call<StandardServerResponse<Boolean>>

    @Multipart
    @POST("Project/Edit")
    fun editProject(
        @Header("Authorization") token: String?,
        @Part("body") editProjectModel: EditProjectModel
    ): Call<StandardServerResponse<EditProjectResponse>>

    @Multipart
    @POST("FollowUp/Delete")
    fun deleteFollowUp(
        @Header("Authorization") token: String?,
        @Part("body") followupId: RequestBody
    ): Call<StandardServerResponse<Boolean>>

    @Multipart
    @POST("Project/ChangeMember")
    fun projectChangeMember(
        @Header("Authorization") token: String?,
        @Part("body") memberChangedDto: MemberChangedDto
    ): Call<StandardServerResponse<Int>>

    @Multipart
    @POST("Project/AddMember")
    fun projectAddMember(
        @Header("Authorization") token: String?,
        @Part("body") memberChangedDto: MemberChangedDto
    ): Call<StandardServerResponse<Int>>

    @Multipart
    @POST("Project/ChangeProgress")
    fun projectChangeProgress(
        @Header("Authorization") token: String?,
        @Part("body") changeProgressModel: ChangeProgressModel
    ): Call<StandardServerResponse<FollowUp>>

    @Multipart
    @POST("Project/GetMemberAndGroups")
    fun getProjectMemberAndGroups(
        @Header("Authorization") token: String?,
        @Part("body") projectId: RequestBody?
    ): Call<StandardServerResponse<GetProjectMembersAndGroupsResponseModel>>


    @Multipart
    @POST("Meeting/GetMemberAndGroups")
    fun getMeetingMemberAndGroups(
        @Header("Authorization") token: String?,
        @Part("body") meetingId: RequestBody?
    ): Call<StandardServerResponse<GetProjectMembersAndGroupsResponseModel>>

    @Multipart
    @POST("Meeting/CloseMeeting")
    fun closeMeeting(
        @Header("Authorization") token: String?,
        @Part("body") meetingId: RequestBody?
    ): Call<StandardServerResponse<Boolean>>


    @Multipart
    @POST("Meeting/Get")
    fun getMeetings(
        @Header("Authorization") token: String?,
        @Part("page") page: Int,
        @Part("body") filterMeetingModel: FilterMeetingModel?
    ): Call<StandardServerResponse<GetMeetingResponse>>

    @Multipart
    @POST("Meeting/RegisterOrEdit")
    fun registerMeeting(
        @Header("Authorization") token: String?,
        @Part("body") registerMeetingRequestModel: RegisterMeetingRequestModel
    ): Call<StandardServerResponse<RegisterMeetingResponse>>

    @Multipart
    @POST("Meeting/GetMeetingProject")
    fun getMeetingProjectsAndOffers(
        @Header("Authorization") token: String?,
        @Part("body") meetingId: RequestBody?
    ): Call<StandardServerResponse<GetMeetingProjectsAndOffersResponse>>
    @Multipart
    @POST("FollowUp/DeleteAction")
    fun DeleteAction(
        @Header("Authorization") token: String?,
        @Part("body") followupId: RequestBody?
    ): Call<StandardServerResponse<Boolean>>



    @Multipart
    @POST("FollowUp/DeleteFileAction")
    fun DeleteFileAction(
        @Header("Authorization") token: String?,
        @Part("body") followupId: RemoveFileActionDto?
    ): Call<StandardServerResponse<FollowUp>>

    @POST("Note/Get")
    fun getNotes(
        @Header("Authorization") token: String?
    ): Call<StandardServerResponse<List<Note>>>

    @Multipart
    @POST("Offer/RegisterOrEdit")
    fun registerOrEditOffer(
        @Header("Authorization") token: String?,
        @Part("body") registerOfferModel: RegisterOfferModel
    ): Call<StandardServerResponse<Offer>>

    @Multipart
    @POST("Offer/RegisterOfferToMeeting")
    fun registerOfferToMeeting(
        @Header("Authorization") token: String?,
        @Part("body") offerToMeetingModel: OfferToMeetingModel
    ): Call<StandardServerResponse<Boolean>>

    @Multipart
    @POST("Note/RegisterOrEdit")
    fun registerOrEditNote(
        @Header("Authorization") token: String?,
        @Part("body") registerNoteModel: RegisterNoteModel
    ): Call<StandardServerResponse<Note>>

    @Multipart
    @POST("Amar/GetAmarMaster")
    fun GetAmarMaster(
        @Header("Authorization") token: String?,
        @Part("body") Year: RequestBody?
    ): Call<StandardServerResponse<data_8>>


    @Multipart
    @POST("Note/Delete")
    fun deleteNote(
        @Header("Authorization") token: String?,
        @Part("body") noteId: RequestBody?
    ): Call<StandardServerResponse<Boolean>>




    @POST("ProjectScheduler/Get")
    fun getScheduledProject(
        @Header("Authorization") token: String?
    ): Call<StandardServerResponse<List<ScheduledProjectModel>>>

    @Multipart
    @POST("ProjectScheduler/RegisterOrEdit")
    fun registerOrEditScheduledProject(
        @Header("Authorization") token: String?,
        @Part("body") regOrEditProjectSchedulerModel: RegOrEditProjectSchedulerModel
    ): Call<StandardServerResponse<ScheduledProjectModel>>

    @Multipart
    @POST("ProjectScheduler/ActiveDeactive")
    fun ActiveDeactiveScheduledProject(
        @Header("Authorization") token: String?,
        @Part("body") jobId: RequestBody?
    ): Call<StandardServerResponse<Boolean>>

    @Multipart
    @POST("ProjectScheduler/Delete")
    fun DeleteScheduledProject(
        @Header("Authorization") token: String?,
        @Part("body") jobId: RequestBody?
    ): Call<StandardServerResponse<Boolean>>


    @Multipart
    @POST("Account/SetSecretary")
    fun SelectAdmin(
        @Header("Authorization") token: String?,
        @Part("body") jobId: RequestBody?
    ): Call<StandardServerResponse<Boolean>>
    @Multipart
    @POST("ProjectScheduler/GetMemberAndGroups")
    fun GetMemberScheduledProject(
        @Header("Authorization") token: String?,
        @Part("body") jobId: RequestBody?
    ): Call<StandardServerResponse<GetProjectMembersAndGroupsResponseModel>>
    @Multipart
    @POST("Account/Register")
    fun registerUser(
        @Header("Authorization") token: String?,
        @Part("body") registerUserRequestModel: RegisterUserRequestModel
    ): Call<StandardServerResponse<Boolean>>

//    @POST("Account/GetMyPersonel")
    @POST("Account/GetPartnerAndSecretary")
    fun getMyPersonel(
        @Header("Authorization") token: String?
//    ): Call<StandardServerResponse<MutableList<GetMyPersonelResponse>>>
    ): Call<StandardServerResponse<GetMyPersonelResponse>>

    @POST("Account/RemoveSecretary")
    fun DelAdmin(
        @Header("Authorization") token: String?
//    ): Call<StandardServerResponse<MutableList<GetMyPersonelResponse>>>
    ): Call<StandardServerResponse<Boolean>>

}