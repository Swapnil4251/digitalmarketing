/**
 * 
 */
package com.digital.web.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author swapnilsarwade
 *
 */
public class AppUtil {

	@SuppressWarnings("rawtypes")
	public static final List EMPTY_LIST = new ArrayList();
	
	public static boolean isNullOrEmpty(String str) {
		return str == null || str.trim().isEmpty();
	}
	
}
