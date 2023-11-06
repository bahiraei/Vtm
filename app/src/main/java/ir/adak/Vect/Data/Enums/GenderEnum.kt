package ir.adak.Vect.Data.Enums

enum class GenderEnum {

    Male(1),
    Female;

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