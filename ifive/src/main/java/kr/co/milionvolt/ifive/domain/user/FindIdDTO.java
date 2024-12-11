package kr.co.milionvolt.ifive.domain.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FindIdDTO {
    private String userId;
    private String createdAt;
}
