package com.incwelltechnology.lms.ui.noticeCreation

import android.os.Bundle
import androidx.lifecycle.Observer
import com.incwelltechnology.lms.R
import com.incwelltechnology.lms.databinding.ActivityCreateNoticeBinding
import com.incwelltechnology.lms.ui.BaseActivity
import com.incwelltechnology.lms.util.hide
import com.incwelltechnology.lms.util.hideErrorHint
import com.incwelltechnology.lms.util.show
import com.incwelltechnology.lms.util.snack
import kotlinx.android.synthetic.main.activity_create_notice.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateNoticeActivity : BaseActivity<ActivityCreateNoticeBinding>() {
    private val noticeViewModel: NoticeViewModel by viewModel()
    override fun getLayout(): Int {
        return R.layout.activity_create_notice
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //custom toolbar
        setSupportActionBar(custom_toolbar)
        custom_toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        custom_toolbar.setNavigationOnClickListener {
            finish()
        }

        fab_publish_notice.setOnClickListener {
            val mTopic = topic.text.toString()
            val mNotice = notice.text.toString()

            when {
                mTopic.isEmpty() -> {
                    til_topic.error =" Topic Field is Empty"
                    til_topic.requestFocus()
                    return@setOnClickListener
                }
                mNotice.isEmpty() -> {
                    til_notice.error ="Notice Field is Empty"
                    til_notice.requestFocus()
                    return@setOnClickListener
                }
                else -> {
                    noticeViewModel.getNotice(mTopic,mNotice)
                    notice_circular_progress.show()
                    noticeViewModel.postNotice()
                    noticeViewModel.noticeResponse.observe(this, Observer {
                        notice_circular_progress.hide()
                        fab_publish_notice.snack(it)
                        topic.text?.clear()
                        notice.text?.clear()
                    })
                }
            }
        }
        hideErrorHint(topic,til_topic)
        hideErrorHint(notice,til_notice)
    }
}
