package com.jiban.howlong.data.message

data class Message(
    var date: String,
    var message: String,
) {
    constructor() : this("", "")
}