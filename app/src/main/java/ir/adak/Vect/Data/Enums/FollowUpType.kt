package ir.adak.Vect.Data.Enums

enum class FollowUpType {
    Text(1),
    File,
    Description,
    ChangeProgress,
    Deleted,
    EnterMember,
    ExitMember,
    ActiveProject,
    DeactiveProject,
    ClosedProject,
    EditProject,
    NewProject,
    NewMeeting,
    CreatedProject,
    SummaryProject,
    SummaryEndProject,
//    Jobs,
//    Meeting,
    ActionTime;


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
        private val map = values().associateBy(FollowUpType::value)
        fun fromInt(type: Int) = map[type]
    }

}