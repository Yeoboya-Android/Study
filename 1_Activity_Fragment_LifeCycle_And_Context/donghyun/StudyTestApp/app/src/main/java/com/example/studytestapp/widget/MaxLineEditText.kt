package com.example.studytestapp.widget
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatEditText
import com.example.studytestapp.R

open class MaxLineEditText : AppCompatEditText {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { init(attrs) }

    private var mImm: InputMethodManager? = null

    @DrawableRes var focusBackgroundRes: Int = R.drawable.bg_none
    @DrawableRes var defaultBackground: Int = R.drawable.bg_none
    private var onTextTrim: Boolean = false

    var maxLineCount: Int = 0 // 0 : 제한 없음
    var maxTextLength: Int = 0

    var focusClear: Boolean = false
        set(value) {
            if (value) clearFocusAndHideKeyboard()
            field = value
        }

    protected var beforeInput: String = ""


    @SuppressLint("Recycle", "CustomViewStyleable")
    private fun init(attrs: AttributeSet?) {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.maxTextEditTextAttr)
        setOption(typeArray)

        initView(mFocusListener)
    }

    open fun setOption(attrs: TypedArray) {
        maxLineCount = attrs.getInt(R.styleable.maxTextEditTextAttr_maxLineCount, 0)
        maxTextLength = attrs.getInt(R.styleable.maxTextEditTextAttr_maxLineCount, 0)
        onTextTrim = attrs.getBoolean(R.styleable.maxTextEditTextAttr_onTextTrim, false)
        focusClear = attrs.getBoolean(R.styleable.maxTextEditTextAttr_focusClear, false)
        focusBackgroundRes = attrs.getResourceId(R.styleable.maxTextEditTextAttr_focusBackground, focusBackgroundRes)
        defaultBackground = attrs.getResourceId(R.styleable.maxTextEditTextAttr_defaultBackground, defaultBackground)
    }

    private fun initView(listener: OnFocusChangeListener) {
        // EditText setting
        onFocusChangeListener = listener
        addTextChangedListener(getTextWatcher())

        changeBackground()
    }

    open fun getTextWatcher(): TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            var input = s.toString()
            var inputLength = StringUtil.getGraphemeLength(input)
            val overTextLength = maxTextLength != 0 && inputLength > maxTextLength
            val overTextLine = maxLineCount != 0 && lineCount > maxLineCount

            if (overTextLength || overTextLine) {
                input = beforeInput
                inputLength = StringUtil.getGraphemeLength(input)
                setText(input)
                cursorGoToEnd()

                if (overTextLength) mChangeTextListener?.onOverTextLimit(this@MaxLineEditText)
            }

            mChangeTextListener?.onTextChange(this@MaxLineEditText, input, inputLength)
            beforeInput = input
        }
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
        changeBackground()
    }

    // 1. focus 상태 2. 입력된 내용이 없는 경우 focusBackgroundRes
    // 그 외의 경우 bg_none
    // 수정 -> focusBackgroundRes 없는 경우, background 건들지 않음.
    private fun changeBackground() {
        val backgroundRes =
            if (isFocused || (!isFocused && text?.isEmpty() != false)) focusBackgroundRes
            else defaultBackground
        setBackgroundResource(backgroundRes)
    }

    // 포커스 해제 및 키보드 비활성화
    private fun clearFocusAndHideKeyboard() {
        if (mImm == null)
            mImm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        clearFocus()
        mImm!!.hideSoftInputFromWindow(windowToken, 0)
    }

    // 커서 위치 마지막으로
    protected fun cursorGoToEnd() {
        setSelection(text?.length ?: 0)
    }


    /** Listener */
    private val mFocusListener = OnFocusChangeListener { v, hasFocus ->
        changeBackground()
        if (!hasFocus && onTextTrim && v is EditText) {
            v.setText(v.text.trim())
        }
        mChangeTextListener?.onFocusChange(v as MaxLineEditText, hasFocus)
    }


    protected var mChangeTextListener: ChangeEditTextListener? = null

    fun setChangeTextListener(listener: ChangeEditTextListener) {
        mChangeTextListener = listener
    }


    /** EditText 변경 리스너 */
    interface ChangeEditTextListener {
        fun onTextChange(view: MaxLineEditText, text: String, textLength: Int)
        fun onOverTextLimit(view: MaxLineEditText)
        fun onFocusChange(view: MaxLineEditText, focus: Boolean)
    }
}