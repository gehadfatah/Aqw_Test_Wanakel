package com.example.weanaklie.domain.common

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.android.friendycar.domain.common.formatTo

import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.weanaklie.R


object CustomBindingAdapter {
    @JvmStatic
    @BindingAdapter("typeFace")
    fun setTypeFace(view: View?, type: String) {
        if (view != null && !TextUtils.isEmpty(type)) {
            (view as TextView).typeface = Typeface.createFromAsset(view.context.assets, type)
        }
    }

    /*  @BindingAdapter("progres")
      @JvmStatic
      fun setImage(view: TextView?, cause: Cause?) {
          cause?.let {
             // CausesApplication.applicationContext().getString(R.string.progress_format)
           //  view?.text= "${((cause.collectedAmount.toFloat()) / (cause.targetAmount.toFloat())).times(100)} %"
              view?.text= String.format("%.1f", ((cause.collectedAmount.toFloat()) / (cause.targetAmount.toFloat())).times(100))+" %"


          }

      }*/


    @BindingAdapter("price")
    @JvmStatic
    fun setPrice(view: TextView?, price: String) {
        view?.text = price.toFloat().formatTo(0)


    }


    @BindingAdapter("image")
    @JvmStatic
    fun setImage(view: ImageView?, url: String?) {
        if (view != null && !TextUtils.isEmpty(url)) {
            Glide.with(view.context).load(url)

                .listener(object : RequestListener<Drawable?> {


                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any,
                        target: com.bumptech.glide.request.target.Target<Drawable?>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        //on load success
                        Log.d(
                            "TAG",
                            " CircleImageView: success "
                        )
                        return false
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable?>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        //on load failed
                        Log.d(
                            "TAG",
                            " CircleImageView: failed"
                        )
                        return false
                    }
                })
                .into(view)
        }
    }


}