package com.minbaka.chnhcore;

import com.sighs.apricityui.init.Document;
import com.sighs.apricityui.init.Element;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class CHNHCursorController {
    // 喵！资源路径也要跟着改，否则会找不到 html 喵
    public static final String DOCUMENT_PATH = "chnh_core/cursor-overlay.html";
    public static final String LAYER_ID = "chnh_core-cursor-layer";

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
        if (layer == null) return;
        layer.setAttribute("class", "cursor-layer " + (style == null ? "cursor-normal" : style.className));
    }

    private static Document getDocument() {
        ArrayList<Document> docs = Document.get(DOCUMENT_PATH);
        return docs.isEmpty() ? null : docs.get(docs.size() - 1);
    }

    public enum CursorStyle {
        NORMAL("cursor-normal"), LINK("cursor-link"), MOVE("cursor-move"), TEXT("cursor-text"), BLOCK("cursor-block"), HIDDEN("cursor-hidden");
        public final String className;
        CursorStyle(String className) { this.className = className; }
    }
}