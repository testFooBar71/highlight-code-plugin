package difflogic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Optional;

public class DiffGeneratedManagerFiles {
    public Optional<Path> getLatestGeneratedDiffPath() {
        Path dir = Paths.get("C:\\Users\\Dell\\Documents\\outputs\\output");  // specify your directory
        Optional<Path> lfPath = Optional.empty();
        try {
            Optional<Path> lastFilePath = Files.list(dir)    // here we get the stream with full directory listing
                    .filter(f -> !Files.isDirectory(f))  // exclude subdirectories from listing
                    .max(Comparator.comparingLong(f -> f.toFile().lastModified()));  // finally get the last file using simple comparator by lastModified field
            lfPath = lastFilePath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lfPath;
    }
}
