package leon.web.util;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.time.FastDateFormat;

/**
 * 
 *
 * @createTime Jul 21, 2015 10:26:35 PM
 * @project userLogin
 * @author leon
 */
public class ToStringUtils {
	static transient private final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(ToStringUtils.class);

	public static String toString(String[] ss) {
		if (ss == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append('[');
		for (int i = 0; i < ss.length; i++) {
			if (i != 0) {
				sb.append(',');
			}
			sb.append(ss[i]);
		}
		sb.append(']');
		return sb.toString();
	}

	public static String toString(Object o) {
		if (o == null) {
			return null;
		}
		if (o instanceof Number) {
			return ((Number) o).toString();
		}
		if (o instanceof Map) {
			return "{" + toString((Map) o) + "}";
		}
		if (o instanceof Collection) {
			return "{" + toString((Collection) o) + "}";
		}
		if (o instanceof String[]) {
			return "{" + toString((String[]) o) + "}";
		}
		if (o instanceof StringBuffer) {
			return ((StringBuffer) o).toString();
		}
		return toString(o, null);
	}

	public static String toString(Object o, ToStringStyle tss) {
		if (o == null) {
			return null;
		}
		if (o instanceof Number) {
			return toString(o);
		}
		if (o instanceof String) {
			return toString((String) o);
		}
		if (o instanceof Date) {
			return toString((Date) o);
		}
		if (o instanceof Timestamp) {
			return toString(((Timestamp) o).getTime());
		}
		if (o instanceof Calendar) {
			return toString(((Calendar) o).getTime());
		}
		if (o.getClass().isPrimitive()) {
			return o.toString();
		}
		if (tss == null) {
			return ToStringBuilder.reflectionToString(o);
		} else {
			return ToStringBuilder.reflectionToString(o, tss);
		}
	}

	private static final FastDateFormat fdf = FastDateFormat.getInstance("yyyy/MM/dd HH:mm:ss.S");

	public static String toString(Date d) {
		return fdf.format(d);
	}

	public static String toString(Map map) {
		if (map == null) {
			return null;
		}
		ToStringBuilder builder = new ToStringBuilder(map);
		for (Iterator i = map.keySet().iterator(); i.hasNext();) {
			Object key = i.next();
			Object value = map.get(key);
			builder.append(toString(key), toString(value));
		}
		return builder.toString();
	}

	public static String toString(Collection collection) {
		if (collection == null) {
			return null;
		}
		ToStringBuilder builder = new ToStringBuilder(collection);
		for (Iterator i = collection.iterator(); i.hasNext();) {
			builder.append(toString(i.next()));
		}
		return builder.toString();
	}

	public static String toString(String o) {
		return o;
	}

	public final static String toString(Iterator i, int size) {
		StringBuffer sb = new StringBuffer();
		for (int c = 0; c < size && i.hasNext(); c++) {
			sb.append(toString(i.next()));
		}
		return sb.toString();
	}
}
