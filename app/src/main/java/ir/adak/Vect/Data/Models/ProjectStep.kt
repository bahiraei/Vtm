package ir.adak.Vect.Data.Models


enum class ProjectStep {
    NoFilter(-1),
    BackLog(1),
    ToDo,
    Doing,
    Done;

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