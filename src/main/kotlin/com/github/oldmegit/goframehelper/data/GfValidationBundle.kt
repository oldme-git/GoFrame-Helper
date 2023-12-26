package com.github.oldmegit.goframehelper.data

import com.intellij.DynamicBundle
import java.util.function.Supplier

object GfValidationBundle : DynamicBundle("message.GfValidationBundle") {
    override fun getMessage(key: String, vararg params: Any?): String {
        return super.getMessage(key, *params)
    }

    override fun getLazyMessage(key: String, vararg params: Any?): Supplier<String> {
        return super.getLazyMessage(key, *params)
    }
}