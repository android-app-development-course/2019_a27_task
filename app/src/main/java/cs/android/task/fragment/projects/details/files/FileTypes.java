package cs.android.task.fragment.projects.details.files;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import androidx.annotation.NonNull;

public enum  FileTypes {
    AUDIO,
    VIDEO,
    IMAGE,
    TEXT,
    NONETYPE
    ;
private static Set<String> audioPostfixs = new HashSet<>();
private static Set<String> videoPostfixs = new HashSet<>();
private static Set<String> imagePostfixs = new HashSet<>();
private static Set<String> textPostfixs = new HashSet<>();

static {
    audioPostfixs.addAll(Arrays.asList(".mp3", ".flac", ".wav", ".ape", ".m4a", ".wma"));
    videoPostfixs.addAll(Arrays.asList(".mp4", ".avi", ".rmvb"));
    imagePostfixs.addAll(Arrays.asList(".jpg", ".png", ".gif", ".bpm"));
    textPostfixs.addAll(Arrays.asList(".txt", ".doc", ".docx", ".pdf"));
}

static public FileTypes getFileType(@NonNull String fileName) {
    Optional<Set<String>> typeSet = Stream.of(audioPostfixs, videoPostfixs, imagePostfixs, textPostfixs)
            .filter(set->set.stream().filter(fileName::endsWith).findAny().isPresent())
            .findFirst();
    if (!typeSet.isPresent())
        return NONETYPE;
    if (typeSet.get() == audioPostfixs ) {
        return AUDIO;
    } else if (typeSet.get() == imagePostfixs) {
        return IMAGE;
    } else if (typeSet.get() == textPostfixs) {
        return TEXT;
    } else {
        return VIDEO;
    }
}

}
