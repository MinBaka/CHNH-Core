package com.sighs.baeffect;

import com.sighs.apricityui.init.Document;
import com.sighs.apricityui.init.Element;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BAEffectCursorController {
    public static final String DOCUMENT_PATH = "baeffect/cursor-overlay.html";
    public static final String LAYER_ID = "baeffect-cursor-layer";
    private static final String BASE_CLASS = "cursor-layer";

    public static Document show(CursorStyle style) {
        Document existing = getDocument();
        if (existing != null) existing.remove();
        Document document = Document.create(DOCUMENT_PATH);
        if (document == null) return null;
        document.setReloadPersistent(true);
        applyCursorStyle(document, style);
        return document;
    }

    public static void hide() {
        clearApricityPseudoCursor();
        ArrayList<Document> docs = Document.get(DOCUMENT_PATH);
        for (Document document : docs) {
            if (document != null) {
                applyCursorStyle(document, CursorStyle.HIDDEN);
                document.setReloadPersistent(false);
                document.remove();
            }
        }
    }

    public static Document ensure() {
        Document document = getDocument();
        return document != null ? document : show(CursorStyle.NORMAL);
    }

    public static void setCursorStyle(CursorStyle style) {
        Document document = ensure();
        if (document != null) applyCursorStyle(document, style);
    }

    private static void applyCursorStyle(Document document, CursorStyle style) {
        Element layer = document.getElementById(LAYER_ID);
        if (layer == null) layer = document.querySelector("#" + LAYER_ID);
        if (layer == null) return;
        String nextClass = BASE_CLASS + " " + (style == null ? "cursor-normal" : style.className);
        if (!nextClass.equals(layer.getAttribute("class"))) layer.setAttribute("class", nextClass);
    }

    private static Document getDocument() {
        ArrayList<Document> docs = Document.get(DOCUMENT_PATH);
        return docs.isEmpty() ? null : docs.get(docs.size() - 1);
    }

    private static void clearApricityPseudoCursor() {
        try {
            Class<?> cursorClass = Class.forName("com.sighs.apricityui.style.Cursor");
            Field spec = cursorClass.getDeclaredField("pseudoCursorSpec");
            spec.setAccessible(true);
            spec.set(null, null);
        } catch (Exception ignored) {}
    }

    public enum CursorStyle {
        NORMAL("cursor-normal"), LINK("cursor-link"), MOVE("cursor-move"), TEXT("cursor-text"), BLOCK("cursor-block"), HIDDEN("cursor-hidden");
        private final String className;
        CursorStyle(String className) { this.className = className; }
    }
}