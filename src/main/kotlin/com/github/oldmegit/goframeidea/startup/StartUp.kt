package com.github.oldmegit.goframeidea.startup

import com.github.oldmegit.goframeidea.data.Bundle
import com.github.oldmegit.goframeidea.goFrame.GfGoMod
import com.intellij.openapi.progress.BackgroundTaskQueue
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

class StartUp : ProjectActivity {
    override suspend fun execute(project: Project) {
        val task = BackgroundTaskQueue(project, Bundle.getMessage("name"))
        task.run(object : Task.Backgroundable(project, Bundle.getMessage("init")) {
            override fun run(indicator: ProgressIndicator) {
                GfGoMod.reset(project)
            }
        })
    }
}