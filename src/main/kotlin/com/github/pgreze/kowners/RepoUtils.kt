package com.github.pgreze.kowners

import java.io.File

fun File.findGitRootPath(): File? = when {
    name == GIT_DIRECTORY -> parentFile
    list()?.contains(GIT_DIRECTORY) == true -> this
    else -> parentFile?.findGitRootPath()
}

private const val GIT_DIRECTORY = ".git"

fun File.findCodeOwnerLocations(): List<File> =
    CODEOWNERS_LOCATIONS
        .map { File(this, "$it/$CODEOWNERS_FILENAME") }
        .filter { it.exists() }

val CODEOWNERS_LOCATIONS = arrayOf(
    ".",
    "docs",
    ".github"
)
const val CODEOWNERS_FILENAME = "CODEOWNERS"
