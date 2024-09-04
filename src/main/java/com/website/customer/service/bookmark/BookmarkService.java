package com.website.customer.service.bookmark;

import com.website.customer.service.bookmark.model.BookmarkDto;
import com.website.common.exception.ClientException;
import com.website.common.exception.ErrorCode;
import com.website.common.repository.bookmark.BookmarkRepository;
import com.website.common.repository.bookmark.model.Bookmark;
import com.website.common.service.item.ItemValidator;
import com.website.customer.service.user.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserValidator userValidator;
    private final ItemValidator itemValidator;

    @Transactional
    public BookmarkDto registerBookmark(Long userId, Long itemId) {
        userValidator.validateUserExists(userId);
        itemValidator.validateItemExists(itemId);

        bookmarkRepository.findByUserIdAndItemId(userId, itemId)
                .ifPresent(mark -> {
                    throw new ClientException(ErrorCode.BAD_REQUEST,
                            "already bookmarked. userId="+ userId +", itemId="+ itemId);
                });

        Bookmark savedBookmark = bookmarkRepository.save(Bookmark.create(userId, itemId));

        return BookmarkDto.of(savedBookmark);

    }

    @Transactional
    public void removeBookmark(Long userId, Long itemId) {
        userValidator.validateUserExists(userId);
        itemValidator.validateItemExists(itemId);

        Bookmark bookmark = bookmarkRepository.findByUserIdAndItemId(userId, itemId)
                .orElseThrow(() -> new ClientException(ErrorCode.BAD_REQUEST,
                        "bookmark not found. userId="+ userId +", itemId="+ itemId));
        bookmarkRepository.delete(bookmark);
    }

    @Transactional(readOnly = true)
    public List<BookmarkDto> getBookmarks(Long userId) {
        userValidator.validateUserExists(userId);
        return bookmarkRepository.findByUserId(userId).stream().map(BookmarkDto::of).collect(Collectors.toList());
    }
}
