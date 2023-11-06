package ir.adak.Vect.Data.Retrofit

import java.io.Serializable

data class data__file_action(
    var fileName:String ?=null,
    var fileNameForShow:String ?=null,
    var fileType:Int ?=null,
    var fileExtension:String ?=null,
    var fileSize:String ?=null,
    var Down:Boolean=false
) : Serializable
