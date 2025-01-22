package com.example.ratingrequestandroid.rating

import android.content.Context
import android.util.Log
import com.example.ratingrequestandroid.util.getActivity
import com.google.android.play.core.review.ReviewManagerFactory

fun Context.launchInAppReview(
    onComplete: ((Boolean) -> Unit)? = null,
) {
    val activity = this.getActivity()
    if (activity != null) {
        val reviewManager = ReviewManagerFactory.create(activity)
        val request = reviewManager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                val flow = reviewManager.launchReviewFlow(activity, reviewInfo)
                flow.addOnCompleteListener {
                    // The flow has finished. The API doesn't indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown.
                    // Therefore, no matter the result, continue with your app's flow.
                    Log.e("App Review process:", "Finished")
                    onComplete?.invoke(true)
                }
            } else {
                // Log or handle error if you want to
                Log.e("App Review process:", "Error-${task.exception?.message}")
                onComplete?.invoke(false)
            }
        }
    } else {
        Log.e("App Review process:", "Failed: ActivityNotFound")
        onComplete?.invoke(false)
    }

}