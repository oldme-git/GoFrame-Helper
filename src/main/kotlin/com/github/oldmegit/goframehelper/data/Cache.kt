package com.github.oldmegit.goframehelper.data

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

@Service(Service.Level.PROJECT)
@State(
    name = "com.github.oldmegit.goframehelper.ui.goframehelperCache",
    storages = [Storage("goframehelperCache.xml")]
)
internal class Cache : PersistentStateComponent<Cache> {
    var isGf: Boolean? = null

    override fun getState(): Cache {
        return this
    }

    override fun loadState(state: Cache) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance(project: Project): Cache {
            return project.getService(Cache::class.java)
        }
    }
}