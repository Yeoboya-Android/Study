package com.example.studytestapp.widget

import java.text.BreakIterator

object StringUtil {

    // 이모지 포함 글자 수 (이모지도 1글자로)
    fun getGraphemeLength(text: String): Int {
        val breakIterator = BreakIterator.getCharacterInstance().apply { setText(text) }

        var count = 0
        while (breakIterator.next() != BreakIterator.DONE)
            count++

        return count
    }
}