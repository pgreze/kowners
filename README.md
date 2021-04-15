# kowners

![Build](https://github.com/pgreze/kowners/workflows/Build/badge.svg?branch=main)

Kotlin support for [Github code owners](https://help.github.com/en/articles/about-code-owners) files.

Features:

- **blame**: display how many files are covered by each ownership rules.
- **coverage**: display the percentage of files covered by each ownership rules.
- **query**: display the potential owner and sub-hierarchy owners for each versioned file.

# Use from command line

## Official releases

See artifacts from https://github.com/pgreze/kowners/releases

Example for Linux:
```
unzip kowners-linux.zip
./kowners
```

## From latest commits

See artifacts from https://github.com/pgreze/kowners/actions/workflows/main.yml

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
