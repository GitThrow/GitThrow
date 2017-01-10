package net.orekyuu.gitthrow.controller.view.user.project;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * プロジェクトメンバー出ないユーザーがアクセスしようとした時
 */
@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class NotMemberException extends RuntimeException {

}
