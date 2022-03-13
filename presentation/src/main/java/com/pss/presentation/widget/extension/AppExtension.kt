package com.pss.presentation.widget.extension

import android.content.Context
import android.util.Patterns
import android.view.View
import android.widget.ImageView

fun View.show(){
    this.visibility = View.VISIBLE
}
fun View.hide() {
    this.visibility = View.INVISIBLE
}
fun View.makeGone() {
    this.visibility = View.GONE
}

//fun ImageView.glideLoad(url: String) {
//    Glide.with(this.context).load(url).into(this)
//}

//null 체크
fun String?.valid() : Boolean = this != null && !this.equals("null", true) && this.trim().isNotEmpty()

//email 검사
fun String.isValidEmail(): Boolean = this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

