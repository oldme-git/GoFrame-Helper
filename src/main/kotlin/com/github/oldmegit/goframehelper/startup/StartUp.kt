package com.github.oldmegit.goframehelper.startup

import com.github.oldmegit.goframehelper.data.Bundle
import com.github.oldmegit.goframehelper.gf.GfGoMod
import com.intellij.openapi.progress.BackgroundTaskQueue
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

class StartUp : StartupActivity {
    override fun runActivity(project: Project) {
        val task = BackgroundTaskQueue(project, Bundle.getMessage("name"))
        task.run(object : Task.Backgroundable(project, Bundle.getMessage("init")) {
            override fun run(indicator: ProgressIndicator) {
                GfGoMod.reset(project)
            }
        })
    }
}