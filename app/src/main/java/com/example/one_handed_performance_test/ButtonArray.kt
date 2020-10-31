package com.example.one_handed_performance_test

import android.content.Context
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.button_array.view.*

import com.example.one_handed_performance_test.MainActivity.Companion.to//计数
import com.example.one_handed_performance_test.MainActivity.Companion.zc
import com.example.one_handed_performance_test.MainActivity.Companion.cm
import com.example.one_handed_performance_test.MainActivity.Companion.block
import com.example.one_handed_performance_test.MainActivity.Companion.toOpr
import com.example.one_handed_performance_test.MainActivity.Companion.zcOpr
import com.example.one_handed_performance_test.MainActivity.Companion.cmOpr
import com.example.one_handed_performance_test.MainActivity.Companion.TO
import com.example.one_handed_performance_test.MainActivity.Companion.ZC
import com.example.one_handed_performance_test.MainActivity.Companion.CM
import kotlinx.android.synthetic.main.activity_play.view.*

class ButtonArray(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs){
    private val errorAudioPlayer = MediaPlayer()//播放音频对象
    private val rightAudioPlayer = MediaPlayer()
    private var error = 0
    private var clickTheTarget = false
    private var buttonList: List<Button>

    init {
        LayoutInflater.from(context).inflate(R.layout.button_array, this)
        initMediaPlayer()
        buttonList = listOf(button_0, button_1, button_2, button_3, button_4)
        setButtonListener()
    }
    private fun setButtonListener() {
        for (bt in buttonList) {
            setEachButtonListener(bt)
        }
    }
    private fun setEachButtonListener(bt: Button) {
        if (bt == button_2) {
            bt.setOnClickListener {
                bt.setBackgroundResource(R.drawable.shape_circle_green)
                rightAudioPlayer.start()

                cm++
                if(cm==2){
                    cm=0
                    zc++
                }
                if(zc==5){
                    zc=0
                    to++
                }
                if(to==4){
                    to=0
                    block++
                }
                if(block==3&&to==4&&zc==5&&cm==2)
                    Toast.makeText(context, "This is the last one!", Toast.LENGTH_SHORT).show()

                toOpr = TO[to]
                zcOpr = ZC[zc]
                cmOpr = CM[cm]

                clickTheTarget = true
            }
        }
        else {
            bt.setOnClickListener {
                bt.setBackgroundResource(R.drawable.shape_circle_red)
                errorAudioPlayer.start()
                error++
            }
        }
    }

    private fun initMediaPlayer() {
        val assetManager = context.assets
        val error = assetManager.openFd("error.mp3")
        val right = assetManager.openFd("right.mp3")
        errorAudioPlayer.setDataSource(error.fileDescriptor, error.startOffset, error.length)
        errorAudioPlayer.prepare()
        rightAudioPlayer.setDataSource(right.fileDescriptor, right.startOffset, right.length)
        rightAudioPlayer.prepare()
    }

    fun refresh() {
        for (bt in buttonList) {
            if (bt == button_2)
                bt.setBackgroundResource(R.drawable.shape_circle_blue)
            else
                bt.setBackgroundResource(R.drawable.shape_circle_grey)
        }
        clickTheTarget = false
    }

    fun release() {
        errorAudioPlayer.stop()
        errorAudioPlayer.release()
        rightAudioPlayer.stop()
        rightAudioPlayer.release()
    }

    fun getError() = error
    fun getClickTheTarget() = clickTheTarget

}