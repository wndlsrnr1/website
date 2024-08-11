package com.website.repository.model.attachment;

import java.awt.*;


public enum FileType {
    IMAGE("image"), THUMBNAIL("thumbnail"), FILE("file"), ;

    String value;

    FileType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
