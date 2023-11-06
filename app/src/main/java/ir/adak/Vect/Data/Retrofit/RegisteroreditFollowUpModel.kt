package ir.adak.Vect.Data.Retrofit

class RegisteroreditFollowUpModel {
 var ProjectId :String ? =null
 var FollowupId  :Int ?=null
 var ActionTitle :String ?=null
 var Description  :String ?=null
 var ActionTime  :String ?=null
 var ActionDate  :String ?=null
    constructor(
        ProjectId: String,
        FollowupId: Int?,
        ActionTitle: String?,
        Description: String?,
        ActionTime: String?,
        ActionDate: String?
    ) {
        this.ProjectId = ProjectId
        this.FollowupId = FollowupId
        this.ActionTitle = ActionTitle
        this.Description = Description
        this.ActionTime = ActionTime
        this.ActionDate = ActionDate
    }
}
