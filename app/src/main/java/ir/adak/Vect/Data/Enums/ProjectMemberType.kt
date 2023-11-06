package ir.adak.Vect.Data.Enums

enum class ProjectMemberType {
    NoFilter(-1),
    Creator(1),
    Mojry,
    Hamkar;

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