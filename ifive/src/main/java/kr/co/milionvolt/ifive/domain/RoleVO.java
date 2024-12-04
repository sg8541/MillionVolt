package kr.co.milionvolt.ifive.domain;

import lombok.Getter;

@Getter
public class RoleVO {
    private final Integer roleId;
    private final String roleName;

    public RoleVO(Integer roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }
}
