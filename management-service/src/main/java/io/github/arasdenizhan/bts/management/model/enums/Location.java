package io.github.arasdenizhan.bts.management.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Location {
    IST("Istanbul Airport"),
    SAW("Sabiha Gokcen Airport"),
    ESB("Ankara Esenboga Airport"),
    ADB("Izmir Adnan Menderes Airport"),
    AYT("Antalya Airport"),
    DLM("Dalaman Airport"),
    BJV("Bodrum Milas Airport");

    private final String name;
}
