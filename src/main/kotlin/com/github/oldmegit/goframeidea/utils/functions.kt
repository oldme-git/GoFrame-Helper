package com.github.oldmegit.goframeidea.utils

import java.io.File
import java.nio.file.Paths

fun isApi(base: String, file: String): Boolean {
    val basePath = Paths.get(base)
    val filePath = Paths.get(file)
    var absolute = basePath.relativize(filePath).toString()
    if (File.separator == "\\") {
        absolute = absolute.replace("\\", "/")
    }
    val absolutePaths = absolute.split("/")
    return absolutePaths[0] == "api"
}