package demo

/**
 * Created by kk on 17/4/11.
 */
enum class AnsiColor(val code: String) {
    /** Foreground color for ANSI black */
    BLACK("\u001b[30m"),
    /** Foreground color for ANSI red */
    RED("\u001b[31m"),
    /** Foreground color for ANSI green */
    GREEN("\u001b[32m"),
    /** Foreground color for ANSI yellow */
    YELLOW("\u001b[33m"),
    /** Foreground color for ANSI blue */
    BLUE("\u001b[34m"),
    /** Foreground color for ANSI magenta */
    MAGENTA("\u001b[35m"),
    /** Foreground color for ANSI cyan */
    CYAN("\u001b[36m"),
    /** Foreground color for ANSI white */
    WHITE("\u001b[37m"),

    /** Background color for ANSI black */
    BLACK_B("\u001b[40m"),
    /** Background color for ANSI red */
    RED_B("\u001b[41m"),
    /** Background color for ANSI green */
    GREEN_B("\u001b[42m"),
    /** Background color for ANSI yellow */
    YELLOW_B("\u001b[43m"),
    /** Background color for ANSI blue */
    BLUE_B("\u001b[44m"),
    /** Background color for ANSI magenta */
    MAGENTA_B("\u001b[45m"),
    /** Background color for ANSI cyan */
    CYAN_B("\u001b[46m"),
    /** Background color for ANSI white */
    WHITE_B("\u001b[47m"),

    /** Reset ANSI styles */
    RESET("\u001b[0m"),
    /** ANSI bold */
    BOLD("\u001b[1m"),
    /** ANSI underlines */
    UNDERLINED("\u001b[4m"),
    /** ANSI blink */
    BLINK("\u001b[5m"),
    /** ANSI reversed */
    REVERSED("\u001b[7m"),
    /** ANSI invisible */
    INVISIBLE("\u001b[8m")
}