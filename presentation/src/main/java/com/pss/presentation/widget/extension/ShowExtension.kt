package com.pss.presentation.widget.extension

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.pss.presentation.R

// Show alert dialog
/*fun Context.showAlertDialog(positiveButtonLable : String = getString(R.string.okay),
                            title : String = getString(R.string.app_name), message : String,
                            actionOnPositveButton : () -> Unit) {
    val builder = AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setCancelable(false)
        .setPositiveButton(positiveButtonLable) { dialog, id ->
            dialog.cancel()
            actionOnPositveButton()
        }
    val alert = builder.create()
    alert?.show()
}*/

// Toash extensions
fun Context.showShotToast(message : String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showLongToast(message : String){
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

// Snackbar Extensions
fun View.showShotSnackbar(message : String){
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

fun View.showLongSnackbar(message : String){
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}

fun View.snackBarWithAction(message : String, actionlable : String,
                            block : () -> Unit){
    Snackbar.make(this, message, Snackbar.LENGTH_LONG)
        .setAction(actionlable) {
            block()
        }
}

//use snackBarWithAction
/*
snackBarWithAction(mes,title){
//TODO do on action
}*/
