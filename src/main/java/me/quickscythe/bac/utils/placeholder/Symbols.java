package me.quickscythe.bac.utils.placeholder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum Symbols {
    SWORD("\uD83D\uDDE1", SymbolType.TOOL), BOW("\uD83C\uDFF9", SymbolType.TOOL), TRIDENT("\uD83D\uDD31", SymbolType.TOOL), POTION("\uD83E\uDDEA", SymbolType.TOOL), SPLASH_POTION("⚗", SymbolType.TOOL), FISHING_ROD("\uD83C\uDFA3", SymbolType.TOOL), SHIELD("\uD83D\uDEE1", SymbolType.TOOL), AXE("\uD83E\uDE93", SymbolType.TOOL), STAR_1("★", SymbolType.STAR), STAR_2("☆", SymbolType.STAR), STAR_3("✪", SymbolType.STAR), STAR_4("✪", SymbolType.STAR), STAR_5("✯", SymbolType.STAR), STAR_6("٭", SymbolType.STAR), STAR_7("✭", SymbolType.STAR), STAR_8("✰", SymbolType.STAR), STAR_9("⚝", SymbolType.STAR), STAR_10("✴", SymbolType.STAR), STAR_11("✳", SymbolType.STAR), STAR_12("✫", SymbolType.STAR), STAR_13("⍟", SymbolType.STAR), STAR_14("✧", SymbolType.STAR), STAR_15("❂", SymbolType.STAR), SUN("☀", SymbolType.STAR), NITRO("◆", SymbolType.STAR), WARNING("⚠", SymbolType.TOOL), MULTIPLICATION("✖"), CHECK_MARK("✔"), MUSIC_NOTE_1("♪", SymbolType.MUSIC), MUSIC_NOTE_2("♩", SymbolType.MUSIC), MUSIC_NOTE_3("♫", SymbolType.MUSIC), MUSIC_NOTE_4("♬", SymbolType.MUSIC), SCISSORS_1("✄"), SCISSORS_2("✂"), ENVELOPE("✉"), COMET("☄"), SPARKLE_SMALL("࿏"), SPARLE_LARGE("⁂"), SPARKLE_CIRCLE("꙰"), HEART_1("❤"), N0("⓪", SymbolType.NUMBER), N1("①", SymbolType.NUMBER), N2("②", SymbolType.NUMBER), N3("③", SymbolType.NUMBER), N4("④", SymbolType.NUMBER), N5("⑤", SymbolType.NUMBER), N6("⑥", SymbolType.NUMBER), N7("⑦", SymbolType.NUMBER), N8("⑧", SymbolType.NUMBER), N9("⑨", SymbolType.NUMBER), N10("⑩", SymbolType.NUMBER), N11("⑪", SymbolType.NUMBER), N12("⑫", SymbolType.NUMBER), N13("⑬", SymbolType.NUMBER), N14("⑭", SymbolType.NUMBER), N15("⑮", SymbolType.NUMBER), N16("⑯", SymbolType.NUMBER), N17("⑰", SymbolType.NUMBER), N18("⑱", SymbolType.NUMBER), N19("⑲", SymbolType.NUMBER), N20("⑳", SymbolType.NUMBER), BAR_0("█", SymbolType.TEXT), BAR_1("▌", SymbolType.TEXT), BAR_2("▏", SymbolType.TEXT), GEMS("❃"), FLOWER1("✿"), FLOWER2("❀"), FLOWER3("\uD83E\uDD40"), SHAMROCK("☘"), WATCH("⌚"), HOURGLASS_1("⌛"), HOURGLASS_2("⏳"),

    UNKNOWN("???");

    final String unicode;
    final SymbolType[] types;

    Symbols(String unicode) {
        this(unicode, SymbolType.DEFAULT);
    }

    Symbols(String unicode, SymbolType... types) {
        this.unicode = unicode;
        this.types = types;
    }

    @Override
    public String toString() {
        return unicode;
    }

    public List<SymbolType> getTypes() {
        List<SymbolType> r = new ArrayList<>();
        Collections.addAll(r, types);
        return r;
    }

    public enum SymbolType {
        DEFAULT, TOOL, NUMBER, STAR, MUSIC, TEXT

    }
}
