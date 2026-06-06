package com.mukund.bookcompanion.ui.home

import com.mukund.bookcompanion.R

enum class BookCategory(val icon: Int, val statusLabel: String?) {
    All(R.drawable.list_alt, null),
    Read(R.drawable.check_circle, "Read"),
    Unread(R.drawable.check_circle_unread, "Unread"),
    Reading(R.drawable.expand_circle_down, "Reading"),
}
