package com.github.oldmegit.goframehelper.data.callUtil.i18n

import com.github.oldmegit.goframehelper.data.callUtil.CallUtil
import com.github.oldmegit.goframehelper.data.callUtil.i18n.types.Yaml
import com.github.oldmegit.goframehelper.ui.AppSettingsState
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import java.io.File

object I18nUtil : CallUtil() {
    private val i18nTypes = mapOf(
        "yaml" to Yaml,
        "yml" to Yaml,
    )

    override fun getData(psiElement: PsiElement): Map<String, Set<PsiElement>> {
        val project = psiElement.project
        return try {
            val fileMaps = getI18nFilesPath(project)
            getKeyValue(fileMaps)
        } catch (_: Exception) {
            hashMapOf()
        }
    }

    override fun getPsiTail(psiElement: PsiElement?): String {
        return ""
    }

    // get key and value in all file
    private fun getKeyValue(files: Map<PsiElement, String>): Map<String, Set<PsiElement>> {
        var data = mapOf<String, Set<PsiElement>>()

        for ((file, extension) in files) {
            val fileMap = i18nTypes[extension]!!.getFileKeyValue(file)
            data = data.merge(fileMap)
        }
        return data
    }

    // get gf i18n folder
    private fun getI18nFoldersPath(project: Project): Set<String> {
        val settings = AppSettingsState.getInstance(project)
        val custom = settings.gfCustomI18nFolder
        return setOf(
            "${project.basePath}/manifest/i18n",
            "${project.basePath}/i18n",
            project.basePath + "/" + custom
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
            if (file.isDirectory) {
                map += scanFolder(project, file.path)
                return@forEach
            }
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
