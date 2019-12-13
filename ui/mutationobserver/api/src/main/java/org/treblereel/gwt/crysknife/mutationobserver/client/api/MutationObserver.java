package org.treblereel.gwt.crysknife.mutationobserver.client.api;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLElement;
import elemental2.dom.MutationObserverInit;
import elemental2.dom.MutationRecord;
import jsinterop.base.Js;

/**
 * @author Dmitrii Tikhomirov
 * Created by treblereel 12/9/19
 */
@Singleton
public class MutationObserver {

    private final elemental2.dom.MutationObserver mutationObserver;

    private final MutationObserverInit mutationObserverInit;

    private final Map<HTMLElement, ObserverCallback> attach = new HashMap<>();

    private final Map<HTMLElement, ObserverCallback> detach = new HashMap<>();

    MutationObserver() {
        mutationObserverInit = MutationObserverInit.create();
        mutationObserverInit.setChildList(true);
        mutationObserverInit.setSubtree(true);

        mutationObserver = new elemental2.dom.MutationObserver((mutationRecords, observer) -> {
            MutationRecord[] records = Js.uncheckedCast(mutationRecords);
            for (int i = 0; i < records.length; i++) {
                if (records[i].type.equals("childList")) {
                    MutationRecord[] added = Js.uncheckedCast(records[i].addedNodes);
                    MutationRecord[] removed = Js.uncheckedCast(records[i].removedNodes);
                    for (int j = 0; j < added.length; j++) {
                        if (attach.containsKey(Js.uncheckedCast(added[i]))) {
                            attach.get(Js.uncheckedCast(added[i])).onAttachOrDetachCallback(added[i]);
                        }
                    }
                    for (int j = 0; j < removed.length; j++) {
                        if (detach.containsKey(Js.uncheckedCast(removed[i]))) {
                            detach.get(Js.uncheckedCast(removed[i])).onAttachOrDetachCallback(removed[i]);
                        }
                    }
                }
            }
            return null;
        });
    }

    @PostConstruct
    public void init() {
        mutationObserver.observe(DomGlobal.document.body, mutationObserverInit);
    }

    public MutationObserver addOnAttachListener(HTMLElement element, ObserverCallback callback) {
        attach.put(element, callback);
        return this;
    }

    public MutationObserver addOnDetachListener(HTMLElement element, ObserverCallback callback) {
        detach.put(element, callback);
        return this;
    }

    public MutationObserver removeOnAttachListener(HTMLElement element) {
        attach.remove(element);
        return this;
    }

    public MutationObserver removeOnDetachListener(HTMLElement element) {
        detach.remove(element);
        return this;
    }

    public void disconnect() {
        mutationObserver.disconnect();
    }
}
