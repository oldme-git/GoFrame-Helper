package com.github.oldmegit.goframehelper.apiTagValueUtil

object Method : TagValue {
    override val list: Map<String, String>
        get() = mapOf(
            "get" to "",
            "put" to "",
            "post" to "",
            "patch" to "",
            "delete" to "",
            "options" to "",
            "head" to "",
        )

    override val separator: String
        get() = ","
}