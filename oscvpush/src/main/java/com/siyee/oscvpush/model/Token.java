package com.siyee.oscvpush.model;

public class Token {

    private String regId;

    private Target target;

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public static Token buildToken(Target target, String regId) {
        Token token = new Token();
        token.setRegId(regId);
        token.setTarget(target);
        return token;
    }

    @Override
    public String toString() {
        return "Token{" +
                "regId='" + regId + '\'' +
                ", target=" + target +
                '}';
    }
}
