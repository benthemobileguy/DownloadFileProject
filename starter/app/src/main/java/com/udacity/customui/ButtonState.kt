package com.udacity.customui

import com.udacity.R

sealed class ButtonState(var buttonText: Int) {
    object Default : ButtonState(R.string.customButton_text_default)
    object Loading : ButtonState(R.string.customButton_text_loading)
    object Completed : ButtonState(R.string.customButton_text_completed)
}