package kr.co.milionvolt.ifive.domain;

import lombok.Getter;

@Getter
public class NotificationVO {
    private final Integer notificationId;
    private final String message;
    private final NotificationDTO.Status status;

    public NotificationVO(Integer notificationId, String message, NotificationDTO.Status status) {
        this.notificationId = notificationId;
        this.message = message;
        this.status = status;
    }
}
