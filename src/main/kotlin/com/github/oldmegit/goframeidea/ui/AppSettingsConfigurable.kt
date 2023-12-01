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
        modified = modified or (appSettingsComponent!!.gfLogicDir != settings.gfLogicDir)
        modified = modified or (appSettingsComponent!!.gfEnableApiWatch != settings.gfEnableApiWatch)
        modified = modified or (appSettingsComponent!!.gfEnableLogicWatch != settings.gfEnableLogicWatch)
        return modified
    }

    override fun apply() {
        val settings = AppSettingsState.getInstance()
        settings.gfApiDir = appSettingsComponent!!.gfApiDir!!
        settings.gfLogicDir = appSettingsComponent!!.gfLogicDir!!
        settings.gfEnableApiWatch = appSettingsComponent!!.gfEnableApiWatch!!
        settings.gfEnableLogicWatch = appSettingsComponent!!.gfEnableLogicWatch!!
    }

    override fun reset() {
        val settings = AppSettingsState.getInstance()
        appSettingsComponent!!.gfApiDir = settings.gfApiDir
        appSettingsComponent!!.gfLogicDir = settings.gfLogicDir
        appSettingsComponent!!.gfEnableApiWatch = settings.gfEnableApiWatch
        appSettingsComponent!!.gfEnableLogicWatch = settings.gfEnableLogicWatch
    }

    override fun disposeUIResources() {
        appSettingsComponent = null
    }
}