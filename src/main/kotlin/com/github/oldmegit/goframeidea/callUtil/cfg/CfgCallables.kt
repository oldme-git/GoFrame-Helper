package com.github.oldmegit.goframeidea.callUtil.cfg

import com.goide.inspections.core.GoCallableDescriptorSet
import com.goide.inspections.core.GoMethodDescriptor

val CfgCallables = GoCallableDescriptorSet(
    setOf(
        // https://github.com/gogf/gf/blob/master/os/gcfg/gcfg.go
        GoMethodDescriptor.of("(*github.com/gogf/gf/os/gcfg.Config).Get"),
    )
)
