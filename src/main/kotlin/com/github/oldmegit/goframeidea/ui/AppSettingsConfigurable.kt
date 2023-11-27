package com.github.oldmegit.goframeidea.ui

import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class AppSettingsConfigurable : Configurable {
    private var appSettingsComponent: AppSettingsComponent? = null

    override fun getDisplayName(): String {
        return "GoFrame Idea"
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return preferredFocusedComponent
    }

    override fun createComponent(): JComponent {
        appSettingsComponent = AppSettingsComponent()
        return appSettingsComponent!!.panel
    }

    override fun isModified(): Boolean {
        val settings = AppSettingsState.getInstance()
        var modified = appSettingsComponent!!.gfApiDir != settings.gfApiDir
//        modified = modified or (appSettingsComponent!!.ideaUserStatus != settings.ideaStatus)
        return modified
    }

    override fun apply() {
        val settings = AppSettingsState.getInstance()
        settings.gfApiDir = appSettingsComponent!!.gfApiDir!!
    }

    override fun reset() {
        val settings = AppSettingsState.getInstance()
        appSettingsComponent!!.gfApiDir = settings.gfApiDir
    }

    override fun disposeUIResources() {
        appSettingsComponent = null
    }
}