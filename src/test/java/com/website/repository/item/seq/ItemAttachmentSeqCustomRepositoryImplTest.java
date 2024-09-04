package com.website.repository.item.seq;

import com.website.common.repository.item.model.ItemAttachmentSequenceResponse;
import com.website.common.repository.item.seq.ItemAttachmentSeqRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
@Slf4j
class ItemAttachmentSeqCustomRepositoryImplTest {

    @Autowired
    ItemAttachmentSeqRepository itemAttachmentSeqRepository;

    @Test
    public void testForselect() throws Exception {
        //given

        List<ItemAttachmentSequenceResponse> byItemId = itemAttachmentSeqRepository.findByItemId(1214L);
        //when

        for (ItemAttachmentSequenceResponse itemAttachmentSequenceResponse : byItemId) {
            log.info("fileId = {}", itemAttachmentSequenceResponse.getFileId());
            log.info("sequence = {}", itemAttachmentSequenceResponse.getSequence());
        }

        //then

    }
}