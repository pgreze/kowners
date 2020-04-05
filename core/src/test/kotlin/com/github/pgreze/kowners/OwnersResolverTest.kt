package com.github.pgreze.kowners

import com.google.common.truth.Truth.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.io.File

class OwnersResolverTest : Spek({

    describe("docs/sample project") {
        val target = File("${System.getProperty("user.dir")}/docs/sample")
        val ownersResolver = target.resolve(CODEOWNERS_FILENAME)
            .readLines()
            .parseCodeOwners()
            .let(::OwnersResolver)

        val files = target.listFilesRecursively().map { it.path }
        val fileToOwners = files
            .map { it to ownersResolver.resolveOwnership(it) }
            .toMap()

        it("resolve all owners") {
            assertThat(fileToOwners).isEqualTo(mapOf(
                "dir/alice/report.txt" to listOf("alice"),
                "dir/bob/notes.md" to listOf("bob"),
                "dir/f3.txt" to null,
                "dir/docs.md" to listOf("maintainer"),
                "dir/read.md" to listOf("maintainer"),
                "charlie.md" to listOf("charlie"),
                "CODEOWNERS" to listOf("boss"),
                "f1.txt" to null,
                "f2.txt" to null,
                "sf.md" to listOf("boss")
            ))
        }
    }
})
