package com.mukund.bookcompanion.ui.home

import androidx.annotation.StringRes
import com.mukund.bookcompanion.R

enum class BookCategory(
    val icon: Int,
    val statusLabel: String?,
    @StringRes val labelRes: Int,
) {
    All(R.drawable.list_alt, null, R.string.category_all),
    Read(R.drawable.check_circle, "Read", R.string.category_read),
    Unread(R.drawable.check_circle_unread, "Unread", R.string.category_unread),
    Reading(R.drawable.expand_circle_down, "Reading", R.string.category_reading),
}
