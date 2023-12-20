package com.website.repository.attachment;

import com.website.domain.attachment.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface AttachmentJpaRepository extends JpaRepository<Attachment, Long> {
}