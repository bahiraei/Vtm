package ir.adak.Vect.Data.Models

data class AppVersion(
    val allowedToLogin: Boolean,
    val currentVersion: Int,
    val description: String,
    val url: String
)