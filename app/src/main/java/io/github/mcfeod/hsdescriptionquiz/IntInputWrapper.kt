package io.github.mcfeod.hsdescriptionquiz

import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.widget.EditText
import java.lang.NumberFormatException

class IntInputWrapper(private val widget: EditText) {
    fun setup(value: Int, default: Int, range: IntRange, onChange: (Int) -> Unit) {
        widget.setText(value.toString())
        widget.hint = default.toString()
        widget.filters = arrayOf(RangeInputFilter(range))
        widget.addTextChangedListener(IntInputListener(default, onChange))
    }

    class RangeInputFilter(private val range: IntRange) : InputFilter {
        override fun filter(
            source: CharSequence?,
            start: Int,
            end: Int,
            dest: Spanned?,
            dstart: Int,
            dend: Int
        ): CharSequence? = try {
            val inputValue = Integer.parseInt(dest?.toString() + source?.toString())
            if (range.contains(inputValue)) null else ""
        } catch (e: NumberFormatException) {
            ""
        }
    }

    class IntInputListener(private val default: Int, private val onChange: (Int) -> Unit): TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            try {
                val inputValue = Integer.parseInt(s.toString())
                onChange(inputValue)
            } catch (e: NumberFormatException) {
                onChange(default)
            }
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
}