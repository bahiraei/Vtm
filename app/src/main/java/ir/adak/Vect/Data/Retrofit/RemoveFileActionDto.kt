package ir.adak.Vect.Data.Retrofit

class RemoveFileActionDto {
 var FollowupId: String ?= null
 var FileName: String ?= null

    constructor(FollowupId: String?, FileName: String?) {
        this.FollowupId = FollowupId
        this.FileName = FileName
    }
}
