# kowners

![Build](https://github.com/pgreze/kowners/workflows/Build/badge.svg?branch=master)

Kotlin support for [Github code owners](https://help.github.com/en/articles/about-code-owners) files.

Features:

- coverage: display the percentage of files covered by ownership rules,
- lint: warns when new untracked files are added during commit,
- query: display the potential owner and sub-hierarchy owners.

## TODO

- Support directory/file distinction
- Support [negative expressions](https://git-scm.com/docs/gitignore#_examples)
