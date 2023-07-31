package cmc.mellyserver.mellyinfra.email;

public class EmailContentTemplate {

    public String buildCertificationContent(String certificationNumber) {

        StringBuilder builder = new StringBuilder();
        builder.append("[Shoe-Auction] 인증번호는 ");
        builder.append(certificationNumber);
        builder.append("입니다. ");

        return builder.toString();
    }

    public String buildSignupSuccessContent(String nickname) {
        StringBuilder builder = new StringBuilder();
        builder.append(nickname + "님! ");
        builder.append("Melly 서비스에 회원가입을 축하드립니다.");
        builder.append(" 많은 추억을 남기시길 바랍니다.");

        return builder.toString();

    }


}
