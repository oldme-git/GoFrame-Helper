package com.github.oldmegit.goframehelper.apiTagValueUtil

object Mime : TagValue {
    override val list: Map<String, String>
        get() = mapOf(
            "multipart/form-data" to "",
            "application/json" to "",
            "application/x-www-form-urlencoded" to "",
        )

    override val separator: String
        get() = ""
}