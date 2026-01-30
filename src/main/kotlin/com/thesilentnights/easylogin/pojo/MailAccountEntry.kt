package com.thesilentnights.easylogin.pojo


data class MailAccountEntry(
    var username: String?,
    var password: String?,
    var host: String?,
    var port: Int = 0,
    var from: String?,
    var enableSSL: Boolean = false,
    var starttlsEnable: Boolean = false,
    var timeout: Long = 0,
)