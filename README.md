# kowners

![Build](https://github.com/pgreze/kowners/workflows/Build/badge.svg?branch=master)

Kotlin support for [Github code owners](https://help.github.com/en/articles/about-code-owners) files.

Features:

- coverage: display the percentage of files covered by ownership rules,
- lint: warns when new untracked files are added during commit,
- query: display the potential owner and sub-hierarchy owners.

# Use from command line

## Official releases

See artifacts from https://github.com/pgreze/kowners/releases

Example for Linux:
```
unzip kowners-linux.zip
./kowners
```

## From latest master commits

See artifacts from https://github.com/pgreze/kowners/actions/workflows/master.yml

For the jar distribution:
```
unzip kowners-jar.zip
java -jar kowners.jar
```

For the graalvm distribution:
```
unzip kowners-macos.zip
chmod +x kowners
[ "$(uname)" = "Darwin" ] && sudo xattr -r -d com.apple.quarantine kowners
./kowners
```

## TODO

- Support directory/file distinction
- Support [negative expressions](https://git-scm.com/docs/gitignore#_examples)
