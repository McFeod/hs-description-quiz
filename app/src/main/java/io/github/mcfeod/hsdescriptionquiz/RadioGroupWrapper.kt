package io.github.mcfeod.hsdescriptionquiz

import android.widget.RadioButton
import android.widget.RadioGroup

class RadioGroupWrapper<T>(private val widget: RadioGroup) {
    private val inputOptions = mutableMapOf<Int, T>()
    private val reverseOptions = mutableMapOf<T, Int>()  // is there bidirectional map in kotlin/java?

    fun add(resourceId: Int, inputValue: T): RadioGroupWrapper<T> {
        inputOptions[resourceId] = inputValue
        reverseOptions[inputValue] = resourceId
        return this
    }

    fun setup(initialValue: T, onChange: (T) -> Unit) {
        val selectedId = reverseOptions[initialValue]
        if (selectedId != null) {
            val selectedButton = widget.findViewById<RadioButton>(selectedId)
            selectedButton.isChecked = true
        }
        widget.setOnCheckedChangeListener {
            _, checkedId -> run {
                val nextInputValue = inputOptions[checkedId]
                if (nextInputValue != null) {
                    onChange(nextInputValue)
                }
            }
        }
    }
}