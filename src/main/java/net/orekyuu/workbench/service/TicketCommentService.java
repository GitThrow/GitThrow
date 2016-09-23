package net.orekyuu.workbench.service;

import net.orekyuu.workbench.entity.TicketComment;

import java.util.List;

public interface TicketCommentService {

    /**
     * コメントを作成します
     * @param projectId プロジェクトId
     * @param ticketNum チケット番号
     * @param text コメントの内容
     * @param userId 発言者のユーザーID
     */
    void createComment(String projectId, int ticketNum, String text, String userId);

    /**
     * 指定プロジェクトのコメントを全て削除
     * @param projectId プロジェクトID
     */
    void deleteByProject(String projectId);

    /**
     * チケットにあるコメントを返します
     * @param projectId プロジェクトID
     * @param ticketNum チケット番号
     * @return 指定チケットに存在するコメント
     */
    List<TicketComment> findByTicket(String projectId, int ticketNum);
}
