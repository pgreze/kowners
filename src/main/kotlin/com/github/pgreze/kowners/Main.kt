package com.github.pgreze.kowners

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.NoRunCliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
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
    val target: File by option("--target")
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
    override fun run() {
        echo("Execute query for path=$target")
    }
}
