package com.website.common.repository.attachment;

import com.website.common.repository.model.attachment.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long>, AttachmentCustomRepository {

}
