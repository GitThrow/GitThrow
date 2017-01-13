/**
 * コメントの一覧を取得
 * @param projectId プロジェクト
 * @param ticketNum チケット番号
 * @return {*}
 */
function fetchTicketComment(projectId, ticketNum) {
    return axios.get('/rest/'+projectId+'/ticket/'+ticketNum+'/comment', {
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
        projectId: projectId,
        text: text
    };
    return axios.post('/rest/'+projectId+'/ticket/'+ticketNum+'/comment', req, {
        headers: {
            'X-XSRF-TOKEN': Cookies.get('XSRF-TOKEN')
        }
    });
}

/**
 * チケットを更新する
 */
function updateTicket(projectId, ticketNum, ticket) {
    return axios.put('/rest/'+projectId+'/ticket/'+ticketNum, ticket, {
        headers: {
            'X-XSRF-TOKEN': Cookies.get('XSRF-TOKEN')
        }
    });
}

function fetchAllPullRequest(projectId) {
    return axios.get(`/rest/${projectId}/pull-request`, {
        headers: {
            'X-XSRF-TOKEN': Cookies.get('XSRF-TOKEN')
        }
    });
}

function showPullRequest(projectId, prNum) {
    return axios.get(`/rest/${projectId}/pull-request/${prNum}`, {
        headers: {
            'X-XSRF-TOKEN': Cookies.get('XSRF-TOKEN')
        }
    });
}

function createPullRequest(projectId, pullRequest) {
    return axios.post(`/rest/${projectId}/pull-request/`, pullRequest, {
        headers: {
            'X-XSRF-TOKEN': Cookies.get('XSRF-TOKEN')
        }
    });
}

function updatePullRequest(projectId, prNum, pullRequest) {
    return axios.put(`/rest/${projectId}/pull-request/${prNum}`, pullRequest, {
        headers: {
            'X-XSRF-TOKEN': Cookies.get('XSRF-TOKEN')
        }
    });
}

function fetchPullRequestComment(projectId, prNum) {
    return axios.get(`/rest/${projectId}/pull-request/${prNum}/comment`, {
        headers: {
            'X-XSRF-TOKEN': Cookies.get('XSRF-TOKEN')
        }
    });
}

function createPullRequestComment(projectId, prNum, text) {
    var req = {
        projectId: projectId,
        text: text
    };
    return axios.post(`/rest/${projectId}/pull-request/${prNum}/comment`, req, {
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
