package ir.adak.Vect.Data.Enums

enum class FilterProjectRegisterType {
    NoFilter(-1),
    Myself,//ثبت شده توسط خودم
    Other;//ثبت شده توسط دیگران

    val value: Int

    private object Counter {
        var value: Int = 0
    }

    constructor() {
        Counter.value++
        value = Counter.value.toInt()
    }

    private constructor(value: Int) {
        this.value = value
        Counter.value = value
    }
}