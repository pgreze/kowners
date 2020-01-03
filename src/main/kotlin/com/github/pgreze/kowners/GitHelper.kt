package com.github.pgreze.kowners

import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

fun File.lsFiles(target: File? = null) =
    "git ls-files ${target ?: ""}".trim()
        .runCommand(this)
        ?.split('\n')
        ?.filter { it.isNotBlank() }

// https://stackoverflow.com/a/41495542
private fun String.runCommand(workingDir: File): String? {
    try {
        val parts = this.split("\\s".toRegex())
        val proc = ProcessBuilder(*parts.toTypedArray())
            .directory(workingDir)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()

        proc.waitFor(5, TimeUnit.SECONDS)
        return proc.inputStream.bufferedReader().readText()
    } catch(e: IOException) {
        e.printStackTrace()
        return null
    }
}
