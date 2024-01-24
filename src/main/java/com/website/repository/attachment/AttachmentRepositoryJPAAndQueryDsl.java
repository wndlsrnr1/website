package com.website.repository.attachment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.domain.attachment.Attachment;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.website.domain.attachment.QAttachment.attachment;

@Repository
public class AttachmentRepositoryJPAAndQueryDsl implements AttachmentRepository  {

    //주입
    private final EntityManager entityManager;
    //주입
    private final AttachmentJpaRepository attachmentJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public AttachmentRepositoryJPAAndQueryDsl(EntityManager entityManager, AttachmentJpaRepository attachmentJpaRepository) {
        this.entityManager = entityManager;
        //주입 받은 EntityManager로 생성
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
        this.attachmentJpaRepository = attachmentJpaRepository;
    }

    @Override
    public Attachment findById(Long id) {
        return attachmentJpaRepository.findById(id).get();
    }

    @Override
    public List<Attachment> findByRequestName(String requestName) {
        return jpaQueryFactory.selectFrom(attachment).where(attachment.requestName.eq(requestName)).fetch();
    }

    /**
     *
     * @param saveName is name of file which save in server side
     * @return List<Attachment> but size of List will be 1
     */
    @Override
    public List<Attachment> findBySaveName(String saveName) {
        return jpaQueryFactory.selectFrom(attachment).where(attachment.saveName.eq(saveName)).fetch();
    }

    @Override
    public Attachment saveAttachment(String requestName, String saveName) {
        Attachment newAttachment = new Attachment(requestName, saveName);
        Attachment returnAttachment = attachmentJpaRepository.save(newAttachment);
        return returnAttachment;
    }
    @Override
    public Attachment saveAttachment(Attachment attachmentParam) {
        return attachmentJpaRepository.save(attachmentParam);
    }

    @Override
    public List<Attachment> saveAttachments(List<Attachment> attachments) {
        List<Attachment> list = new ArrayList<>();
        for (Attachment at : attachments) {
            Attachment returnAttachment = saveAttachment(at);
            list.add(returnAttachment);
        }
        return list;
    }

    @Override
    public boolean deleteAttachmentById(Long id) {
        attachmentJpaRepository.deleteById(id);
        return true;
    }
    @Override
    public Long deleteAttachmentBySaveName(String saveName) {
        long rows = jpaQueryFactory.delete(attachment).where(attachment.saveName.eq(saveName)).execute();
        return rows;
    }


}
