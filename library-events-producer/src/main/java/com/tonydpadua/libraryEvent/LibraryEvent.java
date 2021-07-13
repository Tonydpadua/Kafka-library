package com.tonydpadua.libraryEvent;

import com.tonydpadua.book.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LibraryEvent {

    private Long id;
    private Book book;
}
