package cmc.mellyserver.mellyinfra.email;

public interface EmailSendService {

    void sendSignupEmail(String email, String nickname);
}
