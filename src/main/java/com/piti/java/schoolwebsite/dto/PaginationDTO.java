package com.piti.java.schoolwebsite.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginationDTO {
	private int numberOfElement;
    private int number;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean empty;
    private boolean first;
    private boolean last;
}
