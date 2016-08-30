package utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 문자열 유틸
 * Copyright (c) 2016 HanyangRaon. All Rights Reserved.
 * 
 * @author Kei
 * @version 1.0
 * @since 2016. 8. 25.
 */
public class StringUtils {
	
	public static void main(String[] args) {
		// TODO test new util's method
	}
	
	/**
	 * 객체를 문자열로 변환(toString)한다. null일 경우 replaceNull에 적용한 값을 반환한다.
	 * 
	 * @param value 변환할 대상
	 * @param replaceNull null일 경우 대체값
	 * @return 문자열로 변환된 값.
	 */
	public static String objectToString(Object value, String replaceNull) {
		return (value == null) ? replaceNull : value.toString();
	}
	
	/**
	 * 객체를 문자열로 변환(toString)한다. 객체가 null일 경우 ""를 반환한다.
	 * 
	 * @param value 변환할 대상객체
	 * @return 문자열로 변환된 값.
	 */
	public static String objectToString(Object value) {
		return objectToString(value, "");
	}
	
	/**
	 * 문자열 유효성 체크
	 * 
	 * @param value 확인할 값
	 * @return null, 또는 빈 문자열일 경우 false 반환
	 */
	public static boolean isValid(String value) {
		return false == objectToString(value).trim().isEmpty();
	}
	
	/**
	 * null, 또는 빈 문자열 체크
	 * 
	 * @param value 확인할 값
	 * @return 문자열이 null 또는 ""일 경우 true를 반환
	 */
	public static boolean isEmpty(String value) {
		return !isValid(value);
	}
	
	/**
	 * 문자열에서 패턴과 일치하는 부분의 index를 탐색.
	 * 
	 * @param value 탐색할 문자열
	 * @param pattern 정규식 패턴으로 포함될 문자열
	 * @return 패턴과 일치하는 부분의 시작 index 반환. 일치하는 부분이 하나도 없을 경우 -1을 반환.
	 */
	public static int indexOf(String value, String pattern) {
		int result = -1;
		Matcher matcher = Pattern.compile(pattern).matcher(value);
		
		while (matcher.find()) {
			result = matcher.start();
			break;
		}
		
		return result;
	}
	
	/**
	 * 패턴과 일치하는 값을 전부 획득
	 * 
	 * @param value 탐색할 대상 문자열
	 * @param pattern 정규식 패턴으로 포함될 문자열
	 * @return 일치하는 모든 값을 배열로 반환
	 */
	public static ArrayList<String> matchAll(String value, String pattern) {
		ArrayList<String> result = new ArrayList<String>();
		
		Matcher matcher = Pattern.compile(pattern).matcher(value);
		while (matcher.find()) {
			result.add(matcher.group());
			/*
			 * 이건 뭐지??
			 * ArrayList<String> matchItem = new ArrayList<String>();
			 * int cnt = matcher.groupCount() + 1;
			 * for (int i = 0; i < cnt; i++) {
			 * matchItem.add(matcher.group(i));
			 * }
			 * result.addAll(matchItem);
			 */
		}
		
		return result;
	}
	
	/**
	 * 패턴과 일치하는 값을 탐색
	 * 
	 * @param value 탐색할 대상 문자열
	 * @param pattern 정규식 패턴으로 포함될 문자열
	 * @param isFirst true일 경우 최초 매칭된 값을 반환. false일 경우 마지막으로 탐색된 값을 반환
	 * @return 일치한 값을 반환한다.
	 */
	public static String match(String value, String pattern, boolean isFirst) {
		ArrayList<String> matchAll = matchAll(value, pattern);
		
		int size = matchAll.size();
		if (size == 0) {
			return "";
		}
		
		int index = isFirst ? 0 : size - 1;
		return matchAll.get(index);
	}
	
	/**
	 * 문자열의 마지막 단어가 자음으로 끝나는지 판별<br>
	 * 영어일 경우, a/e/i/o/u/w/y를 모음으로 취급하여 결과를 반환한다.
	 *
	 * @param string 판별할 문자열
	 * @return 마지막 단어가 자음으로 끝날 경우 1. 모음으로 끝날 경우 0. 영어/한글 이외의 문자일 경우 -1을 반환
	 */
	public static int isLastCharHasfinalConsonant(String string) {
		char lastChar = string.charAt(string.length() - 1);
		int result = -1;
		
		if (lastChar >= 0xAC00 && lastChar <= 0xD7A3) { // 한글
			result = ((lastChar - 0xAC00) % 28 > 0) ? 1 : 0;
		} else if ((lastChar >= 'a' && lastChar <= 'z') || (lastChar >= 'A' && lastChar <= 'Z')) { // 영어
			char[] engMoums = { 'a', 'e', 'i', 'o', 'u', 'w', 'y', 'A', 'E', 'I', 'O', 'U', 'W', 'Y' };
			for (char c : engMoums) {
				if (lastChar == c) {
					result = 0;
					break;
				}
			}
			if (result != 0) {
				result = 1;
			}
		}
		
		return result;
	}
	
	/**
	 * 문장 마지막 글자의 끝이 자음인가/모음인가에 따라 단락을 구성한다.
	 *
	 * @param string 문장
	 * @param addAfterJaum 끝이 자음일 경우 붙일 글
	 * @param addAfterMoum 끝이 모음일 경우 붙일 글
	 * @return 완성된 문장
	 */
	public static String getComleteWordByJongsung(String string, String addAfterJaum, String addAfterMoum) {
		int checkResult = isLastCharHasfinalConsonant(string);
		String result;
		switch (checkResult) {
		case 1:
			result = string + addAfterJaum;
			break;
		case 0:
			result = string + addAfterMoum;
			break;
		default:
			result = string + addAfterJaum + "(" + addAfterMoum + ")";
			break;
		}
		return result;
	}
	
	// TODO 이하 함수 재확인
	/*
	 * /**
	 * Object를 문자열로 변환하여 문자열이 비었는지 여부를 체크
	 * @param objStr
	 * @return 비어있으면 true
	 * public static boolean isEmpty(Object objStr) {
	 * if (objStr == null) {
	 * return true;
	 * }
	 * String str = objectToString(objStr);
	 * return isEmpty(str);
	 * }
	 * /**
	 * 문자열 대소비교
	 * @param str1 비교 기준이 될 문자열
	 * @param str2 비교할 문자열
	 * @return str1보다 str2가 더 크면 음수값, 같으면 0, 작으면 양수값
	 * public static int compareTo(String str1, String str2) {
	 * return str1.compareTo(str2);
	 * }
	 * /**
	 * 문자열을 디코딩
	 * @param str
	 * @param enc
	 * @return
	 * @throws UnsupportedEncodingException
	 * public static String decode(String str, String enc) throws UnsupportedEncodingException {
	 * return URLDecoder.decode(str, enc);
	 * }
	 * /**
	 * 문자열을 인코딩
	 * @param str
	 * @param enc
	 * @return
	 * @throws UnsupportedEncodingException
	 * public static String encode(String str, String enc) throws UnsupportedEncodingException {
	 * return URLEncoder.encode(str, "UTF-8");
	 * }
	 * /**
	 * 특수문자 앞에 excape 문자 삽입
	 * @param str
	 * @param escape
	 * @return
	 * public static String insertEscapeCharInFrontOfSpecialChars(String str, String escape) {
	 * str = str.replaceAll("_", escape + "_");
	 * str = str.replaceAll("%", escape + "%");
	 * str = str.replaceAll("'", "''"); // '(작은따옴표)는 escape로 처리되지 않았기에 별도처리
	 * return str;
	 * }
	 * /**
	 * 문자열 절삭.
	 * @param str
	 * @param start 문자열 절삭 시작 index. 시작은 0부터. "abcd"를 1부터 시작하면 "bcd"가 된다. 지정한 인덱스부터 절삭. 결과에 포함된다.
	 * @param end 문자열 절삭 종료 index. "abcd"를 2로 지정해서 자르면 "ab"가 된다. 즉 지정한 인덱스 앞까지만 절삭. 결과에 포함되지 않는다.
	 * @return
	 * public static String substring(String str, int start, int end) {
	 * return str.substring(start, end);
	 * }
	 * /**
	 * null을 "" 으로 변환
	 * @param value
	 * @return
	 * public static String nullToEmpty(String value) {
	 * if (value == null) {
	 * value = "";
	 * }
	 * return value;
	 * }
	 * /**
	 * null, ""을 replace 으로 변환
	 * @param value
	 * @return
	 * public static String nullToReplace(String value, String replace) {
	 * if (nullToEmpty(value).equals("")) {
	 * value = replace;
	 * }
	 * return value;
	 * }
	 * /**
	 * null을 int 로 변환
	 * @param value
	 * @return
	 * public static int nullToInt(String value) {
	 * int i = 0;
	 * if (value == null) {
	 * i = 0;
	 * } else {
	 * try {
	 * i = Integer.parseInt(value);
	 * } catch (Exception e) {
	 * }
	 * }
	 * return i;
	 * }
	 * /**
	 * 문자열을 정수로 변환. 변환 실패시 지정한 기본값을 반환한다.
	 * @author sgKim
	 * @param strNum 변환할 문자열
	 * @param defaultValue 변환 실패시 반환될 기본값
	 * @return
	 * public static int parseInt(String strNum, int defaultValue) {
	 * Pattern p = Pattern.compile("^\\d+.?\\d*$");
	 * int num = defaultValue;
	 * if (strNum != null && p.matcher(strNum).find()) {
	 * num = Integer.parseInt(strNum);
	 * }
	 * return num;
	 * }
	 * /**
	 * 문자열을 정수로 변환. 변환 실패시 반환되는 값은 0.
	 * @author sgKim
	 * @param strNum 변환할 문자열
	 * @return
	 * public static int parseInt(String strNum) {
	 * return parseInt(strNum, 0);
	 * }
	 */
	
}
