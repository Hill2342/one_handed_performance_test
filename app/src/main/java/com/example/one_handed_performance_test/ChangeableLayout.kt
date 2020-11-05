package com.example.one_handed_performance_test

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.media.MediaPlayer
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import com.example.one_handed_performance_test.PlayActivity.Companion.Ti
import com.example.one_handed_performance_test.PlayActivity.Companion.To
import com.example.one_handed_performance_test.PlayActivity.Companion.Ts
import com.example.one_handed_performance_test.PlayActivity.Companion.flag
import com.example.one_handed_performance_test.PlayActivity.Companion.lockdown
import com.example.one_handed_performance_test.PlayActivity.Companion.tmpt
import kotlinx.android.synthetic.main.button_array.view.*
import org.apache.log4j.chainsaw.Main
import java.util.*
import java.util.Collections.shuffle

class ChangeableLayout(context: Context, attrs: AttributeSet): RelativeLayout(context, attrs) {
    private val errorAudioPlayer = MediaPlayer()//播放音频对象
    private val rightAudioPlayer = MediaPlayer()
    private var buttonList: List<Button>//按钮列表
    init {
        LayoutInflater.from(context).inflate(R.layout.changeable_layout, this)
        initMediaPlayer()
        buttonList = listOf(button_0, button_1, button_2, button_3, button_4)
        setButtonListener()
    }

    //设置每个按钮的点击事件
    private fun setButtonListener() {
        for (bt in buttonList) {
            setEachButtonListener(bt)
        }
    }

    //设置按钮的点击事件
    @SuppressLint("SetTextI18n")
    private fun setEachButtonListener(bt: Button) {
        if (bt == button_2) {
            bt.setOnClickListener {
                if (Ti != 0L && Ts == 0L) {
                    Ts = System.currentTimeMillis() - tmpt
                    println("三Ts时间为" + Ts)
                    Log.d("手指", "subPlayChanged: ts成功开始")
                    lockdown = 0
                }
                bt.setBackgroundResource(R.drawable.shape_circle_green)
                rightAudioPlayer.start()

                MainActivity.select++
                if (MainActivity.select==16){
//                    MainActivity.select=0
                    MainActivity.cm++
                }
                if(MainActivity.cm ==2){
                    MainActivity.cm =0
                    MainActivity.zc++
                }
                if(MainActivity.zc ==5){
                    MainActivity.zc =0
                    MainActivity.to++
                }
                if(MainActivity.to==4){
                    MainActivity.to=0
                    MainActivity.block++
                    Collections.shuffle(MainActivity.ZC)
                    Collections.shuffle(MainActivity.CM)
                }
                if(MainActivity.block==2&&MainActivity.to ==3&& MainActivity.zc ==4&& MainActivity.cm ==1)
                    Toast.makeText(context, "This is the last ont!", Toast.LENGTH_SHORT).show()
                MainActivity.toOpr = MainActivity.TO[MainActivity.to]
                MainActivity.zcOpr = MainActivity.ZC[MainActivity.zc]
                MainActivity.cmOpr = MainActivity.CM[MainActivity.cm]
                layoutRefresh()
            }
        }
        else {
            bt.setOnClickListener {
                bt.setBackgroundResource(R.drawable.shape_circle_red)
                errorAudioPlayer.start()
                layoutRefresh()
            }
        }
    }

    //初始化音频资源
    private fun initMediaPlayer() {
        val assetManager = context.assets
        val error = assetManager.openFd("error.mp3")
        val right = assetManager.openFd("right.mp3")
        errorAudioPlayer.setDataSource(error.fileDescriptor, error.startOffset, error.length)
        errorAudioPlayer.prepare()
        rightAudioPlayer.setDataSource(right.fileDescriptor, right.startOffset, right.length)
        rightAudioPlayer.prepare()
    }

    //刷新按钮
    fun buttonsRefresh() {
        for (bt in buttonList) {
            if (bt == button_2)
                bt.setBackgroundResource(R.drawable.shape_circle_blue)
            else
                bt.setBackgroundResource(R.drawable.shape_circle_grey)
        }
    }

    //刷新布局位置，同时改变按钮方位
    fun layoutRefresh() {

        val layout: RelativeLayout = findViewById(R.id.changeableLayout)//刷新后的控件
        layout.scaleX=1F
        layout.scaleY=1F
        layout.translationX = MainActivity.transX
        layout.translationY = MainActivity.transY
        val layoutButtons: LinearLayout = findViewById(R.id.buttons)//重新设置按钮的相对位置
        val paramsButtons: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(100.toPxInt(),100.toPxInt())

        val kind = (1..9).random()
        when(kind){//选择按钮更新位置
            1 -> paramsButtons.addRule(RelativeLayout.ALIGN_PARENT_LEFT)//左上角
            2 -> paramsButtons.addRule(RelativeLayout.CENTER_HORIZONTAL)//上边正中
            3 -> paramsButtons.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)//右上角
            4 -> paramsButtons.addRule(RelativeLayout.CENTER_VERTICAL)//左边正中
            5 -> paramsButtons.addRule(RelativeLayout.CENTER_IN_PARENT)//中心
            6 -> {
                paramsButtons.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)//右边正中
                paramsButtons.addRule(RelativeLayout.CENTER_VERTICAL)
            }
            7 -> paramsButtons.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)//左下角
            8 -> {
                paramsButtons.addRule(RelativeLayout.CENTER_HORIZONTAL)//下边正中
                paramsButtons.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            }
            9 -> {
                paramsButtons.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)//右下角
                paramsButtons.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            }
        }
        layoutButtons.layoutParams=paramsButtons//重绘
        buttonsRefresh()
    }

    private fun Int.toPxInt():Int=(this* Resources.getSystem().displayMetrics.density).toInt()
    private fun Float.toPxFloat():Float=(this* Resources.getSystem().displayMetrics.density).toFloat()

    //设置布局的位置
    fun setLocation(x: Float, y:Float) {

    }

    //设置布局的大小
    fun setSizeOfLayout() {

    }

    //释放音频资源
    fun release() {
        errorAudioPlayer.stop()
        errorAudioPlayer.release()
        rightAudioPlayer.stop()
        rightAudioPlayer.release()
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.actionMasked){
            MotionEvent.ACTION_DOWN -> {
                if (Ti != 0L && Ts == 0L) {
                    Ts = System.currentTimeMillis() - tmpt
                    println("三Ts时间为" + Ts)
                    Log.d("手指", "onTouchEvent: ts成功开始")
                    lockdown = 0
                }
                Log.d("手指", "onTouchEvent: 落下但Ts未开始")
                tmpt = System.currentTimeMillis()
                flag = 0
                lockdown = 1


            }
            MotionEvent.ACTION_MOVE->{
                flag =0
            }
            MotionEvent.ACTION_UP->{
                if(To !=0L&& Ts ==0L){
                    Ti =System.currentTimeMillis()- tmpt
                    println("二Ti时间为"+ Ti)
                    flag =1
                    println("finger up")
                }
                if(Ti ==0L) {
                    To = System.currentTimeMillis() - tmpt
                    tmpt = System.currentTimeMillis()
                    println("一To时间为" + To)
                    flag =1
                }

            }
        }
        return super.onTouchEvent(event)
    }

}