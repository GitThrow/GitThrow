package net.orekyuu.workbench.service.exceptions;

/**
 * ユーザーが既に存在している時にスローされる
 */
public class UserExistsException extends Exception {

    public UserExistsException(String userid, Throwable e) {
        super("userid = " + userid, e);
    }
}
