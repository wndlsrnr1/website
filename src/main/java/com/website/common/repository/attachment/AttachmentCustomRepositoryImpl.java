package com.website.common.repository.attachment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.common.repository.attachment.model.QItemImageInfoForCustomerResponse;
import com.website.common.repository.model.attachment.Attachment;
import com.website.common.repository.attachment.model.ItemImageInfoForCustomerResponse;
import org.springframework.stereotype.Repository;
import static com.website.common.repository.model.attachment.QAttachment.*;
import static com.website.common.repository.model.item.QItemAttachment.*;
import static com.website.common.repository.model.item.QItemAttachmentSeq.itemAttachmentSeq;
import static com.website.common.repository.model.item.QItemThumbnail.itemThumbnail;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<Attachment> getListByIdList(List<Long> fileIdList) {
        List<Attachment> attachmentList = new ArrayList<>();
        for (Long fileId : fileIdList) {
            Attachment findAttachment = em.find(Attachment.class, fileId);
            attachmentList.add(findAttachment);
        }
        return attachmentList;
    }

    @Override
    public List<ItemImageInfoForCustomerResponse> getResponseImageInfoForCustomerResponse(Long itemId) {
        List<ItemImageInfoForCustomerResponse> fetch = query.select(
                        new QItemImageInfoForCustomerResponse(
                                itemAttachment.item.id,
                                attachment.id,
                                attachment.requestName,
                                attachment.saveName,
                                itemThumbnail.attachment.id.eq(itemAttachment.attachment.id),
                                itemAttachmentSeq.seq
                        )
                ).from(attachment)
                .join(itemAttachment).on(attachment.id.eq(itemAttachment.attachment.id))
                .join(itemThumbnail).on(itemAttachment.item.id.eq(itemThumbnail.item.id))
                .join(itemAttachmentSeq).on(itemAttachment.id.eq(itemAttachmentSeq.itemAttachment.id))
                .where(itemAttachment.item.id.eq(itemId))
                .orderBy(itemAttachmentSeq.seq.asc())
                .fetch();

        return fetch;
    }


}
