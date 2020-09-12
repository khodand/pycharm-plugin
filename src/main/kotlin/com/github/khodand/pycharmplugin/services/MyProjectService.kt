package com.github.khodand.pycharmplugin.services

import com.intellij.openapi.project.Project
import com.github.khodand.pycharmplugin.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
