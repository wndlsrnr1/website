package com.website.repository.attachment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.domain.attachment.Attachment;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.website.domain.attachment.QAttachment.attachment;
import static com.website.domain.item.QItemAttachment.*;

@Repository
public class AttachmentCustomRepositoryImpl implements AttachmentCustomRepository {

    //주입
    private final JPAQueryFactory query;
    private final EntityManager em;

    public AttachmentCustomRepositoryImpl(EntityManager em) {
        //주입 받은 EntityManager로 생성
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public List<Attachment> findByRequestName(String requestName) {
        return query.selectFrom(attachment).where(attachment.requestName.eq(requestName)).fetch();
    }

    /**
     * @param saveName is name of file which save in server side
     * @return List<Attachment> but size of List will be 1
     */
    @Override
    public List<Attachment> findBySaveName(String saveName) {
        return query.selectFrom(attachment).where(attachment.saveName.eq(saveName)).fetch();
    }

    @Override
    public List<Attachment> saveAttachments(List<Attachment> attachments) {
        List<Attachment> list = new ArrayList<>();
        for (Attachment at : attachments) {
            em.persist(at);
            list.add(at);
        }
        return list;
    }

    @Override
    public Long deleteAttachmentBySaveName(String saveName) {
        long rows = query.delete(attachment).where(attachment.saveName.eq(saveName)).execute();
        return rows;
    }

    @Override
    public List<Attachment> getAttachmentByItemId(Long itemId) {
        return query.select(attachment)
                .from(itemAttachment)
                .join(attachment)
                .on(itemAttachment.attachment.id.eq(attachment.id))
                .where(itemAttachment.attachment.id.eq(itemId))
                .orderBy(attachment.requestName.asc())
                .fetch();
    }


}
