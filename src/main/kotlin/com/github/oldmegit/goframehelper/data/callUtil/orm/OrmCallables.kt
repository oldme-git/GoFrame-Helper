package com.github.oldmegit.goframehelper.data.callUtil.orm

import com.goide.inspections.core.GoCallableDescriptorSet
import com.goide.inspections.core.GoMethodDescriptor

val OrmCallables = GoCallableDescriptorSet(
    setOf(
        // https://github.com/gogf/gf/blob/master/database/gdb/gdb_model_order_group.go
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).Order"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).OrderAsc"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).OrderDesc"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).OrderRandom"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).Group"),

        // https://github.com/gogf/gf/blob/master/database/gdb/gdb_model_whereor.go
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).WhereOr"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).WhereOrf"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).WhereOrLT"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).WhereOrLTE"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).WhereOrGT"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).WhereOrGTE"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).WhereOrBetween"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).WhereOrLike"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).WhereOrIn"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).WhereOrNull"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).WhereOrNotBetween"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).WhereOrNotLike"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).WhereOrNot"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).WhereOrNotIn"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).WhereOrNotNull"),

        // https://github.com/gogf/gf/blob/master/database/gdb/gdb_model_join.go
        // GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).LeftJoin"),
        // GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).RightJoin"),
        // GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).InnerJoin"),

        // https://github.com/gogf/gf/blob/master/database/gdb/gdb_model_update.go
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).Update"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).UpdateAndGetAffected"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).Increment"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).Decrement"),

        // https://github.com/gogf/gf/blob/master/database/gdb/gdb_model_where.go
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).Where"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).Wheref"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).WherePri"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).WhereLT"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).WhereLTE"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).WhereGT"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).WhereGTE"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).WhereBetween"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).WhereLike"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).WhereIn"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).WhereNull"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).WhereNotBetween"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).WhereNotLike"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).WhereNot"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).WhereNotIn"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).WhereNotNull"),

        // https://github.com/gogf/gf/blob/master/database/gdb/gdb_model_select.go
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).All"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).One"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).Array"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).Value"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).Count"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).CountColumn"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).Min"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).Max"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).Avg"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).Sum"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).Having"),

        // https://github.com/gogf/gf/blob/master/database/gdb/gdb_model_insert.go
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).Data"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).OnConflict"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).OnDuplicate"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).OnDuplicateEx"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).Insert"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).InsertAndGetId"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).InsertIgnore"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).Replace"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).Save"),

        // https://github.com/gogf/gf/blob/master/database/gdb/gdb_model_fields.go
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).Fields"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).FieldsPrefix"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).FieldsEx"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).FieldsExPrefix"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).FieldCount"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).FieldSum"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).FieldMin"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).FieldMax"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).FieldAvg"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).GetFieldsStr"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).GetFieldsExStr"),
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).HasField"),

        // https://github.com/gogf/gf/blob/master/database/gdb/gdb_model_delete.go
        GoMethodDescriptor.of("(*github.com/gogf/gf/database/gdb.Model).Delete"),
    )
)
