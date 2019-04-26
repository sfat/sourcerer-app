// Copyright 2017 Sourcerer Inc. All Rights Reserved.
// Author: Liubov Yaronskaya (lyaronskaya@sourcerer.io)
// Author: Anatoly Kislov (anatoly@sourcerer.io)

package app.extractors

import app.RegexMeasured

class CSharpExtractor : ExtractorInterface {
    companion object {
        const val CLASS_TAG = "CSharpExtractor-"
        const val LANGUAGE_NAME = Lang.CSHARP
        val importRegex = RegexMeasured(
            CLASS_TAG + "importRegex","""^.*using\s+(\w+[.\w+]*)""")
        val commentRegex = RegexMeasured(
            CLASS_TAG + "commentRegex","""^([^\n]*//)[^\n]*""")
        val extractImportRegex = RegexMeasured(
            CLASS_TAG + "extractImportRegex","""using\s+(\w+[.\w+]*)""")
    }

    override fun extractImports(fileContent: List<String>): List<String> {
        val imports = mutableSetOf<String>()

        fileContent.forEach {
            val res = extractImportRegex.find(it)
            if (res != null) {
                imports.add(res.groupValues[1])
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
        return super.mapImportToIndex(import, lang, startsWith = true)
    }

    override fun getLanguageName(): String? {
        return LANGUAGE_NAME
    }
}
