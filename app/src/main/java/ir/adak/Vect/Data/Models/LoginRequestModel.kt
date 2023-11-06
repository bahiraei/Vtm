package ir.adak.Vect.Data.Models

data class LoginRequestModel(
    var SecurityKey: String?,
    var DeviceType: Int,
    var AppVersion: Int,
    var Imei: String
)