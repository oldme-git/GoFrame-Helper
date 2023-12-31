package com.github.oldmegit.goframehelper.callUtil.i18n

import com.github.oldmegit.goframehelper.callUtil.CallUtil
import com.github.oldmegit.goframehelper.callUtil.cfg.types.Json
import com.github.oldmegit.goframehelper.callUtil.cfg.types.Yaml
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import java.io.File

object I18n : CallUtil {
    private val i18nTypes = mapOf(
        "yaml" to Yaml,
        "json" to Json,
    )

    override fun getData(psiElement: PsiElement): Map<String, String> {
        val project = psiElement.project
        val fileMaps = getI18nFilesPath(project)
        println(fileMaps)
        return hashMapOf()
//        return try {
//            I18nUtil.getKeyValue(fileMaps)
//        } catch (_: Exception) {
//            hashMapOf()
//        }
    }

    // get gf i18n folder
    private fun getI18nFoldersPath(project: Project): Set<String> {
        return setOf(
            "${project.basePath}/manifest/i18n",
            "${project.basePath}/i18n",
        )
    }

    private fun getI18nFilesPath(project: Project): Map<PsiElement, String> {
        val map = hashMapOf<PsiElement, String>()
        val folders = getI18nFoldersPath(project)

        for (folder in folders) {
            map += scanFolder(project, folder)
        }
        return map
    }

    // scan all file in folder
    private fun scanFolder(project: Project, folderPath: String): Map<PsiElement, String> {
        val map = HashMap<PsiElement, String>()
        val folder = File(folderPath)
        val files = folder.listFiles()
        var virtualFile: VirtualFile
        var psiFile: PsiElement
        var extension: String

        files?.forEach { file ->
            extension = file.extension.lowercase()
            if (extension in i18nTypes.keys) {
                virtualFile = LocalFileSystem.getInstance().findFileByPath(file.path)!!
                psiFile = PsiManager.getInstance(project).findFile(virtualFile)!!
                map[psiFile] = extension
            }
        }
        return map
    }
}
