package chat.rocket.android.helper

import android.view.View
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import chat.rocket.android.R

object ToastHelper {
    fun showCustomToast(context: Context, toastText: String, view: View) {
        with (view) {
            val layout = LayoutInflater.from(context).inflate(R.layout.custom_toast, findViewById(R.id.custom_toast_container))
            val text = layout.findViewById<TextView>(R.id.text)
            text.setText(toastText)
            val toast =  Toast(context)
            toast.setGravity(Gravity.FILL_HORIZONTAL or Gravity.BOTTOM, 0, 0)
            toast.setDuration(Toast.LENGTH_LONG)
            toast.setView(layout)
            toast.show()
        }
    }
}