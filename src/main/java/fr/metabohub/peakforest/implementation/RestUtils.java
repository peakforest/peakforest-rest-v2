package fr.metabohub.peakforest.implementation;

import java.io.File;
import java.lang.reflect.Field;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestUtils {

	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(RestUtils.class);

	/**
	 * Format a date object into a string following the format "yyyy-MM-dd HH:mm"
	 * 
	 * @param date the input date
	 * @return the string formatted date
	 */
	public static OffsetDateTime formatDate(final Date date) {
		return date != null ? (date.toInstant().atOffset(ZoneOffset.UTC)) : null;
	}

	/**
	 * Delete a file after a timeout
	 * 
	 * @param fileToDelete  the file to remove
	 * @param deleteTimeout the timeout to delete the file
	 * @return true if success, false otherwise
	 */
	public static boolean deleteFileThread(//
			final File fileToDelete, //
			final long deleteTimeout) {
		// (re) delete file on exit
		fileToDelete.deleteOnExit();
		// create detached thread
		final Runnable r = new Runnable() {
			public void run() {
				// detached thread core: sleep X ms, then remove file
				try {
					Thread.sleep(deleteTimeout);
					fileToDelete.delete();
				} catch (final InterruptedException e) {
				}
			}
		};
		// run detached tread
		new Thread(r).start();
		return Boolean.TRUE;
	}

	/**
	 * Get a list of String matching a specific attribute from a ref. object
	 * 
	 * @param objectsToMap the list of objects to map
	 * @param property     the attribute name, form the objects to map
	 * @return a list of mapped values
	 */
	public static List<Object> mapEntitesProperty(//
			final List<Object> objectsToMap, //
			final String property) {
		final List<Object> data = new ArrayList<Object>();
		if (!objectsToMap.isEmpty()) {
			for (final Object objectToMap : objectsToMap) {
				data.add(getObjectField(objectToMap, property));
			}
		}
		return data;
	}

	/**
	 * Get a list of objects (basic hashmaps) matching specific attributes from a
	 * ref. object
	 * 
	 * @param objectsToMap the list of objects to map
	 * @param properties   the list of attributes names, form the objects to map
	 * @return a list of mapped values
	 */
	public static List<Map<String, Object>> mapEntitesProperties(//
			final List<Object> objectsToMap, //
			final List<String> properties) {
		final List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		if (!objectsToMap.isEmpty() && !properties.isEmpty()) {
			// get ref. object
			// process each objects
			for (final Object objectToMap : objectsToMap) {
				final Map<String, Object> datum = new HashMap<String, Object>();
				for (final String property : properties) {
					// map field
					datum.put(property, getObjectField(objectToMap, property));
				}
				// add to list of mapped entities
				data.add(datum);
			}
		}
		return data;
	}

	private static Object getObjectField(//
			final Object objectToMap, //
			final String fieldName) {
		try {
			final Field field = getFieldValue(objectToMap.getClass(), fieldName);
			field.setAccessible(Boolean.TRUE);
			return field.get(objectToMap);
		} catch (final IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			logger.error("Cannot map field in object: {}", e.getMessage());
			return null;
		}
	}

	private static Field getFieldValue(//
			final Class<?> classToMap, //
			final String fieldName)//
			throws NoSuchFieldException {
		try {
			return classToMap.getDeclaredField(fieldName);
		} catch (final NoSuchFieldException e) {
			if (!classToMap.equals(Object.class)) {
				return getFieldValue(classToMap.getSuperclass(), fieldName);
			} else {
				throw e;
			}
		}
	}

	/**
	 * Check if the query is a "count" one
	 * 
	 * @param properties the list of properties to process
	 * @return true if "count" matched, false otherwise
	 */
	public static boolean isCountQuery(final List<String> properties) {
		return isKeywordInQuery(properties, "count");
	}

	/**
	 * Check if the query contains a specific keyword
	 * 
	 * @param properties the list of properties to process
	 * @param keyword    the keyword to test
	 * @return true if the keyword is matched, false otherwise
	 */
	public static boolean isKeywordInQuery(final List<String> properties, final String keyword) {
		return (properties != null //
				&& keyword != null //
				&& properties.contains(keyword));
	}

}
