// Copyright © 2010-2016, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.examples.groovy

import org.specsy.groovy.GroovySpecsy

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class DeferBlocksExample2Spec extends GroovySpecsy {
    @Override
    void run() {
        Path dir = createWithCleanup(Paths.get("temp-directory-" + UUID.randomUUID()), Files.&createDirectory)
        Path file1 = createWithCleanup(dir.resolve("file 1.txt"), Files.&createFile)

        spec "...", {
        }

        spec "...", {
            Path file2 = createWithCleanup(dir.resolve("file 2.txt"), Files.&createFile)
        }
    }

    private Path createWithCleanup(Path path, Closure create) {
        println "Creating $path"
        create(path)
        defer {
            println "Deleting $path"
            Files.delete(path)
        }
        return path
    }
}
