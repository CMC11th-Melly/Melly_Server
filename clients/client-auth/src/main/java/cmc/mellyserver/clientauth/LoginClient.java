package cmc.mellyserver.clientauth;

import cmc.mellyserver.clientauth.api.LoginClientResult;

/*
현재 Melly에서는 카카오, 네이버, 구글, 애플 OAuth를 사용하고 있습니다. 차후 OAuth는 얼마든지 추가될 수 있기에 확장성을 고려한 설계가 필요합니다.
따라서 LoginClient 인터페이스를 만들어서 구체 클라이언트들이 구현하도록 만들었습니다.
OAuth 리소스 서버로부터 데이터를 받아와야 하는 쪽에서는 누구에게 요청하는지 관심을 가지지 않아도 됩니다.
- support : 해당 클라이언트가 어떤 Provider인지 판별합니다
- getUserData : 실제 유저 데이터를 받아옵니다
 */
public interface LoginClient {

  boolean supports(String provider);

  LoginClientResult getUserData(String accessToken);
}
