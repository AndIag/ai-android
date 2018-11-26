package com.andiag.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import java.util.*

abstract class BaseTabsFragment<P : BasePresenter> : BaseFragment<P>() {

    lateinit var mViewPager: ViewPager
    lateinit var mTabLayout: TabLayout

    private var mSelectedTab = 0

    abstract val tabs: List<BaseTab>

    override val layout: Int
        get() = R.layout.fragment_tabs

    private fun setupTabs() {
        val adapter = TabsAdapter(childFragmentManager)
        val tabs = tabs
        for (tab in tabs) {
            adapter.addTab(tab)
        }
        mViewPager.adapter = adapter
        mTabLayout.setupWithViewPager(mViewPager)
        setupTabLayout(tabs)
    }

    private fun setupTabLayout(tabs: List<BaseTab>) {
        for (i in 0 until mTabLayout.tabCount) {
            mTabLayout.getTabAt(i)!!.setIcon(tabs[i].icon)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SELECTED_TAB, mSelectedTab)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_TAB)) {
            mSelectedTab = savedInstanceState.getInt(SELECTED_TAB)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        mViewPager = view!!.findViewById(R.id.viewPager)
        mTabLayout = view.findViewById(R.id.tabLayout)

        setupTabs()

        return view
    }

    override fun onResume() {
        super.onResume()
        mViewPager.currentItem = mSelectedTab
    }

    override fun onStart() {
        super.onStart()

        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                mSelectedTab = position
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    override fun onStop() {
        super.onStop()
        mViewPager.clearOnPageChangeListeners()
    }

    internal class TabsAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private val mFragments = ArrayList<Fragment>()
        private val mFragmentTitles = ArrayList<String>()

        fun addTab(tab: BaseTab) {
            mFragments.add(tab.fragment)
            mFragmentTitles.add(tab.title)
        }

        override fun getItem(position: Int): Fragment {
            return mFragments[position]
        }

        override fun getCount(): Int {
            return mFragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            val pageTitle = mFragmentTitles[position]
            return if (pageTitle == "") pageTitle else null
        }
    }

    companion object {
        private val SELECTED_TAB = "SELECTED_TAB"
    }
}
