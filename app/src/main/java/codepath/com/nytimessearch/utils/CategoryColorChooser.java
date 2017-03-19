package codepath.com.nytimessearch.utils;


import java.util.HashMap;

public class CategoryColorChooser {

    private static HashMap<String, String> categoryColors = new HashMap<>();

    private static String[] allColors = {"#CDDC39", "#4CAF50", "#FFC107", "#009688", "#E91E63", "#607D8B", "#3F51B5", "#795548"};

    private static int colorIndex = 0;

    public static String getColor(String category) {
        if (categoryColors.containsKey(category)) {
            return categoryColors.get(category);
        } else {
            if (colorIndex >= allColors.length) {
                colorIndex = 0;
            }
            String color = allColors[colorIndex];
            categoryColors.put(category, color);
            colorIndex++;
            return color;
        }

    }
}
