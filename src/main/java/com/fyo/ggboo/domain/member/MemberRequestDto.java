package com.fyo.ggboo.domain.member;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;

/**
 * 회원 관련 Request Dto
 * 
 * @author boolancpain
 */
public class MemberRequestDto {
    /**
     * 로그인 Dto
     * 
     * @author boolancpain
     */
    @Getter
    static class LoginDto {
        @NotBlank(message = "{validation.required}")
        private String id;

        @NotBlank(message = "{validation.required}")
        private String password;
    }

    /**
     * 회원 가입 Dto
     * 
     * @author boolancpain
     */
    @Getter
    static class MemberSignupDto {
        @NotBlank(message = "{validation.required}")
        @Pattern(regexp = "[a-zA-Z0-9]{4,12}", message = "{validation.member.id}")
        private String memberId;

        @NotBlank(message = "{validation.required}")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,20}$", message = "{validation.member.password}")
        private String password;

        @NotBlank(message = "{validation.required}")
        @Size(min = 2, max = 10, message = "{validation.member.name}")
        private String memberName;
    }

    /**
     * 회원 수정 Dto
     * 
     * @author boolancpain
     */
    @Getter
    static class MemberUpdateDto {
        @NotBlank(message = "{validation.required}")
        @Size(min = 2, max = 10, message = "{validation.member.name}")
        private String memberName;
    }
}