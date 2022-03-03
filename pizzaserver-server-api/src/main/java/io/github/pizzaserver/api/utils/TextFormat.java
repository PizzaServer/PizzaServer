package io.github.pizzaserver.api.utils;

public enum TextFormat {

    BLACK('0'),
    DARK_BLUE('1'),
    DARK_GREEN('2'),
    DARK_AQUA('3'),
    DARK_RED('4'),
    DARK_PURPLE('5'),
    GOLD('6'),
    GRAY('7'),
    DARK_GRAY('8'),
    BLUE('9'),
    GREEN('a'),
    AQUA('b'),
    RED('c'),
    LIGHT_PURPLE('d'),
    YELLOW('e'),
    WHITE('f'),
    MINECOIN_GOLD('g'),
    OBFUSUCATE('k'),
    BOLD('l'),
    STRIKETHROUGH('m'),
    UNDERLINE('n'),
    ITALIC('o'),
    RESET('r');


    private final char letter;


    TextFormat(char letter) {
        this.letter = letter;
    }

    @Override
    public String toString() {
        return new String(new char[] { '\u00A7', this.letter });
    }

}
