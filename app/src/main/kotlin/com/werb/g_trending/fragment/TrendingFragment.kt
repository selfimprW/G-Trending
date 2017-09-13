package com.werb.g_trending.fragment

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.werb.g_trending.R
import com.werb.g_trending.adapter.TrendingListAdapter
import com.werb.g_trending.api.TrendingRequest
import com.werb.g_trending.model.Repository
import com.werb.g_trending.utils.ResourcesUtils
import kotlinx.android.synthetic.main.fragment_trending.*

/** Created by wanbo <werbhelius@gmail.com> on 2017/9/6. */

class TrendingFragment : LazyLoadFragment() {

    private lateinit var adapter: TrendingListAdapter
    private var language = ""
    private var refresh: SwipeRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        language = arguments.getString(ARG_LANGUAGE)
        adapter = TrendingListAdapter(context)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_trending, container, false).apply {
            refresh = this?.findViewById(R.id.refresh)
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.addItemDecoration(ItemDecoration(context))
        recyclerView.adapter = adapter
        refresh?.setOnRefreshListener {
            requestData()
        }
    }

    override fun requestData() {
        TrendingRequest.repository(language)
                .doOnSubscribe {
                    refresh?.isRefreshing = true
                }
                .subscribe({
                    adapter.clear()
                    adapter.addItem(it)
                }, { it.printStackTrace() }, {
                    refresh?.isRefreshing = false
                })
    }

    companion object {
        private const val ARG_LANGUAGE = "LANGUAGE"

        fun newInstance(name: String): TrendingFragment {
            return TrendingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_LANGUAGE, name)
                }
            }
        }
    }

    private inner class ItemDecoration(private val context: Context) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect?.top = ResourcesUtils.dp2px(context, 3f)
        }
    }

}