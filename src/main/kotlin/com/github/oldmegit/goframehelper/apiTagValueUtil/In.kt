package com.github.oldmegit.goframehelper.apiTagValueUtil

object In : TagValue {
    override val list: Map<String, String>
        get() = mapOf(
            "header" to "",
            "path" to "",
            "query" to "",
            "cookie" to "",
        )

    override val separator: String
        get() = ""
}