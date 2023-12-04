package com.github.oldmegit.goframeidea.data

import com.intellij.DynamicBundle
import java.util.function.Supplier

object Bundle : DynamicBundle("message.GoFrameIdeaBundle") {
    override fun getMessage(key: String, vararg params: Any?): String {
        return super.getMessage(key, *params)
    }

    override fun getLazyMessage(key: String, vararg params: Any?): Supplier<String> {
        return super.getLazyMessage(key, *params)
    }
}