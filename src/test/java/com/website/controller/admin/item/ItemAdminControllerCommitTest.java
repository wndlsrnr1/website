package com.website.controller.admin.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.website.controller.api.model.request.item.SaveItemRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class ItemAdminControllerCommitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Commit
    public void testSaveItemDetailsByItemFormRequest() throws Exception {
        String basePath = "/Users/ik/Downloads/";
        long[] subcategoryIdArray = {3297, 3298, 3293, 3291, 3290, 3289, 3288, 3299, 3296, 3294};
        List<String> fileNameList = new ArrayList<>();
        for (int i = 2; i <= 14; i++) {
            fileNameList.add("dummy (" + i + ").png");
        }
        List<byte[]> imageByteList = new ArrayList<>();
        for (String fileName : fileNameList) {
            byte[] bytes = Files.readAllBytes(Paths.get(basePath + fileName));
            imageByteList.add(bytes);
        }
        for (int i = 0; i < 10000; i++) {
            // Mock image files
            MockMultipartFile imageFile =
                    new MockMultipartFile("imageFiles", "dummy-image" + i + ".png", MediaType.IMAGE_PNG_VALUE, imageByteList.get((int) (Math.random() * 13)));
            MockMultipartFile thumbnailFile =
                    new MockMultipartFile("thumbnailFile", "dummy-thumbnail" + i + ".png", MediaType.IMAGE_PNG_VALUE, imageByteList.get((int) (Math.random() * 13)));
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbkBhZG1pbi5jb20iLCJpYXQiOjE3MjUzMzUzODksImV4cCI6MTcyNTYzNTM4OX0.1Bu4loR5uy3q9tsr9O7YYS-V4847vezGBIh0BYyxHdjyuEBs7TFEqRsfbWj7lLGx");
            //그럴듯한 이름 만들기, 1년전 부터 지금까지 랜덤 날짜
            mockMvc.perform(multipart("/admin/items/add")
                            .file(imageFile)
                            .file(thumbnailFile)
                            .param("images", "dummy-image" + i + ".png")
                            .param("subcategoryId", String.valueOf(subcategoryIdArray[(int) (Math.random() * 10)]))
                            .param("nameKor", "테스트 상품" + i)
                            .param("name", "Test Product" + i)
                            .param("releasedAt", getRandomDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                            .param("price", String.valueOf((int) (Math.random() * 990000) + 10000))
                            .param("quantity", String.valueOf((int) (Math.random() * 100)))
                            .param("status", "Available")
                            .param("description", "This is a test product.")
                            .param("saleRate", String.valueOf((int) (Math.random() * 20)))
                            .param("brand", "Test Brand")
                            .param("manufacturer", "Test Manufacturer")
                            .param("madeIn", "Korea")
                            .param("thumbnailImage", thumbnailFile.getOriginalFilename())
                            .headers(headers)
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(status().isOk());
        }


    }

    public LocalDateTime getRandomDateTime() {
        // Define the zone (UTC here, but you can use any ZoneId)
        ZoneId zoneId = ZoneId.systemDefault();

        // Current time
        LocalDateTime now = LocalDateTime.now(zoneId);

        // Time one year ago
        LocalDateTime aYearAgo = now.minus(1, ChronoUnit.YEARS);

        // Convert to Instant for easy computation
        Instant nowInstant = now.toInstant(ZoneOffset.UTC);
        Instant aYearAgoInstant = aYearAgo.toInstant(ZoneOffset.UTC);

        // Generate random milliseconds between a year ago and now
        long randomMillis = ThreadLocalRandom.current()
                .nextLong(aYearAgoInstant.toEpochMilli(), nowInstant.toEpochMilli());

        // Convert the random instant back to LocalDateTime in the desired timezone
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(randomMillis), zoneId);
    }

}