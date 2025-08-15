package io.github.arasdenizhan.bts.notificationcore.model.mail.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MailType {
    CHECKED_IN("check-in-template", "Baggage Checked in!"),
    LOADED("loaded-template", "Baggage Loaded to Plane"),
    UNLOADED("unloaded-template", "Baggage Unloaded from Plane"),
    READY_TO_CLAIM("ready-template", "Baggage is Ready to Claim!");

    private final String template;
    private final String title;

    public static MailType getType(String eventStatus){
        return switch (eventStatus) {
            case "CHECKED_IN" -> CHECKED_IN;
            case "LOADED" -> LOADED;
            case "UNLOADED" -> UNLOADED;
            case "DELIVERED" -> READY_TO_CLAIM;
            default -> null;
        };
    }
}
