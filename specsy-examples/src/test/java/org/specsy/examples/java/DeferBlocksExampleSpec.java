// Copyright © 2010-2016, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package org.specsy.examples.java;

import org.specsy.java.JavaSpecsy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class DeferBlocksExampleSpec extends JavaSpecsy {
    @Override
    public void run() throws Throwable {
        Path dir = Paths.get("temp-directory-" + UUID.randomUUID());
        Files.createDirectory(dir);
        defer(() -> {
            Files.delete(dir);
        });

        Path file1 = dir.resolve("file 1.txt");
        Files.createFile(file1);
        defer(() -> {
            Files.delete(file1);
        });

        spec("...", () -> {
            // do something with the files
        });

        spec("...", () -> {
            // child specs can also use defer blocks
            Path file2 = dir.resolve("file 2.txt");
            Files.createFile(file2);
            defer(() -> {
                Files.delete(file2);
            });

            // 'file2' will be deleted when this child spec exits
        });
        // will delete first 'file1' and second 'dir'
        // (or if creating 'file1' failed, then will delete only 'dir')
    }
}
