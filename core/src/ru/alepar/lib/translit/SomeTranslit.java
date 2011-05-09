package ru.alepar.lib.translit;

public class SomeTranslit implements ToLatTranslit {

    private static String[][] map = new String[][]{
            new String[]{"a", "а"},
            new String[]{"b", "б"},
            new String[]{"v", "в"},
            new String[]{"g", "г"},
            new String[]{"d", "д"},
            new String[]{"e", "е"},
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
            new String[]{"ye", "э"},
            new String[]{"yu", "ю"},
            new String[]{"ya", "я"},
    };

    @Override
    public String lat(String string) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            String c = "" + string.charAt(i);
            boolean found = false;
            for (String[] strings : map) {
                if (strings[1].equals(c)) {
                    result.append(strings[0]);
                    found = true;
                    break;
                }
            }
            if (!found) {
                result.append(c);
            }
        }
        return result.toString();
    }
}
