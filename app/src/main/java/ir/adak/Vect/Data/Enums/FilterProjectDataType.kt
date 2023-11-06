package ir.adak.Vect.Data.Enums

enum class FilterProjectDataType {
    NoFilter(-1),
    Range,//رنج تاریخ- از تاریخ تا تاریخ
    EndProject,//چند روز مانده به اتمام تاریخ پروژه
    LastFollowUp;//چند روز گذشته از آخرین پیگیری

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