package com.github.oldmegit.goframeidea.callUtil.cfg

import com.github.oldmegit.goframeidea.callUtil.CallUtil
import com.github.oldmegit.goframeidea.callUtil.cfg.types.CfgType
import com.github.oldmegit.goframeidea.callUtil.cfg.types.Yaml
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import java.io.File

object CfgUtil : CallUtil {
    private val cfgTypes = mapOf<String, CfgType>(
        "yaml" to Yaml,
    )

    override fun getData(psiElement: PsiElement): Map<String, String> {
        val project = psiElement.project
        val fileMaps = getCfgFilesPath(project)
        return try {
            getKeyValue(fileMaps)
        } catch (_: Exception) {
            hashMapOf()
        }
    }

    // get key and value in all file
    private fun getKeyValue(files: Map<PsiElement, String>): Map<String, String> {
        val map = hashMapOf<String, String>()

        for ((file, extension) in files) {
            map += cfgTypes[extension]!!.getFileKeyValue(file)
        }
        return map
    }

    // get gf config folder
    private fun getCfgFoldersPath(project: Project): Set<String> {
        return setOf(
            "${project.basePath}/manifest/config",
            "${project.basePath}/config",
        )
    }

    private fun getCfgFilesPath(project: Project): Map<PsiElement, String> {
        val map = hashMapOf<PsiElement, String>()
        val folders = getCfgFoldersPath(project)

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
            if (extension in cfgTypes.keys) {
                virtualFile = LocalFileSystem.getInstance().findFileByPath(file.path)!!
                psiFile = PsiManager.getInstance(project).findFile(virtualFile)!!
                map[psiFile] = extension
            }
        }
        return map
    }
}