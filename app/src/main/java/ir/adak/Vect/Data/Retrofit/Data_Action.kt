package ir.adak.Vect.Data.Retrofit

import java.io.Serializable

class Data_Action : Serializable {
var projectId :String ?=null
var orgLevelId :Int ?=null
var orgLevelTitle :String ?=null
var type :Int    ?=null
var description :String ?=null
var fileName :String ?=null
var fileNameForShow :String ?=null
var fileType :Int ?=null
var fileExtension :String ?=null
var memberFullName :Int ?=null
var memberTitle :String ?=null
var memberOrgLevelId :Int ?=null
var changeProject :String ?=null
var newProjectTitle :String ?=null
var newProjectId :Int ?=null
var newProjectTypeId :Int ?=null
var newMeetingTitle :String ?=null
var newMeetingId :Int ?=null
var fprogress :Int ?=null
var tprogress :Int ?=null
var isReply :Boolean ?=null
var isEdited :Boolean ?=null
var editData :String ?=null
var endProject_ProjectId :Int ?=null
var endProject_ProjectTitle :String ?=null
var endProject_OrgLevelId :Int ?=null
var endProject_OrgLevelTitle :String ?=null
var endProject_PersonFullName :String ?=null
var endProject_ProjectStartDate :String ?=null
var endProject_ProjectEndDate :String ?=null
var endProject_ProjectTypeId :Int ?=null
var endProject_Priority :String ?=null
var endProject_FollowupCount :Int ?=null
var endProject_TimeActivity :String ?=null
var endProject_IsDelayClosed :Boolean ?=null
var endProject_ProjectCount :Int ?=null
var actionTitle :String ?=null
var actionTime :String ?=null
var actionDatePersian :String ?=null
var rejectedDesc :String ?=null
var actionOrgLevelId :Int ?=null
var actionFullName :String ?=null
var actionOrgLevelTitle :String ?=null
var id :String ?=null
var persianDate :String ?=null
var createName :String ?=null
var profileImage :String ?=null
var newProjectAllowToAccess :Boolean ?=null
var newProjectTypeName :String ?=null
var newProjectTypeColor :String ?=null
var newMeetingOrgLevel :String ?=null
var newProjectTypeIconOrFont :String ?=null
var newMeetingAllowToAccess :Boolean ?=null
var replyFollowUp :String ?=null
var endProject_ProjectTypeName :String ?=null
var endProject_ProjectTypeColor :String ?=null
var endProject_ProjectTypeIconOrFont :String ?=null
var actionAccepted :String ?=null
    var actionFiles:ArrayList<data__file_action> ? =null
}
