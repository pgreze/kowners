package com.github.pgreze.kowners

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.clikt.core.NoRunCliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.multiple
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import java.io.File

fun main(args: Array<String>) =
    Kowners()
        .subcommands(Coverage(), Lint(), Query())
        .main(args)

// https://ajalt.github.io/clikt/

class Kowners : NoRunCliktCommand() {
    val verbose: Boolean by option("-v", "--verbose")
        .flag("--no-verbose")
}

abstract class BaseCommand(name: String, help: String) : CliktCommand(name = name, help = help) {
    val target: File by option(help = "Target directory")
        .file(exists = true)
        .default(File("."))
}

class Coverage : BaseCommand(
    name = "coverage",
    help = "display the percentage of files covered by ownership rules"
) {
    override fun run() {
        echo("Execute coverage for path=$target")
    }
}

class Lint : BaseCommand(
    name = "lint",
    help = "warns when new untracked files are added during commit"
) {
    override fun run() {
        echo("Execute lint for path=$target")
    }
}

class Query : BaseCommand(
    name = "query",
    help = "display the potential owner and sub-hierarchy owners"
) {
    val owner: List<String> by option(help = "Filter by owner(s)")
        .multiple()
    val relative: Boolean by option(help = "Display paths related to target path")
        .flag("--absolute")

    override fun run() {
        val gitRootPath = target.findGitRootPath()
            ?: cliError("Invalid git repository: ${target.absolutePath}")
        val codeOwnershipFile = gitRootPath.findCodeOwnerLocations().firstOrNull()
            ?: cliError("CODEOWNERS file not found in git repo $gitRootPath")
        val resolver = OwnersResolver(codeOwnershipFile.readLines().parseCodeOwners())

        val relativeTarget = target.relativeTo(gitRootPath)
        val lsFiles = gitRootPath.lsFiles(relativeTarget)
            ?.takeIf { it.isNotEmpty() }
            ?: cliError("Couldn't resolve tracked files for path $gitRootPath//$relativeTarget")

        lsFiles.map { it to resolver.resolveOwnership(it) }.forEach { (path, owners) ->
            if (owner.isEmpty() || owners?.any { owner.contains(it) } == true) {
                val pathDisplay = path
                    .takeUnless { relative }
                    ?: File(gitRootPath, path).relativeTo(target)
                echo("$pathDisplay (${owners.ownersToString()})")
            }
        }
    }
}

private fun cliError(message: String): Nothing =
    throw CliktError(message)

private fun List<String>?.ownersToString() =
    this?.joinToString(separator = ", ") ?: "??"
