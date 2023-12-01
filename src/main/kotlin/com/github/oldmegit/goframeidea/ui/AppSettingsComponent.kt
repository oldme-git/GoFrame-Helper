package com.github.oldmegit.goframeidea.ui

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JPanel

class AppSettingsComponent {
    val panel: JPanel
    private val apiDir = JBTextField()
    private val logicDir = JBTextField()

    init {
        panel = FormBuilder.createFormBuilder()
                .addLabeledComponent(JBLabel("Api str folder: "), apiDir, 1, false)
                .addLabeledComponent(JBLabel("Logic str folder: "), logicDir, 1, false)
                .addComponentFillVertically(JPanel(), 0)
                .panel
    }

    var gfApiDir: String?
        get() = apiDir.getText()
        set(newText) {
            apiDir.setText(newText)
        }

    var gfLogicDir: String?
        get() = logicDir.getText()
        set(newText) {
            logicDir.setText(newText)
        }
}