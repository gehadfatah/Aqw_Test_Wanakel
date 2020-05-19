package com.example.weanaklie.presentation.main.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weanaklie.R
import kotlinx.android.synthetic.main.fragment_about.*

class AboutFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListenerViews()
    }

    fun openBrowser(link: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(browserIntent)
    }
    fun clickTwitShare(string: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/intent/tweet?text=$string"))
        startActivity(browserIntent)
    }
    private fun setListenerViews() {

        linkWebsite.setOnClickListener {
            openBrowser("https://www.wainnakel.com/")

        }
        linkTwit.setOnClickListener {
            openBrowser("https://mobile.twitter.com/Wainnakel")
        }
    }
}