package com.github.oldmegit.goframeidea.startup

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

@Service(Service.Level.PROJECT)
@State(
    name = "com.github.oldmegit.goframeidea.ui.GoFrameIdeaOrm",
    storages = [Storage("GoFrameIdeaOrm.xml")]
)
internal class OrmState : PersistentStateComponent<OrmState> {
    var a: String? = null

    override fun getState(): OrmState {
        return this
    }

    override fun loadState(state: OrmState) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance(project: Project): OrmState {
            return project.getService(OrmState::class.java)
        }
    }
}