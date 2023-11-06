package ir.adak.Vect.UI.Activities.AddActionActivity

class RegisteroreditFollowUpModel_2 {
    var ProjectId :String ? =null
    var FollowupId  :String ?=null
    var ActionTitle :String ?=null
    var Description  :String ?=null
    var ActionTime  :String ?=null
    var ActionDate  :String ?=null
    constructor(
        ProjectId: String,
        FollowupId: String?,
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
