package ir.adak.Vect.Data.Enums

enum class ProjectType {
    Purpose(1),//هدف
    Project,
    Job,//کار
    Incident,
    Problem,
    Offer;//حادثه

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