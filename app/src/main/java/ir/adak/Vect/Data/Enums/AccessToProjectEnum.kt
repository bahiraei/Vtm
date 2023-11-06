package ir.adak.Vect.Data.Enums

enum class AccessToProjectEnum {

    Full(1),
    ReadOnly;

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