package at.sporty.team1.util;

/**
 * Created by sereGkaluv on 18-Nov-15.
 */
public enum SVGContainer {
    LOGIN_ICON("M 16 3 C 12.154545 3 9 6.1545455 9 10 L 9 13 L 7 13 L 6 13 L 6 14 L 6 28 L 6 29 L 7 29 L 25 29 L 26 29 L 26 28 L 26 14 L 26 13 L 25 13 L 23 13 L 23 10 C 23 6.1545455 19.845455 3 16 3 z M 16 5 C 18.754545 5 21 7.2454545 21 10 L 21 13 L 11 13 L 11 10 C 11 7.2454545 13.245455 5 16 5 z M 8 15 L 24 15 L 24 27 L 8 27 L 8 15 z");

    private final String _svgString;

    SVGContainer(String svgString) {
        _svgString = svgString;
    }

    public String getSVGString(){
        return _svgString;
    }
}
