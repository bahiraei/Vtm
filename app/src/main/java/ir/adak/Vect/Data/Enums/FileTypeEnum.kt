package ir.adak.Vect.Data.Enums
enum class FileTypeEnum {
    Unknown(-1),
    Image(1),
    Video,
    Audio,
    FaceStatus;
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
        private val map = FileTypeEnum.values().associateBy(FileTypeEnum::value)
        fun fromInt(type: Int) = map[type]
    }
}