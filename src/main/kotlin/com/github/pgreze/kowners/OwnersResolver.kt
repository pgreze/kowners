package com.github.pgreze.kowners

class OwnersResolver(
    val ownerships: List<CodeOwnership>
) {
    fun resolveOwnership(path: String): List<String>? =
        ownerships.firstOrNull { it.pattern.matches(path) }?.owners
}
