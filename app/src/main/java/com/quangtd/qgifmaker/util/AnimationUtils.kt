package com.quangtd.qgifmaker.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import com.androidadvance.topsnackbar.AnimationUtils

/**
 * Created by QuangTD
 * on 1/18/2018.
 */
class AnimationUtils {

    companion object {
        // slide the view from below itself to the current position
        fun showViewUp(view: View?) {
            view?.let {
                it.visibility = View.VISIBLE
                val animate = TranslateAnimation(0f, 0f, it.height.toFloat(), 0f)
                animate.duration = 250
                animate.setAnimationListener(object : AnimationUtils.AnimationListenerAdapter() {
                    override fun onAnimationEnd(p0: Animation?) {
                        it.visibility = View.VISIBLE
                    }
                })
                it.startAnimation(animate)
            }

        }

        // slide the view from its current position to below itself
        fun hideViewDown(view: View?) {
            view?.let {
                it.visibility = View.VISIBLE
                val animate = TranslateAnimation(0f, 0f, 0f, it.height.toFloat())
                animate.duration = 250
                animate.setAnimationListener(object : AnimationUtils.AnimationListenerAdapter() {
                    override fun onAnimationEnd(p0: Animation?) {
                        it.visibility = View.GONE
                    }
                })
                it.startAnimation(animate)
            }

        }

        fun showViewFadeIn(view: View?) {
            view?.let {
                it.apply {
                    visibility = View.VISIBLE
                    clearAnimation()
                    animate().setDuration(300).alpha(1f).setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            visibility = View.VISIBLE
                        }
                    })
                }
            }


        }

        fun hideViewFadeOut(view: View?) {
            view?.let {
                it.apply {
                    visibility = View.VISIBLE
                    clearAnimation()
                    animate().alpha(0f).setDuration(300)
                            .setListener(object : AnimatorListenerAdapter() {
                                override fun onAnimationEnd(animation: Animator?) {
                                    super.onAnimationEnd(animation)
                                    visibility = View.GONE
                                }
                            })
                }
            }

        }
    }
}