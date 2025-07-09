package org._p1m.foodorderingsystem.features.forgotPassword.service;


public interface ForgotPasswordService {

    void sendResetCode(String email);

    boolean verifyResetCode(String email, long code);
}