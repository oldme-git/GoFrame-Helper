package com.github.oldmegit.goframehelper.apiTagValueUtil

import com.github.oldmegit.goframehelper.data.GfValidationBundle

object Validation : TagValue {
    override val list: Map<String, String>
        get() = mapOf(
            "bail" to getTail("bail"),
            "ci" to getTail("ci"),
            "foreach" to getTail("foreach"),
            "required" to getTail("required"),
            "required-if" to getTail("required-if"),
            "required-unless" to getTail("required-unless"),
            "required-with" to getTail("required-with"),
            "required-with-all" to getTail("required-with-all"),
            "required-without" to getTail("required-without"),
            "required-without-all" to getTail("required-without-all"),
            "date" to getTail("date"),
            "datetime" to getTail("datetime"),
            "date-format" to getTail("date-format"),
            "before" to getTail("before"),
            "before-equal" to getTail("before-equal"),
            "after" to getTail("after"),
            "after-equal" to getTail("after-equal"),
            "array" to getTail("array"),
            "enums" to getTail("enums"),
            "email" to getTail("email"),
            "phone" to getTail("phone"),
            "phone-loose" to getTail("phone-loose"),
            "telephone" to getTail("telephone"),
            "passport" to getTail("passport"),
            "password" to getTail("password"),
            "password2" to getTail("password2"),
            "password3" to getTail("password3"),
            "postcode" to getTail("postcode"),
            "resident-id" to getTail("resident-id"),
            "bank-card" to getTail("bank-card"),
            "qq" to getTail("qq"),
            "ip" to getTail("ip"),
            "ipv4" to getTail("ipv4"),
            "ipv6" to getTail("ipv6"),
            "mac" to getTail("mac"),
            "url" to getTail("url"),
            "domain" to getTail("domain"),
            "size" to getTail("size"),
            "length" to getTail("length"),
            "min-length" to getTail("min-length"),
            "max-length" to getTail("max-length"),
            "between" to getTail("between"),
            "min" to getTail("min"),
            "json" to getTail("json"),
            "integer" to getTail("integer"),
            "float" to getTail("float"),
            "boolean" to getTail("boolean"),
            "same" to getTail("same"),
            "different" to getTail("different"),
            "eq" to getTail("eq"),
            "not-eq" to getTail("not-eq"),
            "gt" to getTail("gt"),
            "gte" to getTail("gte"),
            "lt" to getTail("lt"),
            "lte" to getTail("lte"),
            "in" to getTail("in"),
            "not-in" to getTail("not-in"),
            "regex" to getTail("regex"),
            "not-regex" to getTail("not-regex"),
        )

    override val separator: String
        get() = "|"

    private fun getTail(key: String): String {
        return GfValidationBundle.getMessage(key)
    }
}