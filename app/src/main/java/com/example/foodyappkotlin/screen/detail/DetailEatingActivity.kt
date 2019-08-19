package com.example.foodyappkotlin.screen.detail

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.common.BaseActivity
import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.data.models.ThucDon
import com.example.foodyappkotlin.data.repository.FoodyRepository
import com.example.foodyappkotlin.data.response.ThucDonResponse
import com.example.foodyappkotlin.di.scope.ActivityScoped
import com.example.foodyappkotlin.screen.detail.fragment_overview.OverviewFragment
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.app_toolbar.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@ActivityScoped
class DetailEatingActivity : BaseActivity(),
    DetailEatingInterface.View {
    lateinit var quanAn: QuanAn
    lateinit var presenter: DetailEatingInterface.Presenter
    lateinit var inputParser: SimpleDateFormat
    lateinit var detailViewModel: DetailViewModel

    @Inject
    lateinit var foodyRepository: FoodyRepository

    companion object {
        private const val EXTRA_QUAN_AN = "EXTRA_QUAN_AN"
        private const val INPUT_FORMAT = "HH:mm"
        fun newInstance(context: Context, quanAn: QuanAn): Intent {
            return Intent(context, DetailEatingActivity::class.java).apply {
                putExtra(EXTRA_QUAN_AN, quanAn)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_menu_acitivity)
        AndroidInjection.inject(this)
        initData()
    }

    private fun initData() {
        inputParser = SimpleDateFormat(INPUT_FORMAT, Locale.ENGLISH)
        intent?.let {
            quanAn = it.getSerializableExtra(EXTRA_QUAN_AN) as QuanAn
            quanAn?.let {
                presenter =
                    DetailEatingPresenter(
                        quanAn.thucdon,
                        foodyRepository,
                        this
                    )
            }
        }
        detailViewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        detailViewModel.setQuanan(quanAn)
        pushFragmentWithoutBackStack(R.id.layout_food_detail, OverviewFragment.newInstance())
    }

    override fun thucDonsSuccess(thucdon: MutableList<ThucDonResponse>) {
//        this.pushFragment(R.id.layout_food_detail, OverviewFragment.newInstance())
    }

    @SuppressLint("ShowToast")
    override fun thucDonsFailure(message: String) {
        //show
//        Toast.makeText(this, message, Toast.LENGTH_SHORT)
    }

    fun showActionBack(listerner : View.OnClickListener){
        actionbarBack.visibility = View.VISIBLE
        actionbarBack.setOnClickListener(listerner)
    }
}
