package com.github.oldmegit.goframeidea.ui

import com.github.oldmegit.goframeidea.data.Bundle
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import javax.swing.JComponent

class AppSettingsConfigurable(private val project: Project) : Configurable {
    private var appSettingsComponent: AppSettingsComponent? = null

    override fun getDisplayName(): String {
        return Bundle.getMessage("name")
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return preferredFocusedComponent
    }

    override fun createComponent(): JComponent {
        appSettingsComponent = AppSettingsComponent()
        return appSettingsComponent!!.panel
    }

    override fun isModified(): Boolean {
        val settings = AppSettingsState.getInstance(project)
        var modified = appSettingsComponent!!.gfApiDir != settings.gfApiDir
        modified = modified or (appSettingsComponent!!.gfLogicDir != settings.gfLogicDir)
        modified = modified or (appSettingsComponent!!.gfEnableApiWatch != settings.gfEnableApiWatch)
        modified = modified or (appSettingsComponent!!.gfEnableLogicWatch != settings.gfEnableLogicWatch)
        return modified
    }

    override fun apply() {
        val settings = AppSettingsState.getInstance(project)
        settings.gfApiDir = appSettingsComponent!!.gfApiDir!!
        settings.gfLogicDir = appSettingsComponent!!.gfLogicDir!!
        settings.gfEnableApiWatch = appSettingsComponent!!.gfEnableApiWatch!!
        settings.gfEnableLogicWatch = appSettingsComponent!!.gfEnableLogicWatch!!
    }

    override fun reset() {
        val settings = AppSettingsState.getInstance(project)
        appSettingsComponent!!.gfApiDir = settings.gfApiDir
        appSettingsComponent!!.gfLogicDir = settings.gfLogicDir
        appSettingsComponent!!.gfEnableApiWatch = settings.gfEnableApiWatch
        appSettingsComponent!!.gfEnableLogicWatch = settings.gfEnableLogicWatch
    }

    override fun disposeUIResources() {
        appSettingsComponent = null
    }
}