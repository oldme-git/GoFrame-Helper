package com.github.oldmegit.goframeidea.ui

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "com.github.oldmegit.goframeidea.ui.AppSettingsState",
    storages = [Storage("GoFrameIdeaSettingsPlugin.xml")]
)
internal class AppSettingsState : PersistentStateComponent<AppSettingsState> {
    var gfApiDir = "api"
    var gfLogicDir = "internal/logic"

    override fun getState(): AppSettingsState {
        return this
    }

    override fun loadState(state: AppSettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance(): AppSettingsState {
            return ApplicationManager.getApplication().getService(AppSettingsState::class.java)
        }
    }
}