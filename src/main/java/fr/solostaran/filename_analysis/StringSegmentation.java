package fr.solostaran.filename_analysis;

import java.util.*;

public class StringSegmentation {

	public static Map<String, Map<String, Integer>> initializeMap(String [] prefix_delimiters) {
		if (prefix_delimiters == null) throw new NullPointerException("list of delimiters cannot be null.");
		if (prefix_delimiters.length == 0) throw new NullPointerException("list of delimiters cannot be empty.");
		Map<String, Map<String, Integer>> map = new HashMap<>();
		//Arrays.stream(prefix_delimiters).forEach(prefix -> map.put(prefix, new HashMap<String, Integer>()));
		for (String prefix : prefix_delimiters)
			map.put(prefix, new HashMap<String, Integer>());
		return map;
	}

	public static void sortMapByValues(Map<String, Map<String, Integer>> map) {

	}

	/**
	 * Segmentation of a string by tags (as delimiters).
	 * @param source the String to process
	 * @param map mapped by delimiters and then compute similar occurrences of Strings
	 * @return the beginning of the source string before any delimiter
	 */
	public static String segmentation(String source, Map<String, Map<String, Integer>> map) {
		// Initial checks
		if (source == null) throw new NullPointerException("Source string cannot be null.");
		if (source.length() == 0) throw new NullPointerException("Source string cannot be empty.");
		if (map == null) throw new NullPointerException("Map cannot be null.");
		Set<String> delimiters = map.keySet();
		if (delimiters.isEmpty()) return source;

		// Search for the first delimiter (and the return value)
		Pair<Integer, String> next = nextDelimiter(source, delimiters, 0);
		if (next == null) throw new StringIndexOutOfBoundsException("No delimiter at all in the source String.");
		String returnString = source.substring(0, next.t);

		// Iterate between delimiters.
		while (next != null) {
			// Search for the next delimiter index
			Pair<Integer, String> current = next;
			next = nextDelimiter(source, delimiters, current.t + current.u.length());

			// Get the value between the current and next delimiters
			String key = null;
			if (next != null) {
				key = source.substring(current.t + current.u.length(), next.t);
			} else {
				key = source.substring(current.t + current.u.length());
			}

			// Write the result of segmentation (value occurrences)
			Map<String, Integer> segment = map.get(current.u);
			Integer value = segment.get(key);
			if (value == null) value = 0;
			segment.put(key, value + 1);
		}
		return returnString;
	}

	/**
	 * Find the first occurrence of a delimiter in a String after a certain index.
	 * @param source processing String
	 * @param delimiters a set of delimiters
	 * @param index start index
	 * @return a pair consisting of the index of the first occurrence of a delimiter and this delimiter.
	 */
	private static Pair<Integer, String> nextDelimiter(String source, Set<String> delimiters, int index) {
		for (int i = index; i < source.length(); i++) {
			for (String delim : delimiters) {
				if (i+delim.length() > source.length()) continue;
				String compare = source.substring(i, i+delim.length());
				if (delim.equals(compare))
					return new Pair(i, delim);
			}
		}
		return null;
	}
}
