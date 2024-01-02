package com.github.oldmegit.goframehelper.callUtil.i18n

import com.goide.inspections.core.GoCallableDescriptorSet
import com.goide.inspections.core.GoMethodDescriptor

val I18nCallables = GoCallableDescriptorSet(
    setOf(
        // https://github.com/gogf/gf/blob/master/i18n/gi18n/gi18n_manager.go
        GoMethodDescriptor.of("(*github.com/gogf/gf/i18n/gi18n.Manager).T"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/i18n/gi18n.Manager).Tf"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/i18n/gi18n.Manager).Translate"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/i18n/gi18n.Manager).TranslateFormat"),
    )
)
