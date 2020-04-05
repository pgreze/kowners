package com.github.pgreze.kowners

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.clikt.core.NoRunCliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.default
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.file
import java.io.File
import kotlin.math.roundToInt

fun main(args: Array<String>) =
    Kowners().main(args)

// https://ajalt.github.io/clikt/

class Kowners : NoRunCliktCommand() {
    init {
        subcommands(Coverage(), Lint(), Query())
    }
}

abstract class BaseCommand(name: String, help: String) : CliktCommand(name = name, help = help) {
    // Notice: ~/ notation is not possible with `gw run --args "..."` or IntelliJ runners
    val target: File by argument(help = "Target directory (default: working directory)")
        .file(exists = true)
        .default(File(System.getProperty("user.dir")))

    val gitRootPath: File by lazy {
        target.findGitRootPath()
            ?: cliError("Invalid git repository: $target")
    }
    val codeOwnershipFile: File by lazy {
        gitRootPath.findCodeOwnerLocations().firstOrNull()
            ?: cliError("CODEOWNERS file not found in git repo ${gitRootPath.absolutePath}")
    }
    val resolver by lazy { OwnersResolver(codeOwnershipFile.readLines().parseCodeOwners()) }

    val relativeTarget by lazy { target.relativeTo(gitRootPath) }

    val lsFiles by lazy {
        gitRootPath.lsFiles(relativeTarget)
            ?.takeIf { it.isNotEmpty() }
            ?: cliError("Couldn't resolve tracked files for path $gitRootPath//$relativeTarget")
    }
}

class Coverage : BaseCommand(
    name = "coverage",
    help = "display the percentage of files covered by ownership rules"
) {
    override fun run() {
        val ownerToFiles = mutableMapOf<String?, MutableSet<String>>()

        lsFiles.forEach { file ->
            when (val owners = resolver.resolveOwnership(file)) {
                null -> ownerToFiles.addForKey(null, file)
                else -> owners.forEach { owner -> ownerToFiles.addForKey(owner, file) }
            }
        }

        ownerToFiles
            .map { (owner, files) -> owner to files.size.percentOf(lsFiles.size) }
            .sortedByDescending { it.second }
            .forEach { (owner, percent) ->
                echo("${percent}% ${owner ?: "??"}")
            }
    }

    private fun MutableMap<String?, MutableSet<String>>.addForKey(key: String?, value: String) =
        getOrPut(key, ::mutableSetOf).add(value)

    private fun Int.percentOf(total: Int) =
        (toDouble() / total * 100).roundToInt()
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
        lsFiles.map { it to resolver.resolveOwnership(it) }.forEach { (path, owners) ->
            if (owner.isEmpty() || owners?.any { owner.contains(it) } == true) {
                val pathDisplay = path
                    .takeUnless { relative }
                    ?: File(gitRootPath, path).relativeTo(target)
                echo("$pathDisplay ${owners.ownersToString()}")
            }
        }
    }
}

private fun cliError(message: String): Nothing =
    throw CliktError(message)

private fun List<String>?.ownersToString() =
    this?.joinToString(separator = " ") ?: "??"
