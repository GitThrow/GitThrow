package net.orekyuu.workbench.service;

import net.orekyuu.workbench.controller.rest.model.PullRequestModel;
import net.orekyuu.workbench.entity.OpenPullRequest;

import java.util.List;
import java.util.Optional;

public interface PullRequestService {

    /**
     * 新しいプルリクエストを作る
     * @param pullRequest プルリクエストの情報
     */
    void create(OpenPullRequest pullRequest);

    /**
     * プロジェクトに存在するプルリクエストの一覧を検索
     * @param projectId プロジェクトID
     * @return プロジェクトに存在するプロジェクト
     */
    List<PullRequestModel> findByProject(String projectId);

    /**
     * プロジェクトと番号からプルリクエストを検索する
     * @param projectId プロジェクトID
     * @param prNum 番号
     * @return 検索結果
     */
    Optional<PullRequestModel> findByProjectAndNum(String projectId, int prNum);

    /**
     * プロジェクトに存在するプルリクエストを削除する
     * @param projectId プロジェクトID
     */
    void deleteByProject(String projectId);

    /**
     * プルリクエストをクローズする
     * @param projectId プロジェクトID
     * @param prNum 番号
     * @param baseCommit ベースブランチのヘッド
     * @param targetCommit ターゲットブランチのヘッド
     */
    void close(String projectId, int prNum, String baseCommit, String targetCommit);
}
