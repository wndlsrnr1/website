package com.website.domain.attachment;

import lombok.*;

import javax.persistence.*;

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

    //@Enumerated(EnumType.STRING)
    //@Column(name = "file_type", nullable = false)
    //private FileType fileType;

    @Builder
    public Attachment(String requestName, String saveName) {
        this.requestName = requestName;
        this.saveName = saveName;
    }

}
