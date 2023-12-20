package com.website.domain.attachment;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attachment {

    @Id
    @GeneratedValue
    @Column(name = "attachment_id", nullable = false)
    private Long id;
    private String requestName;
    private String saveName;

    @Builder
    public Attachment(String requestName, String saveName) {
        this.requestName = requestName;
        this.saveName = saveName;
    }
}
