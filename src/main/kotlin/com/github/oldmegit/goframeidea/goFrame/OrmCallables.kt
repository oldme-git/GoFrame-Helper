package com.github.oldmegit.goframeidea.goFrame

import com.goide.inspections.core.GoCallableDescriptorSet
import com.goide.inspections.core.GoMethodDescriptor

val OrmCallables = GoCallableDescriptorSet(
    setOf(
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).Where"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).Order")
    )
)