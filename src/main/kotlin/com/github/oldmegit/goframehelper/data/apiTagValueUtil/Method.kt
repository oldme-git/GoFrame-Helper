package com.github.oldmegit.goframehelper.data.apiTagValueUtil

object Method : TagValue {
    override val list: Map<String, String>
        get() = mapOf(
            "get" to "",
            "put" to "",
            "post" to "",
            "delete" to "",
            "patch" to "",
            "head" to "",
            "connect" to "",
            "options" to "",
            "trace" to "",
            "all" to ""
        )
    override val separator: String
        get() = ","
}