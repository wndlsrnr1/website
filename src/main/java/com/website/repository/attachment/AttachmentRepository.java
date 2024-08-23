package com.website.repository.attachment;

import com.website.repository.model.attachment.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long>, AttachmentCustomRepository {

}
