package ir.adak.Vect.Data.Enums

enum class HeldFilterEnum {

    NoFilter(-1),
    NotHelded(0),
    Helded;

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

    companion object {
        private val map = HeldFilterEnum.values().associateBy(HeldFilterEnum::value)
        fun fromInt(type: Int) = map[type]
    }
}
