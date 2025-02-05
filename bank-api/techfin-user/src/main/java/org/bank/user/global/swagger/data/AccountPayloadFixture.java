package org.bank.user.global.swagger.data;

public class AccountPayloadFixture {

    public static final String CREATE_PAYLOAD = """
                {
                    "profile": {
                        "name" : "테스트",
                        "residentNumber" : "000000-1010101",
                        "age" : 25,
                        "address" : {
                            "city" : "서울",
                            "district" : "강동구",
                            "street" : "도로명 주소",
                            "postCode" : "우편번호",
                            "detailed" : "상세주소"
                        },
                        "email" : "eotjd228@naver.com",
                        "phone" : "010-1234-1234"
                    },
                    "credential" : {
                        "userid" : "testuser",
                        "password" : "testuser",
                        "username" : "테스트"
                    }
                }
                """;

    public static final String EDIT_PAYLOAD = """
                {
                    "profile": {
                        "address" : {
                            "city" : "서울",
                            "district" : "강동구",
                            "street" : "도로명 주소",
                            "postCode" : "우편번호",
                            "detailed" : "상세주소"
                        },
                        "email" : "test@email.mail.net",
                        "phone" : "010-1234-1234"
                    }
                }
                """;

    public static final String AUTHCLAIMS_HEADER_PAYLOAD = "eyAgInVzZXJpZCI6ICJ0ZXN0dXNlciIsICAidXNlcm5hbWUiOiAi7YWM7Iqk7Yq4IiwgICJlbWFpbCI6ICJlb3RqZDIyOEBuYXZlci5jb20ifQ==";

    public static final String LOGIN_PAYLOAD = """
            {
                "userid": "testuser",
                "password": "testuser"
            }
            """;
}
