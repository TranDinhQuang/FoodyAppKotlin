package com.example.foodyappkotlin.screen.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.common.BaseActivity

class DetailEating : BaseActivity() {
    companion object {
        public fun newInstance(context: Context): Intent {
            val intent = Intent(context, DetailEating::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_detail_eating)
    }
}
