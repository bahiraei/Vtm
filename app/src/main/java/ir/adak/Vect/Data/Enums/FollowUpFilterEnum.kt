package ir.adak.Vect.Data.Enums

enum class FollowUpFilterEnum {
    IMAGES,
    FILES,
    FaceStatus,
    VIDEOS,
    AUDIO;


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
        private val map = values().associateBy(FollowUpFilterEnum::value)
        fun fromInt(type: Int) = map[type]
    }
}