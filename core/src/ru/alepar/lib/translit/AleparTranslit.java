package ru.alepar.lib.translit;

import java.util.HashSet;
import java.util.Set;

public class AleparTranslit implements Translit {

    private static String[][] map = new String[][]{
            new String[]{"a", "а"},
            new String[]{"b", "б"},
            new String[]{"v", "в"},
            new String[]{"g", "г"},
            new String[]{"d", "д"},
            new String[]{"e", "е"},
            new String[]{"jo", "ё"},
            new String[]{"yo", "ё"},
            new String[]{"zh", "ж"},
            new String[]{"z", "з"},
            new String[]{"i", "и"},
            new String[]{"j", "й"},
            new String[]{"k", "к"},
            new String[]{"l", "л"},
            new String[]{"m", "м"},
            new String[]{"n", "н"},
            new String[]{"o", "о"},
            new String[]{"p", "п"},
            new String[]{"r", "р"},
            new String[]{"s", "с"},
            new String[]{"t", "т"},
            new String[]{"u", "у"},
            new String[]{"f", "ф"},
            new String[]{"h", "х"},
            new String[]{"c", "ц"},
            new String[]{"ch", "ч"},
            new String[]{"sh", "ш"},
            new String[]{"sh", "щ"},
            new String[]{"'", "ь"},
            new String[]{"y", "ы"},
            new String[]{"'", "ъ"},
            new String[]{"je", "э"},
            new String[]{"ye", "э"},
            new String[]{"ju", "ю"},
            new String[]{"yu", "ю"},
            new String[]{"ja", "я"},
            new String[]{"ya", "я"},
    };

    @Override
    public Set<String> translate(String str) {
        Set<String> result = new HashSet<String>();

        Set<Record> src = new HashSet<Record>();
        Set<Record> dst = new HashSet<Record>();

        src.add(new Record(0, ""));

        while (!src.isEmpty()) {
            for (Record prefix : src) {
                if (("" + str.charAt(prefix.offset)).matches("[^a-zA-Z]")) {
                    Record next = new Record(prefix.offset + 1, prefix.string + str.charAt(prefix.offset));
                    if (next.offset == str.length()) {
                        result.add(next.string);
                    } else {
                        dst.add(next);
                    }
                    break;
                }
                for (int i = 0; i < map.length; i++) {
                    Record next = continues(str, prefix, i);
                    if (next != null) {
                        if (next.offset == str.length()) {
                            result.add(next.string);
                        } else {
                            dst.add(next);
                        }
                    }
                }
            }
            src = dst;
            dst = new HashSet<Record>();
        }
        return result;
    }

    private Record continues(String str, Record prefix, int i) {
        int endIndex = prefix.offset + map[i][0].length();
        if (endIndex > str.length()) {
            return null;
        }
        if (str.substring(prefix.offset, endIndex).equals(map[i][0])) {
            return new Record(endIndex, prefix.string + map[i][1]);
        }
        return null;
    }

    private static class Record {
        private final int offset;
        private final String string;

        private Record(int offset, String string) {
            this.offset = offset;
            this.string = string;
        }
    }

    public static void main(String[] args) {
        Translit translit = new AleparTranslit();
        System.out.println(translit.translate("lukyanenko~"));
    }

}
