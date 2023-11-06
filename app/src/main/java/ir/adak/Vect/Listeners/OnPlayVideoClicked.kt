package ir.adak.Vect.Listeners

import java.io.File

interface OnPlayVideoClicked {
    fun onPlayVideoClicked(file: File?, videoName: String, videoUrl: String)
}