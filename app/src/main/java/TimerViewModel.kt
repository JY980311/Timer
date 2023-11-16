import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

open class TimerViewModel : ViewModel() {


    var startTime = System.currentTimeMillis()
    val recordList: MutableList<String> = mutableListOf()
    val recordGapList: MutableList<String> = mutableListOf()
    var min = mutableStateOf("00")
    var sec = mutableStateOf("00")
    var msec = mutableStateOf("00")

    var str_button = "시작하기"
    var button = mutableStateOf(str_button)
    var reset_button = mutableStateOf("기록하기")

    fun timer() {
        // timer 가 눌리면 text를 멈추기로 바꾸기
        if (button.value == str_button) {
            button.value = "멈추기"
            reset_button.value = "기록하기"
        } else {
            button.value = "시작하기"
            reset_button.value = "초기화"
        }

        viewModelScope.launch {
            while (true) {
                if (button.value == str_button) {
                    break
                }
                val elapsedTime = System.currentTimeMillis() - startTime

                val elapsedSecs = elapsedTime / 1000
                val elapsedMins = elapsedSecs / 60

                msec.value = ((elapsedTime / 10) % 100).toString()
                if(msec.value.toInt() < 10) msec.value = "0" + msec.value

                sec.value = (elapsedSecs % 60).toString()
                if(sec.value.toInt() < 10) sec.value = "0" + sec.value

                min.value = elapsedMins.toString()
                if(min.value.toInt() < 10) min.value = "0" + min.value

                delay(10)
            }
        }

//        viewModelScope.launch {
//            while (true) {
//                if (button.value == str_button) {
//                    break
//                }
//                delay(10)
//                // 0.01초마다 msec 1씩 증가
//                msec.value = (msec.value.toInt() + 1).toString()
//                if (msec.value.toInt() < 10) {
//                    msec.value = "0" + msec.value
//                }
//                if (msec.value.toInt() == 100) {
//                    msec.value = "00"
//                    // msec가 100이 되면 sec 1씩 증가
//                    sec.value = (sec.value.toInt() + 1).toString()
//                    if (sec.value.toInt() < 10) {
//                        sec.value = "0" + sec.value
//                    }
//                    if (sec.value.toInt() == 60) {
//                        sec.value = "00"
//                        // sec가 60이 되면 min 1씩 증가
//                        min.value = (min.value.toInt() + 1).toString()
//                        if (min.value.toInt() < 10) {
//                            min.value = "0" + min.value
//                        }
//                    }
//                }
//            }
//        }
    }

    fun record() {
        val recordTime = min.value + ':' + sec.value + '.' + msec.value
        val recordGap = calculateGap(recordTime)

        if (reset_button.value == "초기화") {
            startTime = System.currentTimeMillis()
            min.value = "00"
            sec.value = "00"
            msec.value = "00"
            recordList.clear()
        } else {
            if(recordList.size <= 8) {
                recordList.add(recordTime)
            } else {
                recordList.add(recordTime)
               recordGapList.add(recordGap)
            }
        }
    }

    fun calculateGap(current: String): String {
        val previousRecordTime = if(recordList.isNotEmpty()) recordList.last() else "00:00.00"
        val prevParts = previousRecordTime.split(":",".")
        val currentParts = current.split(":",".")

        val prevMin = prevParts[0].toInt()
        val prevSec = prevParts[1].toInt()
        val prevMsec = prevParts[2].toInt()

        val curMin = currentParts[0].toInt()
        val curSec = currentParts[1].toInt()
        val curMsec = currentParts[2].toInt()

        val prevTotalMsec = (prevMin * 60 * 1000) + (prevSec * 1000) + prevMsec
        val curTotalMsec = (curMin * 60 * 1000) + (curSec * 1000) + curMsec

        val gap = curTotalMsec - prevTotalMsec

        val gapMin = gap / (60 * 1000)
        val gapSec = (gap % (60 * 1000)) / 1000
        val gapMsec = (gap % (60 * 1000)) % 1000

        return "${gapMin}:${gapSec}.${gapMsec}"

    }
}