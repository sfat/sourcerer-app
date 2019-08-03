// Copyright 2019 Sourcerer Inc. All Rights Reserved.
// Author: Anatoly Kislov (anatoly@sourcerer.io)

package app.extractors

import app.model.CommitStats
import app.model.DiffFile

class DevopsExtractor(private val langName: String) : ExtractorInterface {
    companion object {
        const val DEVOPS = "devops."
        const val JENKINS = "jenkins"
        const val CIRCLECI = "circleci"
        const val GITLABCI = "gitlabci"
        const val TRAVIS = "travis"
        const val K8S = "k8s"
        const val DOCKER = "docker"
    }

    override fun extract(files: List<DiffFile>): List<CommitStats> {
        val vueFiles = files.filter { it.path.endsWith(vueExtension) }
        val otherFiles = files.filter { !it.path.endsWith(vueExtension) }

        // Add stats from *.vue files.
        val stats = listOf(CommitStats(
            numLinesAdded = vueFiles.map { it.getAllAdded().size }.sum(),
            numLinesDeleted = vueFiles.map { it.getAllDeleted().size }.sum(),
            type = ExtractorInterface.TYPE_LIBRARY,
            tech = "js.vue"  //TODO(anatoly): TECH NAME.
        )).filter { it.numLinesAdded > 0 || it.numLinesDeleted > 0 }
        return stats
    }
}
