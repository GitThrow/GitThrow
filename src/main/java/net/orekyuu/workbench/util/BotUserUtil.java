package net.orekyuu.workbench.util;

public final class BotUserUtil {
    private BotUserUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * プロジェクトIDからbotユーザーのIDに変換します
     * @param projectId プロジェクトID
     * @return botユーザーのID
     */
    public static String toBotUserId(String projectId) {
        return projectId + "_bot";
    }

    /**
     * 指定のユーザーIDがbotユーザーであるかチェックします
     * @param userId ユーザーID
     * @return botであればtrue
     */
    public static boolean isBotUserId(String userId) {
        return userId.endsWith("_bot");
    }

    public static boolean isProjectBot(String projectId, String userId) {
        if (!isBotUserId(userId)) {
            return false;
        }
        String projectId2 = userId.substring(0, userId.length() - "_bot".length());
        return projectId.equals(projectId2);
    }
}
