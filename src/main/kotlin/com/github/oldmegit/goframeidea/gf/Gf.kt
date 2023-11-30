package com.github.oldmegit.goframeidea.gf

import com.intellij.openapi.util.IconLoader

class Gf {
    companion object {
        val goModMark = "github.com/gogf/gf/v2"
        val icon = IconLoader.getIcon("/icons/gf16.png", Gf::class.java)
        val openApiTagGMeta = mapOf(
            "path" to "",
            "tags" to "",
            "method" to "GET/PUT/POST/DELETE...",
            "deprecated" to "",
            "mime" to "default:application/json",
        )
        val openApiTagNorMal = mapOf(
            "v" to "validate(only Req)",
        )
        val openApiTag = mapOf(
            "sm" to "summary",
            "summary" to "",
            "dc" to "description",
            "description" to "",
            "d" to "default",
            "default" to "",
            "type" to "",
        )
    }
}