package com.barengsaya.storyapp.view.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.barengsaya.storyapp.R
import com.google.android.material.textfield.TextInputEditText


class PasswordEditText : TextInputEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                error = if (!s.isNullOrEmpty() && s.length < 8) {
                    context.getString(R.string.password_error)
                } else {
                    null
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}
