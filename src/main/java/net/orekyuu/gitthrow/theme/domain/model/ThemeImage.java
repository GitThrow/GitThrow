package net.orekyuu.gitthrow.theme.domain.model;

import java.time.LocalDateTime;

public class ThemeImage {
    private final byte[] img;
    private final double opacity;
    private final LocalDateTime lastModified;

    public ThemeImage(byte[] img, double opacity, LocalDateTime lastModified) {
        this.opacity = opacity;
        this.lastModified = lastModified;
        this.img = img;
    }

    public byte[] getImg() {
        return img;
    }

    public double getOpacity() {
        return opacity;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }
}
