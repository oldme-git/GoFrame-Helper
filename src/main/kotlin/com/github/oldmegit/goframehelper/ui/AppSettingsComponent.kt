package com.github.oldmegit.goframehelper.ui

import com.github.oldmegit.goframehelper.data.Bundle
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JPanel

class AppSettingsComponent {
    val panel: JPanel
    private val apiDir = JBTextField()
    private val logicDir = JBTextField()
    private val customGfCli = JBTextField()
    private val enableApiWatch = JBCheckBox(Bundle.getMessage("setting.api.watch"))
    private val enableLogicWatch = JBCheckBox(Bundle.getMessage("setting.service.watch"))

    init {
        panel = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel(Bundle.getMessage("setting.api.src")), apiDir, 1, false)
            .addLabeledComponent(JBLabel(Bundle.getMessage("setting.logic.src")), logicDir, 1, false)
            .addLabeledComponent(JBLabel(Bundle.getMessage("setting.custom.gfCli")), customGfCli, 1, false)
            .addComponent(enableApiWatch)
            .addComponent(enableLogicWatch)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    var gfApiDir: String?
        get() = apiDir.getText()
        set(newText) {
            apiDir.text = newText
        }

    var gfLogicDir: String?
        get() = logicDir.getText()
        set(newText) {
            logicDir.text = newText
        }

    var gfCustomGfCli: String?
        get() = customGfCli.getText()
        set(newText) {
            customGfCli.text = newText
        }

    var gfEnableApiWatch: Boolean?
        get() = enableApiWatch.isSelected
        set(newStatus) {
            enableApiWatch.setSelected(newStatus as Boolean)
        }

    var gfEnableLogicWatch: Boolean?
        get() = enableLogicWatch.isSelected
        set(newStatus) {
            enableLogicWatch.setSelected(newStatus as Boolean)
        }
}