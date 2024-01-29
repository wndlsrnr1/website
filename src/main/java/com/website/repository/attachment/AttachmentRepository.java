package com.website.repository.attachment;

import com.website.domain.attachment.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long>, AttachmentCustomRepository {

}
