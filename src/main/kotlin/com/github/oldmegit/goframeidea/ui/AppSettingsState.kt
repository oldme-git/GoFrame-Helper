package com.github.oldmegit.goframeidea.ui

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

@Service(Service.Level.PROJECT)
@State(
    name = "com.github.oldmegit.goframeidea.ui.AppSettingsState",
    storages = [Storage("GoFrameIdeaSettingsPlugin.xml")]
)
internal class AppSettingsState : PersistentStateComponent<AppSettingsState> {
    var gfApiDir = "api"
    var gfLogicDir = "internal/logic"
    var gfEnableApiWatch = true
    var gfEnableLogicWatch = true

    override fun getState(): AppSettingsState {
        return this
    }

    override fun loadState(state: AppSettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance(project: Project): AppSettingsState {
            return project.getService(AppSettingsState::class.java)
        }
    }
}