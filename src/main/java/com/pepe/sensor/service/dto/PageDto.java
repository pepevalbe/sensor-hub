package com.pepe.sensor.service.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PageDto {
	private int page;
	private int size;

	public PageDto(int page, int size) {
		this.page = page;
		this.size = size;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = Math.max(page, 0);
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = Math.min(50, Math.max(10, size));
	}

	public PageRequest toRequest(Sort.Direction direction, String... properties) {
		return PageRequest.of(page, size, direction, properties);
	}
}
