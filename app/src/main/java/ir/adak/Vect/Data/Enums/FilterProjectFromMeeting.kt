package ir.adak.Vect.Data.Enums

enum class FilterProjectFromMeeting {
    NoFilter(-1),
    NoMeeting,//پروژه هایی که درون جلسه نیستند
    Meeting;//پروژه های درون جلسه

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