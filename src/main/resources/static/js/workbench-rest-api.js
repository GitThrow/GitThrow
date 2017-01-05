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
 * チケットにコメントする
 * @param projectId プロジェクト
 * @param ticketNum チケット番号
 * @param text コメントの内容
 * @return {*}
 */
function createTicketComment(projectId, ticketNum, text) {
    var req = {
        project: projectId,
        id: ticketNum,
        text: text
    };
    return axios.post('/rest/ticket/comment', req, {
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
