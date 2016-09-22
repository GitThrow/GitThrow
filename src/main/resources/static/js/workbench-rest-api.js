/**
 * コメントの一覧を取得
 * @param projectId プロジェクト
 * @param ticketNum チケット番号
 * @return {*}
 */
function fetchTicketComment(projectId, ticketNum) {
    return axios.get('/rest/ticket/comment', {
        params: {
            project: projectId,
            id: ticketNum
        },
        headers: {
            'X-XSRF-TOKEN': Cookies.get('XSRF-TOKEN')
        }
    });
}

/**
 * チケットを更新する
 * @param ticket チケット
 */
function updateTicket(ticket) {
    return axios.post('/rest/ticket', ticket, {
        headers: {
            'X-XSRF-TOKEN': Cookies.get('XSRF-TOKEN')
        }
    });
}

/**
 * すべてのユーザーを取得する
 */
function fetchAllUser() {
    return axios.get('/rest/user/all');
}