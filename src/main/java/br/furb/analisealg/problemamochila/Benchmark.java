package br.furb.analisealg.problemamochila;

import java.util.concurrent.TimeUnit;

public class Benchmark {
    private String filename;
    private String recursiveDuration;
    private String bottomUpDuration;

    public Benchmark(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getRecursiveDuration() {
        return recursiveDuration;
    }

    public void setRecursiveDuration(long l1, long l2) {
        long duration = TimeUnit.MILLISECONDS.convert(l2 - l1, TimeUnit.NANOSECONDS);

        long millis = duration % 1000;
        long second = (duration / 1000) % 60;
        long minute = (duration / (1000 * 60)) % 60;

        this.recursiveDuration = String.format("%dmin%dseg.%dms", minute, second, millis);
    }

    public String getBottomUpDuration() {
        return bottomUpDuration;
    }

    public void setBottomUpDuration(long l1, long l2) {
        long duration = TimeUnit.MILLISECONDS.convert(l2 - l1, TimeUnit.NANOSECONDS);

        long millis = duration % 1000;
        long second = (duration / 1000) % 60;
        long minute = (duration / (1000 * 60)) % 60;

        this.bottomUpDuration = String.format("%dmin%dseg.%dms", minute, second, millis);
    }
}
