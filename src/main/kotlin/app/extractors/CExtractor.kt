// Copyright 2017 Sourcerer Inc. All Rights Reserved.
// Author: Liubov Yaronskaya (lyaronskaya@sourcerer.io)
// Author: Anatoly Kislov (anatoly@sourcerer.io)

package app.extractors

import app.RegexMeasured

class CExtractor : ExtractorInterface {
    companion object {
        const val LANGUAGE_NAME = Lang.C
        val importRegex = RegexMeasured(
            CLASS_TAG + "importRegex",
            """^([^\n]*#include)\s[^\n]*"""
        )
        val commentRegex = RegexMeasured(
            CLASS_TAG + "commentRegex",
            """^([^\n]*//)[^\n]*"""
        )
        val extractImportRegex = RegexMeasured(
            CLASS_TAG + "extractImport",
            """#include\s+["<](\w+)[/\w+]*\.\w+[">]"""
        )
    }

    override fun extractImports(fileContent: List<String>): List<String> {
        val imports = mutableSetOf<String>()

        fileContent.forEach {
            val res = extractImportRegex.find(it)
            if (res != null) {
                val lineLib = res.groupValues.last()
                imports.add(lineLib)
            }
        }

        return imports.toList()
    }

    override fun tokenize(line: String): List<String> {
        var newLine = importRegex.replace(line, "")
        newLine = commentRegex.replace(newLine, "")
        return super.tokenize(newLine)
    }

    override fun mapImportToIndex(import: String, lang: String,
                                  startsWith: Boolean): String? {
        // TODO(lyaronskaya): Add C to libraries.
        return super.mapImportToIndex(import, Lang.CPP, startsWith = true)
    }

    override fun getLanguageName(): String? {
        return LANGUAGE_NAME
    }
}
