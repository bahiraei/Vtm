package ir.adak.Vect.Data.Retrofit

data class StandardServerResponse<T>(
    var data: T?,
    var message: String?,
    var isSuccess: Boolean,
    var statusCode: Int
)