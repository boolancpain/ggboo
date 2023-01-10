package com.fyo.ggboo.global;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

/**
 * Date Util
 * 
 * @author boolancpain
 */
@Component
public class DateUtil {
	
	/**
	 * LocalDateTime 객체로 변환
	 * 
	 * @param date
	 */
	public LocalDateTime toLocalDateTime(String date) {
		return LocalDateTime.of(LocalDate.parse(date, DateTimeFormatter.ISO_DATE), LocalTime.of(0, 0, 0));
	}
	
	/**
	 * 검색 시작일로 변환
	 * 
	 * @param date("yyyy-MM-dd")
	 */
	public LocalDateTime toStartLocalDateTime(String date) {
		return this.toLocalDateTime(date, 0, 0, 0);
	}
	
	/**
	 * 검색 종료일로 변환
	 * 
	 * @param date("yyyy-MM-dd")
	 */
	public LocalDateTime toEndLocalDateTime(String date) {
		return this.toLocalDateTime(date, 23, 59, 59);
	}
	
	/**
	 * 날짜형태의 문자열을 LocalDateTime 객체로 변환
	 * 
	 * @param date("yyyy-MM-dd")
	 * @param hour
	 * @param minute
	 * @param second
	 */
	public LocalDateTime toLocalDateTime(String date, int hour, int minute, int second) {
		return LocalDateTime.of(LocalDate.parse(date, DateTimeFormatter.ISO_DATE), LocalTime.of(hour, minute, second));
	}
}